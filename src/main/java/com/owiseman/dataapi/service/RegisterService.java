package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.*;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.repository.KeycloakClientRepository;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import com.owiseman.dataapi.repository.SysUserFilesRepository;
import com.owiseman.dataapi.service.storage.ObjectStorageService;
import com.owiseman.dataapi.service.storage.StorageServiceFactory;
import com.owiseman.dataapi.service.storage.StorageType;
import jakarta.validation.Valid;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Validated
public class RegisterService {

    @Autowired
    private KeycloakAdminUtils KeycloakAdminUtils;

    @Autowired
    private KeycloakRealmService keycloakRealmService;

    @Autowired
    private KeycloakClientService keycloakClientService;

    @Autowired
    private KeycloakClientRepository keycloakClientRepository;

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private KeycloakTokenService keycloakTokenService;

    @Autowired
    private SysUserFilesRepository sysUserFilesRepository;

    @Autowired
    private SysUserConfigRepository sysUserConfigRepository;

    @Autowired
    private StorageServiceFactory storageServiceFactory;

    @Value("${keycloak.urls.auth}")
    private String keycloakAuthUrl;

    @Value("${keycloak.urls.token}")
    private String keycloakTokenUrl;

    @Value("${keycloak.client-info}")
    private String clientInfo;

    @Transactional(rollbackFor = Exception.class)
    public void register(@Valid RegisterDto registerDto, String token) {
        // 验证密码匹配
        if (!registerDto.isPasswordMatch()) {
            throw new RuntimeException("密码和确认密码不匹配");
        }

        // 获取管理员token
        String adminToken = token;
        // 获得存储类型
        StorageType type = registerDto.storageType().isEmpty() ? StorageType.seaweedfs :
                StorageType.valueOf(registerDto.storageType().get());

        KeycloakRealmDto realmDto = null;
        try {
            //  创建Realm（使用组织名作为realm名）
            String realmName = registerDto.organization().toLowerCase().replaceAll("\\s+", "_");
            Keycloak keycloak = KeycloakAdminUtils.getKeyCloak(realmName, keycloakAuthUrl, "admin-cli", clientInfo, adminToken);
            realmDto = keycloakRealmService.createRealm(realmName, adminToken);

            // 添加一个默认的Client
            String clientId = realmName + "-admin" + "-client";

            ClientRepresentation clientRepresentation = new ClientRepresentation();
            clientRepresentation.setName(clientId);
            clientRepresentation.setClientId(clientId);
            clientRepresentation.setEnabled(true);
            clientRepresentation.setRedirectUris(new ArrayList<String>());
            clientRepresentation.setBearerOnly(false);
            clientRepresentation.setServiceAccountsEnabled(true);
            clientRepresentation.setPublicClient(false);
            clientRepresentation.setDirectAccessGrantsEnabled(true);

           KeycloakClientDto keycloakClientDto =
                   keycloakClientService.createClient(clientRepresentation, keycloak, realmName);

            // 2. 在新Realm中创建管理员用户
            UserRegistrationRecord userRecord = new UserRegistrationRecord(
                    null,
                    registerDto.username(),
                    registerDto.email(),
                    registerDto.firstName(),
                    registerDto.lastName(),
                    registerDto.password(),
                    null
                    ,null
                    ,null
            );
            // 创建用户
            UserRegistrationRecord createdUser = keycloakUserService.createUser(userRecord, keycloak, realmDto.getName(), clientId);

            //  为用户添加管理员角色
            keycloakUserService.assignAdminRoleForNewRealm(keycloak, createdUser.id(), realmName);

            //  添加用户的额外属性（如电话号码）
            keycloakUserService.updateUserAttributesForNewRealm(keycloak,
                    createdUser.id(),
                    Map.of("phone", List.of(registerDto.phone())),
                    realmName
            );
            // 更新用户密码
            String userId = keycloak.realm(realmName).users().search(registerDto.username()).get(0).getId();
            CredentialRepresentation password = new CredentialRepresentation();
            password.setType(CredentialRepresentation.PASSWORD);
            password.setValue(registerDto.password());  // 设置密码值
            password.setTemporary(false);             // 是否临时密码（用户首次登录需修改）
            keycloak.realm(realmName).users().get(userId).resetPassword(password);

            // 创建用户配置
            createUserConfig(createdUser.id(), realmName);

            // 5. 创建用户的根目录
            createUserRootDirectory(createdUser.id(), realmName, Optional.ofNullable(type.getType()));

        } catch (Exception e) {
            // 如果创建过程中出现错误，回滚所有操作
            if (realmDto != null) {
                keycloakRealmService.deleteRealmByName(realmDto.getName(), adminToken);
            }
            if (e.getMessage().contains("409")) {
                throw new RuntimeException("组织名已存在");
            }
            throw new RuntimeException("注册失败: " + e.getMessage());
        }
    }

