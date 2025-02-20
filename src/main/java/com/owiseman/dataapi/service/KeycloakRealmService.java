package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.KeycloakRealmDto;
import com.owiseman.dataapi.entity.SysKeycloakRealm;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class KeycloakRealmService {
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-info}")
    private String password;

    @Value("${keycloak.urls.auth}")
    private String serverUrl;
    @Value("${keycloak.resource}")
    private String clientId;
    private final Integer ACCESS_TOKEN_LIFESPAN = 10*60; // 10分钟

    @Autowired
    private KeycloakSyncService keycloakSyncService;

    private Keycloak getKeycloak(String token) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2ConstantsExtends.PASSWORD)
                .clientId(clientId)
                .username(OAuth2ConstantsExtends.ADMIN)
                .password(password)
                .authorization(token)
                .build();
        return keycloak;
    }

    public KeycloakRealmDto createRealm(String realmName, String token) {
        Keycloak keycloak = getKeycloak(token);
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(realmName);
        realm.setEnabled(OAuth2ConstantsExtends.TRUE);
        KeycloakRealmDto record = new KeycloakRealmDto(realmName, OAuth2ConstantsExtends.TRUE);
        keycloak.realms().create(realm);
        // 同步到数据库
        SysKeycloakRealm sysKeycloakRealm = new SysKeycloakRealm();
//        sysKeycloakRealm.setId(realmResource.getId());
        sysKeycloakRealm.setId(UUID.randomUUID().toString());
        sysKeycloakRealm.setRealm(realmName);
        sysKeycloakRealm.setDisplayName(realmName);
        sysKeycloakRealm.setEnabled(OAuth2ConstantsExtends.TRUE);
        sysKeycloakRealm.setAccessCodeLifespan(ACCESS_TOKEN_LIFESPAN);
        keycloakSyncService.syncRealm(sysKeycloakRealm);
        return record;
    }

    public void deleteRealmByName(String realmName, String token) {
        Keycloak keycloak = getKeycloak(token);
        try {
            // 同步到数据库
            keycloakSyncService.syncDeleteRealm(realmName);
            keycloak.realm(realmName).remove();
        } catch (NotFoundException e) {
            throw new RuntimeException("Realm not exist: " + realmName);
        }
    }

    public void updateRealm(KeycloakRealmDto keycloakRealmDto, SysKeycloakRealm sysKeycloakRealms, String token) {
        Keycloak keycloak = getKeycloak(token);
        try {
           RealmRepresentation realm =  new RealmRepresentation();
           realm.setEnabled(sysKeycloakRealms.getEnabled());
           realm.setDisplayName(sysKeycloakRealms.getDisplayName());
           realm.setDisplayNameHtml(sysKeycloakRealms.getDisplayNameHtml());
           realm.setNotBefore(sysKeycloakRealms.getNotBefore());
           realm.setDefaultSignatureAlgorithm(sysKeycloakRealms.getDefaultSignatureAlgorithm());
           realm.setRevokeRefreshToken(sysKeycloakRealms.getRevokeRefreshToken());
           realm.setRefreshTokenMaxReuse(sysKeycloakRealms.getRefreshTokenMaxReuse());
           realm.setAccessTokenLifespan(sysKeycloakRealms.getAccessTokenLifespan());
           realm.setAccessTokenLifespanForImplicitFlow(sysKeycloakRealms.getAccessTokenLifespanForImplicitFlow());
           realm.setSsoSessionIdleTimeout(sysKeycloakRealms.getSsoSessionIdleTimeout());
           realm.setSsoSessionMaxLifespan(sysKeycloakRealms.getSsoSessionMaxLifespan());
           realm.setSsoSessionIdleTimeoutRememberMe(sysKeycloakRealms.getSsoSessionIdleTimeoutRememberMe());
           realm.setSsoSessionMaxLifespanRememberMe(sysKeycloakRealms.getSsoSessionMaxLifespanRememberMe());
           realm.setOfflineSessionIdleTimeout(sysKeycloakRealms.getOfflineSessionIdleTimeout());
           realm.setOfflineSessionMaxLifespanEnabled(sysKeycloakRealms.getOfflineSessionMaxLifespanEnabled());
           realm.setOfflineSessionMaxLifespan(sysKeycloakRealms.getOfflineSessionMaxLifespan());
           realm.setClientSessionIdleTimeout(sysKeycloakRealms.getClientSessionIdleTimeout());
           realm.setClientSessionMaxLifespan(sysKeycloakRealms.getClientSessionMaxLifespan());
           realm.setClientOfflineSessionIdleTimeout(sysKeycloakRealms.getClientOfflineSessionIdleTimeout());
           realm.setClientOfflineSessionMaxLifespan(sysKeycloakRealms.getClientOfflineSessionMaxLifespan());
           realm.setAccessCodeLifespan(sysKeycloakRealms.getAccessCodeLifespan());
           realm.setAccessCodeLifespanUserAction(sysKeycloakRealms.getAccessCodeLifespanUserAction());
           realm.setAccessCodeLifespanLogin(sysKeycloakRealms.getAccessCodeLifespanLogin());
           realm.setActionTokenGeneratedByAdminLifespan(sysKeycloakRealms.getActionTokenGeneratedByAdminLifespan());
           realm.setActionTokenGeneratedByUserLifespan(sysKeycloakRealms.getActionTokenGeneratedByUserLifespan());
            // 同步到数据库
            keycloakSyncService.syncRealm(sysKeycloakRealms);
            keycloak.realm(keycloakRealmDto.getName()).update(realm);
        } catch (NotFoundException e) {
            throw new RuntimeException("Realm not exist: " + keycloakRealmDto.getName());
        }
    }



}
