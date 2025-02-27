package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.KeycloakClientDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysKeycloakClient;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class KeycloakClientService {


    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-info}")
    private String clientInfo;

    @Value("${keycloak.urls.auth}")
    private String serverUrl;
    @Value("${keycloak.resource}")
    private String clientId;
//    @Value("${keycloak.credentials.secret}")
//    private String clientSecret;

    @Autowired
    private KeycloakSyncService keycloakSyncService;

    private Keycloak getKeycloak(String token) {
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
        return keycloak;
    }
    public KeycloakClientDto createClient(ClientRepresentation clientRepresentation, String token ) {
        Keycloak keycloak = getKeycloak(token);
        RealmResource realmResource = keycloak.realm(realm);
        ClientsResource clients = realmResource.clients();

        // 创建Client
        Response response = clients.create(clientRepresentation);

        if (!Objects.equals(201, response.getStatus())) {
            throw new RuntimeException("创建Client失败: " + response.getStatusInfo());
        }

        String clientId = getClientId(response);

        // 获取Client Secret
        ClientResource clientResource = clients.get(clientId);

        CredentialRepresentation secret = clientResource.getSecret();
        clientRepresentation.setSecret(secret.getValue());
        // 同步到数据库
        SysKeycloakClient sysKeycloakClient = new SysKeycloakClient();
        sysKeycloakClient.setId(clientRepresentation.getId());
        sysKeycloakClient.setClientId(clientRepresentation.getClientId());
        sysKeycloakClient.setSecret(clientRepresentation.getSecret());
        sysKeycloakClient.setName(clientRepresentation.getName());
        sysKeycloakClient.setDescription(clientRepresentation.getDescription());
        sysKeycloakClient.setType(clientRepresentation.getType());
        sysKeycloakClient.setRootUrl(clientRepresentation.getRootUrl());
        sysKeycloakClient.setAdminUrl(clientRepresentation.getAdminUrl());
        sysKeycloakClient.setBaseUrl(clientRepresentation.getBaseUrl());
        sysKeycloakClient.setSurrogateAuthRequired(clientRepresentation.isSurrogateAuthRequired());
        sysKeycloakClient.setEnabled(clientRepresentation.isEnabled());
        sysKeycloakClient.setAlwaysDisplayInConsole(clientRepresentation.isAlwaysDisplayInConsole());
        sysKeycloakClient.setClientAuthenticatorType(clientRepresentation.getClientAuthenticatorType());
        sysKeycloakClient.setRegistrationAccessToken(clientRepresentation.getRegistrationAccessToken());
        sysKeycloakClient.setRealmName(realm);
        keycloakSyncService.syncClient(sysKeycloakClient);
        return new KeycloakClientDto(
                clientRepresentation.getId(),
                clientRepresentation.getClientId(),
                clientRepresentation.getSecret(),
                clientRepresentation.getName(),
                clientRepresentation.getDescription(),
                clientRepresentation.getType(),
                clientRepresentation.getRootUrl(),
                clientRepresentation.getAdminUrl(),
                clientRepresentation.getBaseUrl(),
                clientRepresentation.isSurrogateAuthRequired(),
                clientRepresentation.isEnabled(),
                clientRepresentation.isAlwaysDisplayInConsole(),
                clientRepresentation.getClientAuthenticatorType(),
                clientRepresentation.getRegistrationAccessToken()
        );
    }

    private String getClientId(Response response) {
        String location = response.getLocation().getPath();
        return location.substring(location.lastIndexOf('/') + 1);
    }

    public List<ClientRepresentation> findAllClients(String token) {
        Keycloak keycloak = getKeycloak(token);
        return keycloak.realm(realm).clients().findAll();
    }

    public PageResult<ClientRepresentation> findClientsByPage(int page, int size,String token) {
        Keycloak keycloak = getKeycloak(token);
        // 计算分页参数
        int firstResult = (page - 1) * size;
        int maxResults = size;
        List<ClientRepresentation> clients =
                keycloak.realm(realm)
                        .clients()
                        .findAll(null, true, true,firstResult, maxResults);
        int total = keycloak.realm(realm).clients().findAll().size();
        return new PageResult<>(clients, page, size, total);

    }

    public void deleteClient(String clientId, String token) {
        Keycloak keycloak = getKeycloak(token);
        // 同步到数据库
        keycloakSyncService.syncDeleteClient(clientId);
        keycloak.realm(realm).clients().get(clientId).remove();
    }

    public KeycloakClientDto updateClient(String clientId, ClientRepresentation clientRepresentation, String token) {
        Keycloak keycloak = getKeycloak(token);
        keycloak.realm(realm)
                .clients().get(clientId).update(clientRepresentation);
         // 同步到数据库
        SysKeycloakClient sysKeycloakClient = new SysKeycloakClient();
        sysKeycloakClient.setId(clientRepresentation.getId());
        sysKeycloakClient.setClientId(clientRepresentation.getClientId());
        sysKeycloakClient.setSecret(clientRepresentation.getSecret());
        sysKeycloakClient.setName(clientRepresentation.getName());
        sysKeycloakClient.setDescription(clientRepresentation.getDescription());
        sysKeycloakClient.setType(clientRepresentation.getType());
        sysKeycloakClient.setRootUrl(clientRepresentation.getRootUrl());
        sysKeycloakClient.setAdminUrl(clientRepresentation.getAdminUrl());
        sysKeycloakClient.setBaseUrl(clientRepresentation.getBaseUrl());
        sysKeycloakClient.setSurrogateAuthRequired(clientRepresentation.isSurrogateAuthRequired());
        sysKeycloakClient.setEnabled(clientRepresentation.isEnabled());
        sysKeycloakClient.setAlwaysDisplayInConsole(clientRepresentation.isAlwaysDisplayInConsole());
        sysKeycloakClient.setClientAuthenticatorType(clientRepresentation.getClientAuthenticatorType());
        sysKeycloakClient.setRegistrationAccessToken(clientRepresentation.getRegistrationAccessToken());
        sysKeycloakClient.setRealmName(realm);
        keycloakSyncService.syncClient(sysKeycloakClient);
        return new KeycloakClientDto(
                clientRepresentation.getId(),
                clientRepresentation.getClientId(),
                clientRepresentation.getSecret(),
                clientRepresentation.getName(),
                clientRepresentation.getDescription(),
                clientRepresentation.getType(),
                clientRepresentation.getRootUrl(),
                clientRepresentation.getAdminUrl(),
                clientRepresentation.getBaseUrl(),
                clientRepresentation.isSurrogateAuthRequired(),
                clientRepresentation.isEnabled(),
                clientRepresentation.isAlwaysDisplayInConsole(),
                clientRepresentation.getClientAuthenticatorType(),
                clientRepresentation.getRegistrationAccessToken()
        );
    }

}