    private void createUserConfig(String userId, String realmName) {
        SysUserConfig config = new SysUserConfig();
        config.setUserId(userId);
        config.setKeycloakRealm(realmName);
        config.setKeycloakAuthUrl(keycloakAuthUrl);
        // 替换 token URL 中的 master 为新的 realm 名称
        String tokenUrl = keycloakTokenUrl.replace("/realms/master/", "/realms/" + realmName + "/");
        config.setKeycloakTokenUrl(tokenUrl);

        // 设置数据库表前缀
        config.setDatabaseTableNamePrefix(realmName.toLowerCase() + "_");

        sysUserConfigRepository.save(config);
    }

    private void createUserRootDirectory(String userId, String realmName, Optional<String> type) {
        // 创建用户的根目录
        SysUserFile rootDir = createDirectory(userId, realmName, null, "/" + realmName, type);

        // 创建证书目录
        String certPath = rootDir.getPath() + "/certificates";
        createDirectory(userId, "certificates", rootDir.getId(), certPath, type);

        // 分别创建 APNS 和 Firebase 证书目录
        String apnsPath = certPath + "/apns";
        String firebasePath = certPath + "/firebase";
        createDirectory(userId, "apns", rootDir.getId(), apnsPath, type);
        createDirectory(userId, "firebase", rootDir.getId(), firebasePath, type);

        // 创建媒体文件目录
        String mediaPath = rootDir.getPath() + "/media";
        SysUserFile mediaDir = createDirectory(userId, "media", rootDir.getId(), mediaPath, type);

        // 创建媒体子目录
        createDirectory(userId, "images", mediaDir.getId(), mediaPath + "/images", type);
        createDirectory(userId, "audio", mediaDir.getId(), mediaPath + "/audio", type);
        createDirectory(userId, "video", mediaDir.getId(), mediaPath + "/video", type);
        createDirectory(userId, "documents", mediaDir.getId(), mediaPath + "/documents", type);
        createDirectory(userId, "avatars", mediaDir.getId(), mediaPath + "/avatars", type);
        createDirectory(userId, "others", mediaDir.getId(), mediaPath + "/others", type);

        // 创建小程序专用目录
        String miniProgramPath = rootDir.getPath() + "/mini_programs";
        SysUserFile miniProgramDir = createDirectory(userId, "mini_programs", rootDir.getId(), miniProgramPath, type);

        // 创建微信小程序子目录
        createDirectory(userId, "wechat", miniProgramDir.getId(), miniProgramPath + "/wechat", type);

        // 创建支付宝小程序子目录
        createDirectory(userId, "alipay", miniProgramDir.getId(), miniProgramPath + "/alipay", type);
    }

    private SysUserFile createDirectory(String userId, String dirName, String parentId, String path, Optional<String> type) {
        SysUserFile dir = new SysUserFile();
        dir.setUserId(userId);
        dir.setFileName(dirName);
        dir.setFid("dir_" + dirName + "_" + System.currentTimeMillis());
        dir.setSize(0L);
        dir.setUploadTime(LocalDateTime.now());
        dir.setDirectory(true);
        dir.setPath(path);
        dir.setParentId(parentId);

        String defaultStorageType = type.orElse("seaweedfs");
        try {
            ObjectStorageService storageService = storageServiceFactory.getService(defaultStorageType);
            // 调用存储服务创建实际目录（路径需要转换为存储服务格式）
            String storagePath = path.replace("/" + dirName, "") + "/" + dirName;
            storageService.createDirectory(userId, storagePath);
        } catch (Exception e) {
            // 如果存储服务操作失败，回滚数据库操作
            sysUserFilesRepository.deleteById(dir.getId());
            throw new RuntimeException("目录创建失败: " + e.getMessage());
        }
        return sysUserFilesRepository.save(dir);
    }

    private String normalizePath(String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }
}