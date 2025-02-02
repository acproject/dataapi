package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.UserCreateRequest;
import jakarta.ws.rs.core.Response;
import org.jooq.User;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;

@Service
public class KeycloakUserService {
    @Autowired
    private Keycloak keycloak;
    @Value("${keycloak.realm}")
    private String realm;
     @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    // 创建用户
    public String createUser(UserCreateRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEnabled(request.getEnabled());
        user.setEmail(request.getEmail());

        // 密码配置
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        // 调用keycloak API创建用户
        Response response = keycloak.realm(realm).users().create(user);
        return response.getLocation().getPath().split("/")[3]; // 返回用户ID
    }

    public UserRepresentation authenticate(String username, String password) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(username)
                    .password(password)
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
    public void disableUser(String userId) {
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(false);
        keycloak.realm(realm).users().get(userId).update(user);
    }

    public void deleteUser(String userId) {
        keycloak.realm(realm).users().get(userId).remove();
    }

    public void enableUser(String userId) {
        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
        user.setEnabled(true);
        keycloak.realm(realm).users().get(userId).update(user);
    }

    // 分配角色
    public void assignRole(String userId, String roleName) {
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
    }
}
