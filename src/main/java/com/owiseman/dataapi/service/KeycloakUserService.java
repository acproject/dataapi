package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.ResetPassword;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import jakarta.ws.rs.core.Response;

//import org.jooq.DSLContext;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;

import org.keycloak.representations.idm.CredentialRepresentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord, String token) {
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

        UsersResource usersResource = getUsersResource(token);
        Response response = usersResource.create(user);
        if (Objects.equals(201, response.getStatus())) {
            List<UserRepresentation> representationList = usersResource.search(userRegistrationRecord.username(), true);
            if (!CollectionUtils.isEmpty(representationList)) {
                UserRepresentation userRepresentation1 = representationList.stream().filter(userRepresentation ->
                        Objects.equals(false, userRepresentation.isEmailVerified())).findFirst().orElse(null);
                assert userRepresentation1 != null;

                if (user.isEmailVerified())
                    emailVerification(userRepresentation1.getId(), token);
            }
            var id = representationList.get(0).getId().toString();
            UserRegistrationRecord userRecord = new UserRegistrationRecord(
                    id,
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    ""
            );
            usersSyncService.syncUsers(userRecord);
            return userRecord;
        } else if (Objects.equals(409, response.getStatus())) {
            throw new RuntimeException("User already exists");
        }
        throw new RuntimeException("Failed to create user");
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
        usersSyncService.deleteUser(userId);
        getUsersResource(token).delete(userId);
    }

    @Override
    public void emailVerification(String userId, String token) {
        UsersResource usersResource = getUsersResource(token);
        usersResource.get(userId).sendVerifyEmail();
    }

    @Override
    public UserResource getUsersResourceById(String userId, String token) {
        var usersResource = getUsersResource(token);
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

    public void assignAdminRole(String userId, String token) {
        UserResource userResource = getUsersResourceById(userId, token);
        RolesResource rolesResource = keycloak.realm(realm).roles();
        RoleRepresentation adminRole = rolesResource.get("admin").toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(adminRole));
    }

    public void updateUserAttributes(String userId, Map<String, List<String>> attributes, String token) {
        UserResource userResource = getUsersResourceById(userId, token);
        UserRepresentation user = userResource.toRepresentation();
        user.setAttributes(attributes);
        userResource.update(user);
    }
}
