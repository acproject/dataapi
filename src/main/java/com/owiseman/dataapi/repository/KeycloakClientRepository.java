package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysKeycloakClient;
import com.owiseman.dataapi.entity.Tables;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeycloakClientRepository{
    @Autowired
    private  DSLContext dslContext;

    @Autowired
    public KeycloakClientRepository() {

    }

    public @NotNull List<SysKeycloakClient> getAllClients() {
        return dslContext.selectFrom(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .fetchInto(SysKeycloakClient.class);

    }

    public SysKeycloakClient findById(String id) {
        return (SysKeycloakClient) dslContext.selectFrom(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .where(Tables.SYSKEYCLOAKCLIENT.ID.eq(id))
                .fetchOneInto(SysKeycloakClient.class);
    }

    public void save(SysKeycloakClient sysKeycloakClients) {
        dslContext.insertInto(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .set(Tables.SYSKEYCLOAKCLIENT.ID, sysKeycloakClients.getId())
                .set(Tables.SYSKEYCLOAKCLIENT.REALMNAME, sysKeycloakClients.getRealmName())
                .set(Tables.SYSKEYCLOAKCLIENT.CLIENTID, sysKeycloakClients.getClientId())
                .execute();
    }

    public void deleteByIdOrClientId(String clientId) {
        dslContext.deleteFrom(Tables.SYSKEYCLOAKCLIENT.TABLE)
                .where(Tables.SYSKEYCLOAKCLIENT.ID.eq(clientId).or(Tables.SYSKEYCLOAKCLIENT.CLIENTID.eq(clientId)))
                .execute();
    }
}
