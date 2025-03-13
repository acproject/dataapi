package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.NormSysUserDto;
import com.owiseman.dataapi.dto.ResetPassword;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import com.owiseman.dataapi.entity.SysUser;
import com.owiseman.dataapi.repository.SysUserRepository;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;

import org.keycloak.representations.idm.CredentialRepresentation;

import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class KeycloakUserService implements UserService {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.urls.auth}")
    private String serverUrl;
    @Value("${keycloak.resource}")
    private String clientId;
    //    @Value("${keycloak.credentials.secret}")
//    private String clientSecret;
    @Value("${keycloak.client-info}")
    private String clientInfo;
    @Value("${keycloak.is-email-verified}")
    private String isEmailVerified;

    private UsersSyncService usersSyncService;

    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    KeycloakAdminUtils keycloakAdminUtils;


    @Autowired
    public KeycloakUserService() {

        this.usersSyncService = new UsersSyncService();
    }

    public UserRepresentation authenticate(String username, String password, String token) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2ConstantsExtends.PASSWORD)
                    .clientId(clientId)
//                    .clientSecret(clientSecret)
                    .username(username)
                    .password(password)
                    .authorization(token)
                    .build();

            UserRepresentation userRepresentation = keycloak.realm(realm).users().search(username, true).get(0);
            if (userRepresentation != null) {
                return userRepresentation;
            } else {
                throw new RuntimeException("Authentication failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed");
        }
    }

    // 禁用用户
    public void disableUser(String userId, String token) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2ConstantsExtends.PASSWORD)
                .clientId(clientId)
//                .clientSecret(clientSecret)
                .username(OAuth2ConstantsExtends.ADMIN)
                .password(clientInfo)
                .authorization(token)
                .build();
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(false);
        usersSyncService.disableUser(userId);
        keycloak.realm(realm).users().get(userId).update(user);
    }


    public void enableUser(String userId, String token) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2ConstantsExtends.PASSWORD)
                .clientId(clientId)
