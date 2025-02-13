package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.KeycloakRealmDto;
import com.owiseman.dataapi.entity.SysKeycloakRealm;
import jakarta.ws.rs.NotFoundException;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KeycloakRealmService {
    @Value("${keycloak.master}")
    private String masterRealm;

    @Value("${keycloak.master-password}")
    private String masterPassword;

    @Value("${keycloak.urls.auth}")
    private String serverUrl;
    @Value("${keycloak.master-client}")
    private String clientId;

    private KeycloakSyncService keycloakSyncService;

    public KeycloakRealmService() {
        keycloakSyncService = new KeycloakSyncService();
    }

    private Keycloak getKeycloak(String token) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(masterRealm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .username(OAuth2ConstantsExtends.MASTER_ADMIN)
                .password(masterPassword)
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
        sysKeycloakRealm.setRealm(realmName);
        sysKeycloakRealm.setEnabled(OAuth2ConstantsExtends.TRUE);
        keycloakSyncService.syncRealm(sysKeycloakRealm);
        return record;
    }

    public void deleteRealm(String realmName, String token) {
        Keycloak keycloak = getKeycloak(token);
        try {
            // 同步到数据库
            keycloakSyncService.syncDeleteRealm(realmName);
            keycloak.realm(realmName).remove();
        } catch (NotFoundException e) {
            throw new RuntimeException("Realm not exist: " + realmName);
        }
    }

    public void updateRealm(String realmName, SysKeycloakRealm sysKeycloakRealms, String token) {
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
            keycloak.realm(realmName).update(realm);
        } catch (NotFoundException e) {
            throw new RuntimeException("Realm not exist: " + realmName);
        }
    }



}
