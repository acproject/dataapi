package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RoleServiceImpl implements RoleService {
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

    @Autowired
    private UserService userService;
    @Override
    public void assignRole(String userId, String roleName, String token) {
       UserResource userResource = userService.getUsersResourceById(userId, token);
        RolesResource rolesResource = getRolesResource(token);
        RoleRepresentation representation = rolesResource.get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(representation));
    }

     private RolesResource getRolesResource(String token){
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
        return keycloak.realm(realm).roles();
    }


}