//                .clientSecret(clientSecret)
                .username(OAuth2ConstantsExtends.ADMIN)
                .password(clientInfo)
                .authorization(token)
                .build();
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(true);
        usersSyncService.enableUser(userId);
        keycloak.realm(realm).users().get(userId).update(user);
    }

    /**
     * 该方法用于创建NormUser，可以封装进后续的SDK中
     *
     * @param userRegistrationRecord 需要新增的用户
     * @param token                  admin token
     * @return
     */
    @Override
    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord, String token) {
        if (ObjectUtils.isEmpty(userRegistrationRecord.userId())) {
            throw new RuntimeException("userId is empty");
        }
        String realmName = sysUserRepository.findById(userRegistrationRecord.userId().get()).get().getRealmName();
        String userClientId = sysUserRepository.findById(userRegistrationRecord.userId().get()).get().getClientId();
        Keycloak keycloak = keycloakAdminUtils.getKeyCloak(realmName, serverUrl, "admin-cli", clientInfo, token);
        UserRegistrationRecord result = createUser(userRegistrationRecord, keycloak, realmName, userClientId);

        assignAdminRoleForNewNormRealm(keycloak, result.id(), realmName);
        // 更新用户密码
        String userId = keycloak.realm(realmName).users().search(userRegistrationRecord.username()).get(0).getId();
        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(userRegistrationRecord.password());  // 设置密码值
        password.setTemporary(false);             // 是否临时密码（用户首次登录需修改）
        keycloak.realm(realmName).users().get(userId).resetPassword(password);

        return result;
    }

    public UserRegistrationRecord createUser(NormSysUserDto normSysUserDto, Keycloak keycloak, String realm) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(normSysUserDto.getUsername());
        user.setEmail(normSysUserDto.getEmail());
        user.setFirstName(normSysUserDto.getFirstName());
        user.setLastName(normSysUserDto.getLastName());
        user.setEmailVerified(Boolean.valueOf(isEmailVerified));

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(normSysUserDto.getPassword());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(OAuth2ConstantsExtends.FALSE);
        UsersResource usersResource = getNormUsersResource(keycloak, realm);
        Response response = usersResource.create(user);
        if (Objects.equals(201, response.getStatus())) {
            List<UserRepresentation> representationList = usersResource.search(normSysUserDto.getUsername(), true);
            if (!CollectionUtils.isEmpty(representationList)) {
                UserRepresentation userRepresentation1 = representationList.stream().filter(userRepresentation ->
                        Objects.equals(false, userRepresentation.isEmailVerified())).findFirst().orElse(null);
                assert userRepresentation1 != null;

                if (user.isEmailVerified())
                    emailVerification(keycloak, realm, userRepresentation1.getId());
            }
            var id = representationList.get(0).getId().toString();
            normSysUserDto.setId(id);
            UserRegistrationRecord userRecord = new UserRegistrationRecord(
                    id,
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    normSysUserDto.getPassword(),
                    null,
                    null,
                    null
            );
            usersSyncService.syncNormUsers(normSysUserDto, realm);
            return userRecord;
        } else if (Objects.equals(409, response.getStatus())) {
            throw new RuntimeException("User already exists");
        }
        throw new RuntimeException("Failed to create user");
    }

    /**
     * 该方法用于注册一个新的组织用户时，创建的为管理员
     *
     * @param userRegistrationRecord
     * @param keycloak
     * @param realm
     * @param clientId
     * @return
     */
    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord, Keycloak keycloak, String realm, String clientId) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRegistrationRecord.username());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstname());
        user.setLastName(userRegistrationRecord.lastname());
        user.setEmailVerified(Boolean.valueOf(isEmailVerified));

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistrationRecord.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(OAuth2ConstantsExtends.FALSE);

        UsersResource usersResource = getNormUsersResource(keycloak, realm);
        Response response = usersResource.create(user);
        if (Objects.equals(201, response.getStatus())) {
            List<UserRepresentation> representationList = usersResource.search(userRegistrationRecord.username(), true);
            if (!CollectionUtils.isEmpty(representationList)) {
                UserRepresentation userRepresentation1 = representationList.stream().filter(userRepresentation ->
                        Objects.equals(false, userRepresentation.isEmailVerified())).findFirst().orElse(null);
                assert userRepresentation1 != null;

                if (user.isEmailVerified())
                    emailVerification(keycloak, realm, userRepresentation1.getId());
            }
            var id = representationList.get(0).getId().toString();
            UserRegistrationRecord userRecord = new UserRegistrationRecord(
                    id,
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    userRegistrationRecord.password(),
                    null,
                    null,
                    null
            );
            usersSyncService.syncUsers(userRecord, realm, clientId);
            return userRecord;
        } else if (Objects.equals(409, response.getStatus())) {
            throw new RuntimeException("User already exists");
        }
        throw new RuntimeException("Failed to create user");
    }

    public UsersResource getNormUsersResource(Keycloak keycloak, String realmName) {
        return keycloak.realm(realmName).users();
    }

    public UsersResource getUsersResource(String token) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2ConstantsExtends.PASSWORD)
                .clientId(clientId)
