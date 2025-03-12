package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.CreateKeycloakClientDto;
import com.owiseman.dataapi.dto.KeycloakClientDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysKeycloakClient;
import com.owiseman.dataapi.util.JwtParserUtil;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;

import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.RolePolicyRepresentation;
import org.keycloak.representations.idm.authorization.ScopePermissionRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.owiseman.dataapi.config.OAuth2ConstantsExtends.*;


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
    @Autowired
    private KeycloakTokenService keycloakTokenService;
    @Autowired
    private KeycloakAdminUtils keycloakAdminUtils;

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

//    private Keycloak getKeycloak(String token,
//                                 String projectId,
//                                 String clientId,
//                                 String clientSecret,
//                                 String username,
//                                 String password
//    ) {
//        Keycloak keycloak = KeycloakBuilder.builder()
//                .serverUrl(serverUrl)
//                .realm(projectId)
//                .grantType(OAuth2ConstantsExtends.PASSWORD)
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .username(username)
//                .password(password)
//                .authorization(token)
//                .build();
//        return keycloak;
//    }

    /**
     * 用户登录后创建Client
     * @param createKeycloakClientDto
     * @param token
     * @return
     */
    public KeycloakClientDto createClient(CreateKeycloakClientDto createKeycloakClientDto, String token) {

        Keycloak keycloak = keycloakAdminUtils.getKeyCloak(DEFAULT_REALM_NAME, serverUrl, DEFAULT_CLIENT_ID, clientInfo, token);
        RealmResource realmResource = keycloak.realm(createKeycloakClientDto.getRealmName());
        ClientsResource clients = realmResource.clients();

        // 创建Client
        Response response = clients.create(createKeycloakClientDto.getClientRepresentation());

        if (!Objects.equals(201, response.getStatus())) {
            throw new RuntimeException("创建Client失败: " + response.getStatusInfo());
        }

        String clientId = getClientId(response);

        // 获取Client Secret
        ClientResource clientResource = clients.get(clientId);

        CredentialRepresentation secret = clientResource.getSecret();
        createKeycloakClientDto.getClientRepresentation().setSecret(secret.getValue());
        // 同步到数据库
        SysKeycloakClient sysKeycloakClient = new SysKeycloakClient();
        sysKeycloakClient.setId(createKeycloakClientDto.getClientRepresentation().getId());
        sysKeycloakClient.setClientId(createKeycloakClientDto.getClientRepresentation().getClientId());
        sysKeycloakClient.setSecret(createKeycloakClientDto.getClientRepresentation().getSecret());
        sysKeycloakClient.setName(createKeycloakClientDto.getClientRepresentation().getName());
        sysKeycloakClient.setDescription(createKeycloakClientDto.getClientRepresentation().getDescription());
        sysKeycloakClient.setType(createKeycloakClientDto.getClientRepresentation().getType());
        sysKeycloakClient.setRootUrl(createKeycloakClientDto.getClientRepresentation().getRootUrl());
        sysKeycloakClient.setAdminUrl(createKeycloakClientDto.getClientRepresentation().getAdminUrl());
        sysKeycloakClient.setBaseUrl(createKeycloakClientDto.getClientRepresentation().getBaseUrl());
        sysKeycloakClient.setSurrogateAuthRequired(createKeycloakClientDto.getClientRepresentation().isSurrogateAuthRequired());
        sysKeycloakClient.setEnabled(createKeycloakClientDto.getClientRepresentation().isEnabled());
        sysKeycloakClient.setAlwaysDisplayInConsole(createKeycloakClientDto.getClientRepresentation().isAlwaysDisplayInConsole());
        sysKeycloakClient.setClientAuthenticatorType(createKeycloakClientDto.getClientRepresentation().getClientAuthenticatorType());
        sysKeycloakClient.setRegistrationAccessToken(createKeycloakClientDto.getClientRepresentation().getRegistrationAccessToken());
        sysKeycloakClient.setRealmName(createKeycloakClientDto.getRealmName());
        sysKeycloakClient.setForProject(TRUE);
        keycloakSyncService.syncClient(sysKeycloakClient);
        return new KeycloakClientDto(
                createKeycloakClientDto.getClientRepresentation().getId(),
                createKeycloakClientDto.getClientRepresentation().getClientId(),
                createKeycloakClientDto.getClientRepresentation().getSecret(),
                createKeycloakClientDto.getClientRepresentation().getName(),
                createKeycloakClientDto.getClientRepresentation().getDescription(),
                createKeycloakClientDto.getClientRepresentation().getType(),
                createKeycloakClientDto.getClientRepresentation().getRootUrl(),
                createKeycloakClientDto.getClientRepresentation().getAdminUrl(),
                createKeycloakClientDto.getClientRepresentation().getBaseUrl(),
                createKeycloakClientDto.getClientRepresentation().isSurrogateAuthRequired(),
                createKeycloakClientDto.getClientRepresentation().isEnabled(),
                createKeycloakClientDto.getClientRepresentation().isAlwaysDisplayInConsole(),
                createKeycloakClientDto.getClientRepresentation().getClientAuthenticatorType(),
                createKeycloakClientDto.getClientRepresentation().getRegistrationAccessToken(),
                TRUE
        );
    }


    /**
     * 注册专用的方法
     * @param clientRepresentation
     * @param keycloak
     * @param realmName
     * @return
     */
    public KeycloakClientDto createClient(ClientRepresentation clientRepresentation,Keycloak keycloak, String realmName) {
        RealmResource realmResource = keycloak.realm(realmName);
        ClientsResource clients = realmResource.clients();

        // 创建Client
        Response response = clients.create(clientRepresentation);

        if (!Objects.equals(201, response.getStatus())) {
            throw new RuntimeException("创建Client失败: " + response.getStatusInfo());
        }

        String clientId = getClientId(response);
        String clientSecret = UUID.randomUUID().toString().replaceAll("-", "");
        // 获取Client Secret
        ClientResource clientResource = clients.get(clientId);

        var secret = clientResource.generateNewSecret();
        var sc = secret.getValue();
        var authResource = clientResource.authorization();
        // 添加资源
        var resource = new ResourceRepresentation();
        resource.setName("Protected Resource");
        resource.setUris(Set.of("/protected"));
        authResource.resources().create(resource);

        // 创建角色策略
        RolePolicyRepresentation rolePolicy = new RolePolicyRepresentation();
        rolePolicy.setName("Admin Only");
//        rolePolicy.setRoles(Set.of(new RolePolicyRepresentation.RoleDefinition("", true)));
        authResource.policies().role().create(rolePolicy);

        // 创建权限
        ScopePermissionRepresentation permission = new ScopePermissionRepresentation();
        permission.setName("Admin Access Permission");
        permission.setResources(Set.of("Protected Resource"));
        permission.setPolicies(Set.of("Admin Only"));
        authResource.permissions().scope().create(permission);
        // 同步到数据库
        SysKeycloakClient sysKeycloakClient = new SysKeycloakClient();
        sysKeycloakClient.setId(clientId);
        sysKeycloakClient.setClientId(clientRepresentation.getClientId());
        sysKeycloakClient.setSecret(sc);
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
        sysKeycloakClient.setRealmName(realmName);
        sysKeycloakClient.setForProject(FALSE);
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
                clientRepresentation.getRegistrationAccessToken(),
                FALSE
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

    public KeycloakClientDto updateClient(String clientId, ClientRepresentation clientRepresentation, String token, Boolean isForProject) {
        Keycloak keycloak = getKeycloak(token);
        RealmResource realmResource = keycloak.realms().realm(realm);
        String realmName = realmResource.toRepresentation().getRealm();
        keycloak.realm(realmName)
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
        sysKeycloakClient.setRealmName(realmName);
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
                clientRepresentation.getRegistrationAccessToken(),
                isForProject
        );
    }

}
