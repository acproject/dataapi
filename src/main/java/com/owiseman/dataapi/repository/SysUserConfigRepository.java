package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSUSERCONFIG.*;

@Repository
public class SysUserConfigRepository {
    @Autowired
    private DSLContext dslContext;

    @Autowired
    public SysUserConfigRepository() {
    }

    public SysUserConfig save(SysUserConfig config) {
        dslContext.insertInto(TABLE)
                .set(ID, config.getId())
                .set(USERID, config.getUserId())
                .set(KEYCLOAKREALM, config.getKeycloakRealm())
                .set(KEYCLOAKCLIENTID, config.getKeycloakClientId())
                .set(KEYCLOAKCLIENTSECRET, config.getKeycloakClientSecret())
                .set(KEYCLOAKAUTHURL, config.getKeycloakAuthUrl())
                .set(KEYCLOAKTOKENURL, config.getKeycloakTokenUrl())
                .set(APNSKEYPATH, config.getApnsKeyPath())
                .set(APNSTEAMID, config.getApnsTeamId())
                .set(APNSKEYID, config.getApnsKeyId())
                .set(APNSBUNDLEID, config.getApnsBundleId())
                .set(APNSPRODUCTION, config.getApnsProduction())
                .set(FIREBASEPROJECTID, config.getFirebaseProjectId())
                .set(FIREBASEPRIVATEKEY, config.getFirebasePrivateKey())
                .set(FIREBASECLIENTEMAIL, config.getFirebaseClientEmail())
                .set(FIREBASECLIENTID, config.getFirebaseClientId())
                .set(FIREBASESERVICEACCOUNTPATH, config.getFirebaseServiceAccountPath())
                .set(DATABASETABLENAMEPREFIX, config.getDatabaseTableNamePrefix())
                .set(ATTRIBUTES, config.getAttributes())
                .execute();
        return config;
    }

    public void update(SysUserConfig config) {
        dslContext.update(TABLE)
                .set(USERID, config.getUserId())
                .set(KEYCLOAKREALM, config.getKeycloakRealm())
                .set(KEYCLOAKCLIENTID, config.getKeycloakClientId())
                .set(KEYCLOAKCLIENTSECRET, config.getKeycloakClientSecret())
                .set(KEYCLOAKAUTHURL, config.getKeycloakAuthUrl())
                .set(KEYCLOAKTOKENURL, config.getKeycloakTokenUrl())
                .set(APNSKEYPATH, config.getApnsKeyPath())
                .set(APNSTEAMID, config.getApnsTeamId())
                .set(APNSKEYID, config.getApnsKeyId())
                .set(APNSBUNDLEID, config.getApnsBundleId())
                .set(APNSPRODUCTION, config.getApnsProduction())
                .set(FIREBASEPROJECTID, config.getFirebaseProjectId())
                .set(FIREBASEPRIVATEKEY, config.getFirebasePrivateKey())
                .set(FIREBASECLIENTEMAIL, config.getFirebaseClientEmail())
                .set(FIREBASECLIENTID, config.getFirebaseClientId())
                .set(FIREBASESERVICEACCOUNTPATH, config.getFirebaseServiceAccountPath())
                .set(DATABASETABLENAMEPREFIX, config.getDatabaseTableNamePrefix())
                .set(ATTRIBUTES, config.getAttributes())
                .where(ID.eq(config.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysUserConfig> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysUserConfig.class);
    }

    public Optional<SysUserConfig> findByUserId(String userId) {
        return dslContext.selectFrom(TABLE)
                .where(USERID.eq(userId))
                .fetchOptionalInto(SysUserConfig.class);
    }

    public List<SysUserConfig> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysUserConfig.class);
    }

    public PageResult<SysUserConfig> findAllWithPagination(int pageNumber, int pageSize) {
        List<SysUserConfig> configs = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.noCondition(),
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserConfig.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(DSL.noCondition())
                .fetchOne(0, Integer.class);

        return new PageResult<>(configs, pageNumber, pageSize, total);
    }

    public PageResult<SysUserConfig> findByConditionWithPagination(Condition condition, int pageNumber, int pageSize) {
        List<SysUserConfig> configs = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserConfig.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(configs, pageNumber, pageSize, total);
    }

    public void deleteByUserId(String userId) {
        dslContext.deleteFrom(TABLE)
                .where(USERID.eq(userId))
                .execute();
    }
}