//                .clientSecret(clientSecret)
                .username(OAuth2ConstantsExtends.ADMIN)
                .password(clientInfo)
                .authorization(token)
                .build();
        return keycloak.realm(realm).users();
    }

    @Override
    public UserRepresentation getUserById(String userId, String token) {
        return getUsersResourceById(userId, token).toRepresentation();
    }

    @Override
    public void deleteUserById(String userId, String token) {
        var realmName = sysUserRepository.findById(userId).get().getRealmName();
        Keycloak keycloak = keycloakAdminUtils.getKeyCloak(realmName, serverUrl, "admin-cli", clientInfo, token);
        getNormUsersResource(keycloak, realmName).delete(userId);
        usersSyncService.deleteUser(userId);
    }

    @Override
    public void emailVerification(String userId, String token) {
        UsersResource usersResource = getUsersResource(token);
        usersResource.get(userId).sendVerifyEmail();
    }

    public void emailVerification(Keycloak keycloak, String realm, String userId) {
        UsersResource usersResource = getNormUsersResource(keycloak, realm);
        usersResource.get(userId).sendVerifyEmail();
    }

    @Override
    public UserResource getUsersResourceById(String userId, String token) {
        var usersResource = getUsersResource(token);
        return usersResource.get(userId);
    }

    public UserResource getUsersResourceByIdForNewRealm(Keycloak keycloak, String userId, String realm) {
        var usersResource = getNormUsersResource(keycloak, realm);
        return usersResource.get(userId);
    }

    @Override
    public void updatePassword(String userId, String token) {
        var userResource = getUsersResourceById(userId, token);
        List<String> actions = new ArrayList<>();
        actions.add("UPDATE_PASSWORD");
        userResource.executeActionsEmail(actions);
    }

    @Override
    public void updatePassword(ResetPassword resetPassword, String userId, String token) {
        var userResource = getUsersResourceById(userId, token);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(resetPassword.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        userResource.resetPassword(credentialRepresentation);
    }

    public UserResource updateUser(SysUser sysUser, String token) {
        return updateUser(sysUser.getId(), sysUser.getEmail(),
                sysUser.getFirstName(), sysUser.getLastName()
                , Optional.of(sysUser.getAttributes()), token);
    }

    public UserResource updateNormUser(NormSysUserDto sysUser, String token) {
        var userId = sysUser.getId();
        var realmName =  sysUserRepository.findById(userId).get().getRealmName();

        var newEmail = sysUser.getEmail();
        var newFirstName = sysUser.getFirstName();
        var newLastName = sysUser.getLastName();
        var attributes = sysUser.getAttributes();
        Keycloak keycloak = keycloakAdminUtils.getKeyCloak(realmName, serverUrl, "admin-cli", clientInfo, token);
        var  userResource = getNormUsersResource(keycloak, realmName).get(sysUser.getId());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (!newEmail.equals(userRepresentation.getEmail()) && newEmail.contains("@")) {
            userRepresentation.setEmail(newEmail);
            if (ObjectUtils.isEmpty(sysUser))
                sysUser.setEmail(newEmail);
        }
        if (!newFirstName.equals(userRepresentation.getFirstName()) && !newFirstName.isEmpty()) {
            userRepresentation.setFirstName(newFirstName);
            if (ObjectUtils.isEmpty(sysUser))
                sysUser.setFirstName(newFirstName);
        }
        if (!newLastName.equals(userRepresentation.getLastName()) && !newLastName.isEmpty()) {
            userRepresentation.setLastName(newLastName);
            if (ObjectUtils.isEmpty(sysUser))
                sysUser.setLastName(newLastName);
        }

        if (!attributes.isEmpty()) {
            userRepresentation.setAttributes(attributes);
            sysUser.setAttributes(attributes);
        }

        if (ObjectUtils.isEmpty(sysUser))
            usersSyncService.updateUserById(sysUser);
        userResource.update(userRepresentation);
        return userResource;

    }

    @Override
    public UserResource updateUser(String userId, String newEmail,
                                   String newFirstName, String newLastName,
                                   Optional<Map<String, List<String>>> attributes, String token) {
        var userResource = getUsersResourceById(userId, token);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        var sysUser = usersSyncService.getSysUserById(userId);
        if (!newEmail.equals(userRepresentation.getEmail()) && newEmail.contains("@")) {
            userRepresentation.setEmail(newEmail);
            if (ObjectUtils.isEmpty(sysUser))
                sysUser.setEmail(newEmail);
        }
        if (!newFirstName.equals(userRepresentation.getFirstName()) && !newFirstName.isEmpty()) {
            userRepresentation.setFirstName(newFirstName);
            if (ObjectUtils.isEmpty(sysUser))
                sysUser.setFirstName(newFirstName);
        }
        if (!newLastName.equals(userRepresentation.getLastName()) && !newLastName.isEmpty()) {
            userRepresentation.setLastName(newLastName);
            if (ObjectUtils.isEmpty(sysUser))
                sysUser.setLastName(newLastName);
        }

        if (!attributes.isEmpty()) {
            userRepresentation.setAttributes(attributes.get());
            if (ObjectUtils.isEmpty(sysUser))
                sysUser.setAttributes(attributes.get());
        }

        if (ObjectUtils.isEmpty(sysUser))
            usersSyncService.updateUserById(sysUser);
        userResource.update(userRepresentation);
        return userResource;
    }

    public void assignAdminRoleForNewRealm(Keycloak keycloak, String userId, String realm) {
        UserResource userResource = getUsersResourceByIdForNewRealm(keycloak, userId, realm);
        RolesResource rolesResource = keycloak.realm(realm).roles();
        keycloak.realm(realm).roles().create(keycloak.realm("master").roles().get("admin").toRepresentation());
        RoleRepresentation adminRole = rolesResource.get("admin").toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(adminRole));
    }

    public void assignAdminRoleForNewNormRealm(Keycloak keycloak, String userId, String realm) {
        UserResource userResource = getUsersResourceByIdForNewRealm(keycloak, userId, realm);
        RolesResource rolesResource = keycloak.realm(realm).roles();
        try {
            RoleRepresentation userRole = rolesResource.get("user").toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(userRole));
        } catch (NotFoundException e) {
            RoleRepresentation newUserRole = new RoleRepresentation();
            newUserRole.setName("user");
            newUserRole.setDescription("user");
            rolesResource.create(newUserRole);
        }
    }

    /**
     * 当需要给新创建的realm添加admin角色时，需要先创建admin角色，然后再给用户添加admin角色
     * 如果只需要使用master的realm时可以直接使用该方法
     *
     * @param userId
     * @param token
     */
    public void assignAdminRole(String userId, String token) {
        UserResource userResource = getUsersResourceById(userId, token);
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2ConstantsExtends.PASSWORD)
                .clientId(clientId)
