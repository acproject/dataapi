package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysKeycloakClient;
import com.owiseman.dataapi.entity.SysUser;
import com.owiseman.dataapi.entity.Tables;
import com.owiseman.dataapi.util.JooqContextHolder;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class KeycloakClientRepository{
    private final DSLContext dslContext;

    @Autowired
    public KeycloakClientRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    public @NotNull List<SysKeycloakClient> getAllClients() {
        return dslContext.selectFrom(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .fetchInto(SysKeycloakClient.class);

    }

    public Optional<SysKeycloakClient> findById(String id) {
        return  dslContext.selectFrom(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .where(Tables.SYSKEYCLOAKCLIENT.ID.eq(id))
                .fetchOptionalInto(SysKeycloakClient.class);
    }

    public Optional<SysKeycloakClient> findByClientId(String clientId) {
        return  dslContext.selectFrom(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .where(Tables.SYSKEYCLOAKCLIENT.CLIENTID.eq(clientId))
                .fetchOptionalInto(SysKeycloakClient.class);
    }

    public void save(SysKeycloakClient sysKeycloakClients) {
        if (sysKeycloakClients.getId() == null || sysKeycloakClients.getId().isEmpty()) {
            sysKeycloakClients.setId(java.util.UUID.randomUUID().toString());
        }
        dslContext.insertInto(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .set(Tables.SYSKEYCLOAKCLIENT.ID, sysKeycloakClients.getId())
                .set(Tables.SYSKEYCLOAKCLIENT.REALMNAME, sysKeycloakClients.getRealmName())
                .set(Tables.SYSKEYCLOAKCLIENT.CLIENTID, sysKeycloakClients.getClientId())
                .set(Tables.SYSKEYCLOAKCLIENT.SECRET, sysKeycloakClients.getSecret())
                .set(Tables.SYSKEYCLOAKCLIENT.NAME, sysKeycloakClients.getName())
                .set(Tables.SYSKEYCLOAKCLIENT.DESCRIPTION, sysKeycloakClients.getDescription())
                .set(Tables.SYSKEYCLOAKCLIENT.TYPE, sysKeycloakClients.getType())
                .set(Tables.SYSKEYCLOAKCLIENT.ROOTURL, sysKeycloakClients.getRootUrl())
                .set(Tables.SYSKEYCLOAKCLIENT.ADMINURL, sysKeycloakClients.getAdminUrl())
                .set(Tables.SYSKEYCLOAKCLIENT.BASEURL, sysKeycloakClients.getBaseUrl())
                .set(Tables.SYSKEYCLOAKCLIENT.SURROGATEAUTHREQUIRED, sysKeycloakClients.getSurrogateAuthRequired())
                .set(Tables.SYSKEYCLOAKCLIENT.ENABLED, sysKeycloakClients.getEnabled())
                .set(Tables.SYSKEYCLOAKCLIENT.ALWAYSDISPLAYINCONSOLE, sysKeycloakClients.getAlwaysDisplayInConsole())
                .set(Tables.SYSKEYCLOAKCLIENT.CLIENTAUTHENTICATORTYPE, sysKeycloakClients.getClientAuthenticatorType())
                .set(Tables.SYSKEYCLOAKCLIENT.REGISTRATIONACCESSTOKEN, sysKeycloakClients.getRegistrationAccessToke())
                .set(Tables.SYSKEYCLOAKCLIENT.REALMNAME, sysKeycloakClients.getRealmName())
                .execute();
    }

    public void deleteByIdOrClientId(String clientId) {
        dslContext.deleteFrom(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .where(Tables.SYSKEYCLOAKCLIENT.ID.eq(clientId).or(Tables.SYSKEYCLOAKCLIENT.CLIENTID.eq(clientId)))
                .execute();
    }
}
