package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.KeycloakRealmDto;
import com.owiseman.dataapi.dto.RegisterDto;
import com.owiseman.dataapi.dto.TokenResponse;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.repository.SysUserFilesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    private void createUserRootDirectory(String userId, String realmName) {
        // 创建用户的根目录
        SysUserFile rootDir = new SysUserFile();
        rootDir.setUserId(userId);
        rootDir.setFileName(realmName);
        rootDir.setFid("root_" + realmName);
        rootDir.setSize(0L);
        rootDir.setUploadTime(LocalDateTime.now());
        rootDir.setDirectory(true);
        rootDir.setPath("/" + realmName); // 根目录路径
        rootDir.setParentId(null); // 根目录没有父目录
        sysUserFilesRepository.save(rootDir);
    }
}