//                .clientSecret(clientSecret)
                .username(OAuth2ConstantsExtends.ADMIN)
                .password(clientInfo)
                .authorization(token)
                .build();
        RolesResource rolesResource = keycloak.realm(realm).roles();
        try {
            RoleRepresentation adminRole = rolesResource.get("admin").toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(adminRole));
        } catch (NotFoundException e) {
            RoleRepresentation newAdminRole = new RoleRepresentation();
            newAdminRole.setName("admin");
            newAdminRole.setDescription("admin");
            rolesResource.create(newAdminRole);
            userResource.roles().realmLevel().add(Collections.singletonList(newAdminRole));
        }
    }

    public void updateUserAttributes(String userId, Map<String, List<String>> attributes, String token) {
        UserResource userResource = getUsersResourceById(userId, token);
        UserRepresentation user = userResource.toRepresentation();
        user.setAttributes(attributes);
        userResource.update(user);
    }

    public void updateUserAttributesForNewRealm(Keycloak keycloak, String userId, Map<String, List<String>> attributes, String realm) {
        UserResource userResource = getUsersResourceByIdForNewRealm(keycloak, userId, realm);
        UserRepresentation user = userResource.toRepresentation();
        user.setAttributes(attributes);
        userResource.update(user);
    }

    public void resetPassword(String userId, String newPassword) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(newPassword);
        credentialRepresentation.setTemporary(false);

        getUsersResourceById(userId, getAdminToken()).resetPassword(credentialRepresentation);
    }

    public void syncUser(SysUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(user.getId());
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEnabled(user.getEnabled());
        userRepresentation.setEmailVerified(user.getEmailVerified());
        userRepresentation.setAttributes(user.getAttributes());

        getUsersResourceById(user.getId(), getAdminToken()).update(userRepresentation);
    }


    private String getAdminToken() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2ConstantsExtends.PASSWORD)
                .clientId(clientId)
                .username(OAuth2ConstantsExtends.ADMIN)
                .password(clientInfo)
                .build()
                .tokenManager()
                .getAccessToken()
                .getToken();
    }

    public void updateUserStatus(String userId, boolean enabled) {
        // 获取用户资源
        UserResource userResource = getUsersResourceById(userId, getAdminToken());
        UserRepresentation userRepresentation = userResource.toRepresentation();

        // 更新用户状态
        userRepresentation.setEnabled(enabled);
        userResource.update(userRepresentation);

        // 同步到本地数据库
        SysUser sysUser = usersSyncService.getSysUserById(userId);
        if (!ObjectUtils.isEmpty(sysUser)) {
            sysUser.setEnabled(enabled);
            usersSyncService.updateUserById(sysUser);
        }
    }
}
