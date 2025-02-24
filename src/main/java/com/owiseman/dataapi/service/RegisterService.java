package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.KeycloakRealmDto;
import com.owiseman.dataapi.dto.RegisterDto;
import com.owiseman.dataapi.dto.TokenResponse;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import com.owiseman.dataapi.repository.SysUserFilesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class RegisterService {
    
    @Autowired
    private KeycloakRealmService keycloakRealmService;

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private KeycloakTokenService keycloakTokenService;

    @Autowired
    private SysUserFilesRepository sysUserFilesRepository;

    @Autowired
    private SysUserConfigRepository sysUserConfigRepository;

    @Value("${keycloak.urls.auth}")
    private String keycloakAuthUrl;

    @Value("${keycloak.urls.token}")
    private String keycloakTokenUrl;

    @Transactional
    public void register(@Valid RegisterDto registerDto) {
        // 验证密码匹配
        if (!registerDto.isPasswordMatch()) {
            throw new RuntimeException("密码和确认密码不匹配");
        }

        // 获取管理员token
        TokenResponse adminTokenResponse = keycloakTokenService.getTokenResponse();
        String adminToken = adminTokenResponse.getAccessToken();

        KeycloakRealmDto realmDto = null;
        try {
            // 1. 创建Realm（使用组织名作为realm名）
            String realmName = registerDto.organization().toLowerCase().replaceAll("\\s+", "_");
            realmDto = keycloakRealmService.createRealm(realmName, adminToken);

            // 2. 在新Realm中创建管理员用户
            UserRegistrationRecord userRecord = new UserRegistrationRecord(
                    null,
                    registerDto.username(),
                    registerDto.email(),
                    registerDto.firstName(),
                    registerDto.lastName(),
                    registerDto.password()
            );

            // 创建用户
            UserRegistrationRecord createdUser = keycloakUserService.createUser(userRecord, adminToken);

            // 3. 为用户添加管理员角色
            keycloakUserService.assignAdminRole(createdUser.id(), adminToken);

            // 4. 添加用户的额外属性（如电话号码）
            keycloakUserService.updateUserAttributes(
                    createdUser.id(),
                    Map.of("phone", List.of(registerDto.phone())),
                    adminToken
            );

            // 创建用户配置
            createUserConfig(createdUser.id(), realmName);

            // 5. 创建用户的根目录
            createUserRootDirectory(createdUser.id(), realmName);

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

    private void createUserRootDirectory(String userId, String realmName) {
        // 创建用户的根目录
        SysUserFile rootDir = createDirectory(userId, realmName, null, "/" + realmName);
    
        // 创建证书目录
        String certPath = rootDir.getPath() + "/certificates";
        createDirectory(userId, "certificates", rootDir.getId(), certPath);
    
        // 分别创建 APNS 和 Firebase 证书目录
        String apnsPath = certPath + "/apns";
        String firebasePath = certPath + "/firebase";
        createDirectory(userId, "apns", rootDir.getId(), apnsPath);
        createDirectory(userId, "firebase", rootDir.getId(), firebasePath);
    
        // 创建媒体文件目录
        String mediaPath = rootDir.getPath() + "/media";
        SysUserFile mediaDir = createDirectory(userId, "media", rootDir.getId(), mediaPath);
    
        // 创建媒体子目录
        createDirectory(userId, "images", mediaDir.getId(), mediaPath + "/images");
        createDirectory(userId, "audio", mediaDir.getId(), mediaPath + "/audio");
        createDirectory(userId, "video", mediaDir.getId(), mediaPath + "/video");
        createDirectory(userId, "documents", mediaDir.getId(), mediaPath + "/documents");
    }

    private SysUserFile createDirectory(String userId, String dirName, String parentId, String path) {
        SysUserFile dir = new SysUserFile();
        dir.setUserId(userId);
        dir.setFileName(dirName);
        dir.setFid("dir_" + dirName + "_" + System.currentTimeMillis());
        dir.setSize(0L);
        dir.setUploadTime(LocalDateTime.now());
        dir.setDirectory(true);
        dir.setPath(path);
        dir.setParentId(parentId);
        return sysUserFilesRepository.save(dir);
    }
}