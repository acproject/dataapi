package com.owiseman.dataapi.repository;


import com.owiseman.dataapi.entity.SysKeycloakRealm;
import com.owiseman.dataapi.entity.Tables;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KeycloakRealmRepository {


    private final DSLContext dslContext;

    @Autowired
    public KeycloakRealmRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public @NotNull SysKeycloakRealm findById(String id) {
        return (SysKeycloakRealm) dslContext.selectFrom(Tables.SYSKEYCLOAKREALM.TABLE)
                .where(Tables.SYSKEYCLOAKREALM.ID.eq(id))
                .fetchOneInto(SysKeycloakRealm.class);
    }

    public void save(SysKeycloakRealm sysKeycloakRealms) {

        dslContext.insertInto(Tables.SYSKEYCLOAKREALM.TABLE)
                .set(Tables.SYSKEYCLOAKREALM.ID, sysKeycloakRealms.getId())
                .set(Tables.SYSKEYCLOAKREALM.REALM, sysKeycloakRealms.getRealm())
                .set(Tables.SYSKEYCLOAKREALM.ENABLED, sysKeycloakRealms.getEnabled())
                .set(Tables.SYSKEYCLOAKREALM.ACCESSTOKENLIFESPAN, sysKeycloakRealms.getAccessTokenLifespan())
                .execute();
    }

    public void update(SysKeycloakRealm sysKeycloakRealms) {
        dslContext.update(Tables.SYSKEYCLOAKREALM.TABLE)
                .set(Tables.SYSKEYCLOAKREALM.REALM, sysKeycloakRealms.getRealm())
                .set(Tables.SYSKEYCLOAKREALM.ENABLED, sysKeycloakRealms.getEnabled())
                .set(Tables.SYSKEYCLOAKREALM.ACCESSTOKENLIFESPAN, sysKeycloakRealms.getAccessTokenLifespan())
                .where(Tables.SYSKEYCLOAKREALM.ID.eq(sysKeycloakRealms.getId()))
                .execute();
    }

    public void deleteByIdOrName(String id) {
        dslContext.deleteFrom(Tables.SYSKEYCLOAKREALM.TABLE)
                .where(Tables.SYSKEYCLOAKREALM.ID.eq(id).or(Tables.SYSKEYCLOAKREALM.REALM.eq(id)))
                .execute();
    }

}
