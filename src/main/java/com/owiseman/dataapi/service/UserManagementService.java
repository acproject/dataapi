package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.*;
import com.owiseman.dataapi.entity.SysUser;
import com.owiseman.dataapi.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserManagementService {
    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private KeycloakSyncService keycloakSyncService;


    /**
     * 在管理界面盗用它来完成用户列表的分页查询
     * @param projectDto ,这里主要需要传递project api key
     * @param page
     * @param size
     * @return
     */
    public PageResult<SysUser> listUsers(ProjectDto projectDto, int page, int size) {
        return sysUserRepository.findByRealmNameWithPagination(projectDto.getProjectApiKey(), page, size);
    }

    public void deleteNormUser(String userId, String token) {
        keycloakUserService.deleteUserById(userId, token);
    }

    @Transactional
    public void updateUserProfile(String userId, NormSysUserDto updateDto, String token) {
        SysUser user = sysUserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 用户名不允许修改
        if (!user.getUsername().equals(updateDto.getUsername())) {
            throw new RuntimeException("用户名不允许修改");
        }

        // 更新用户信息
//        updateUserInfo(user, updateDto);

        // 同步到 Keycloak
        keycloakUserService.updateNormUser(updateDto, token);

        // 同步到本地数据库
        user.setAttributes(updateDto.getAttributes());
        keycloakSyncService.syncUser(user);
    }

    @Transactional
    public void resetPassword(String userId, String newPassword) {
        SysUser user = sysUserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 重置密码
        keycloakUserService.resetPassword(userId, newPassword);
    }

    @Transactional
    public void updateAvatar(String userId, String avatarUrl, String token) {
        SysUser user = sysUserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 更新头像
        Map<String, List<String>> attributes = user.getAttributes();
        attributes.put("avatar", List.of(avatarUrl));
        user.setAttributes(attributes);

        // 同步到 Keycloak 和本地数据库
        keycloakUserService.updateUserAttributes(userId, attributes, token);
        sysUserRepository.update(user);
    }

    @Transactional
    public void updateUserAttributes(String userId, UserAttributesDto attributes,String token) {
        SysUser user = sysUserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        Map<String, List<String>> userAttributes = new HashMap<>();
        userAttributes.put("avatar", List.of(attributes.avatar()));
        userAttributes.put("phone", List.of(attributes.phone()));
        userAttributes.put("position", List.of(attributes.position()));
        userAttributes.put("department", List.of(attributes.department()));
        userAttributes.put("location", List.of(attributes.location()));
        userAttributes.put("timezone", List.of(attributes.timezone()));
        userAttributes.put("language", List.of(attributes.language()));
        userAttributes.put("theme", List.of(attributes.theme()));
        userAttributes.put("emailNotifications", List.of(attributes.emailNotifications().toString()));
        userAttributes.put("smsNotifications", List.of(attributes.smsNotifications().toString()));

        user.setAttributes(userAttributes);
        keycloakUserService.updateUserAttributes(userId, userAttributes, token);
        sysUserRepository.update(user);
    }

    public void disableUser(String userId) {
        SysUser user = sysUserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setEnabled(false);
        keycloakUserService.updateUserStatus(userId, false);
        sysUserRepository.update(user);
    }

    public void enableUser(String userId) {
        SysUser user = sysUserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setEnabled(true);
        keycloakUserService.updateUserStatus(userId, true);
        sysUserRepository.update(user);
    }

//    private void updateUserInfo(SysUser user, UserUpdateDto updateDto) {
//        // 更新基本信息
//        user.setEmail(updateDto.getEmail());
//        user.setFirstName(updateDto.getFirstName());
//        user.setLastName(updateDto.getLastName());
//
//        // 更新扩展属性
//        Map<String, List<String>> attributes = new HashMap<>();
//        if (user.getAttributes() != null) {
//            attributes.putAll(user.getAttributes());
//        }
//
//        if (updateDto.getPhone() != null) {
//            attributes.put("phone", List.of(updateDto.getPhone()));
//        }
//        if (updateDto.getPosition() != null) {
//            attributes.put("position", List.of(updateDto.getPosition()));
//        }
//        if (updateDto.getDepartment() != null) {
//            attributes.put("department", List.of(updateDto.getDepartment()));
//        }
//        if (updateDto.getLocation() != null) {
//            attributes.put("location", List.of(updateDto.getLocation()));
//        }
//        if (updateDto.getTimezone() != null) {
//            attributes.put("timezone", List.of(updateDto.getTimezone()));
//        }
//        if (updateDto.getLanguage() != null) {
//            attributes.put("language", List.of(updateDto.getLanguage()));
//        }
//        if (updateDto.getTheme() != null) {
//            attributes.put("theme", List.of(updateDto.getTheme()));
//        }
//        if (updateDto.getEmailNotifications() != null) {
//            attributes.put("emailNotifications", List.of(updateDto.getEmailNotifications().toString()));
//        }
//        if (updateDto.getSmsNotifications() != null) {
//            attributes.put("smsNotifications", List.of(updateDto.getSmsNotifications().toString()));
//        }
//
//        user.setAttributes(attributes);
//    }
}