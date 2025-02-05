package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.ResetPassword;
import com.owiseman.dataapi.dto.UserCreateRequest;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import jakarta.ws.rs.core.Response;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class KeycloakUserService implements UserService {

    @Value("${keycloak.realm}")
    private String realm;
     @Value("${keycloak.urls.auth}")
    private String serverUrl;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${keycloak.user-info}")
    private String userInfo;



    public UserRepresentation authenticate(String username, String password, String token) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
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
        }catch (Exception e) {
             throw new RuntimeException("Authentication failed");
        }
    }

    // 禁用用户
    public void disableUser(String userId, String token) {
        Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(OAuth2ConstantsExtends.USER_ADMIN)
                    .password(userInfo)
                    .authorization(token)
                    .build();
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(false);
        keycloak.realm(realm).users().get(userId).update(user);
    }


    public void enableUser(String userId, String token) {
        Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(OAuth2ConstantsExtends.USER_ADMIN)
                    .password(userInfo)
                    .authorization(token)
                    .build();
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(true);
        keycloak.realm(realm).users().get(userId).update(user);
    }

    @Override
    public UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord, String token) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userRegistrationRecord.username());
        user.setEmail(userRegistrationRecord.email());
        user.setFirstName(userRegistrationRecord.firstName());
        user.setLastName(userRegistrationRecord.lastName());
        user.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistrationRecord.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);

        UsersResource usersResource = getUsersResource(token);

        Response response = usersResource.create(user);
        if (Objects.equals(201, response.getStatus())) {
            List<UserRepresentation> representationList = usersResource.search(userRegistrationRecord.username(), true);
            if(!CollectionUtils.isEmpty(representationList)) {
                UserRepresentation userRepresentation1 = representationList.stream().filter(userRepresentation ->
                        Objects.equals(false, userRepresentation.isEmailVerified())).findFirst().orElse(null);
                assert  userRepresentation1 != null;
                emailVerification(userRepresentation1.getId(),token);
            }
             return userRegistrationRecord;
        }
        return null;
    }

    private UsersResource getUsersResource(String token) {
        Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(OAuth2ConstantsExtends.USER_ADMIN)
                    .password(userInfo)
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
}
