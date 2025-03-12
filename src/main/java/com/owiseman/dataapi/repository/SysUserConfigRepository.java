package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.util.JooqContextHolder;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.owiseman.dataapi.entity.Tables.SYSUSERCONFIG.*;

@Repository
public class SysUserConfigRepository {

    private final DSLContext dslContext;

    @Autowired
    public SysUserConfigRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    public SysUserConfig save(SysUserConfig config) {
        if (config.getId() == null || config.getId().isEmpty()) {
            config.setId(UUID.randomUUID().toString());
        }
        dslContext.insertInto(TABLE)
                .set(ID, config.getId())
                .set(PROJECTNAME, config.getProjectName())
                .set(PROJECTAPIKEY,config.getProjectApiKey())
                .set(PLATFORM, config.getPlatform())
                .set(KEYCLOAKREALM, config.getKeycloakRealm())
                .set(KEYCLOAKCLIENTID, config.getKeycloakClientId())
                .set(KEYCLOAKCLIENTSECRET,config.getKeycloakClientSecret())
                .set(KEYCLOAKAUTHURL, config.getKeycloakAuthUrl())
                .set(KEYCLOAKTOKENURL,config.getKeycloakTokenUrl())
                .set(APNSKEYPATH,config.getApnsKeyPath())
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
                .set(USERID, config.getUserId())
                .set(ATTRIBUTES, config.getAttributes())
                .set(S3BUCKETNAME, config.getS3BucketName())
                .set(S3REGION, config.getS3Region())
                .set(S3ACCESSKEY, config.getS3AccessKey())
                .set(S3SECRETKEY,config.getS3SecretKey())
                .set(S3ENDPOINT,config.getS3Endpoint())
                .set(OSSENDPOINT, config.getOssEndpoint())
                .set(OSSBUCKETNAME, config.getOssBucketName())
                .set(OSSACCESSKEYID, config.getOssAccessKeyId())
                .set(OSSACCESSKEYSECRET, config.getOssAccessKeySecret())
                .set(OSSREGION, config.getOssRegion())
                .set(SEAWEEDFSMASTERURL, config.getSeaweedFsMasterUrl())
                .set(SEAWEEDFSREPLICATION, config.getSeaweedFsReplication())
                .set(SEAWEEDFSCOLLECTION, config.getSeaweedFsCollection())
                .set(STORAGETYPE, config.getStorageType())
                .set(WECHATAPPID, config.getWechatAppId())
                .set(WECHATAPPSECRET, config.getWechatAppSecret())
                .set(WECHATMCHID, config.getWechatMchId())
                .set(WECHATAPIKEY, config.getWechatApiKey())
                .set(ALIPAYAPPID, config.getAlipayAppId())
                .set(ALIPAYPRIVATEKEY, config.getAlipayPrivateKey())
                .set(ALIPAYPUBLICKEY, config.getAlipayPublicKey())
                .set(ALIPAYGATEWAYURL, config.getAlipayGatewayUrl())
                .set(APLIPAYCERTPATH, config.getAplipayCertPath())
                .set(DESCRIPTION, config.getDescription())
                .set(WECHATPAYCERTPATH, config.getWechatPayCertPath())
                .set(NOTIFYURL, config.getNotifyUrl())
                .set(RETURNURL, config.getReturnUrl())
                .execute();

        return config;
    }

    public SysUserConfig update(SysUserConfig config) {
       dslContext.update(TABLE)
                .set(PLATFORM, config.getPlatform())
                .set(KEYCLOAKREALM, config.getKeycloakRealm())
                .set(KEYCLOAKCLIENTID, config.getKeycloakClientId())
                .set(KEYCLOAKCLIENTSECRET,config.getKeycloakClientSecret())
                .set(KEYCLOAKAUTHURL, config.getKeycloakAuthUrl())
                .set(KEYCLOAKTOKENURL,config.getKeycloakTokenUrl())
                .set(APNSKEYPATH,config.getApnsKeyPath())
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
                .set(S3BUCKETNAME, config.getS3BucketName())
                .set(S3REGION, config.getS3Region())
                .set(S3ACCESSKEY, config.getS3AccessKey())
                .set(S3SECRETKEY,config.getS3SecretKey())
                .set(S3ENDPOINT,config.getS3Endpoint())
                .set(OSSENDPOINT, config.getOssEndpoint())
                .set(OSSBUCKETNAME, config.getOssBucketName())
                .set(OSSACCESSKEYID, config.getOssAccessKeyId())
                .set(OSSACCESSKEYSECRET, config.getOssAccessKeySecret())
                .set(OSSREGION, config.getOssRegion())
                .set(SEAWEEDFSMASTERURL, config.getSeaweedFsMasterUrl())
                .set(SEAWEEDFSREPLICATION, config.getSeaweedFsReplication())
                .set(SEAWEEDFSCOLLECTION, config.getSeaweedFsCollection())
                .set(STORAGETYPE, config.getStorageType())
                .set(WECHATAPPID, config.getWechatAppId())
                .set(WECHATAPPSECRET, config.getWechatAppSecret())
                .set(WECHATMCHID, config.getWechatMchId())
                .set(WECHATAPIKEY, config.getWechatApiKey())
                .set(ALIPAYAPPID, config.getAlipayAppId())
                .set(ALIPAYPRIVATEKEY, config.getAlipayPrivateKey())
                .set(ALIPAYPUBLICKEY, config.getAlipayPublicKey())
                .set(ALIPAYGATEWAYURL, config.getAlipayGatewayUrl())
                .set(APLIPAYCERTPATH, config.getAplipayCertPath())
                .set(DESCRIPTION, config.getDescription())
                .set(WECHATPAYCERTPATH, config.getWechatPayCertPath())
                .set(NOTIFYURL, config.getNotifyUrl())
                .set(RETURNURL, config.getReturnUrl())
               .where(ID.eq(config.getId()))
               .execute();
       return config;
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

    public Optional<SysUserConfig> findByIdAndUserId(String id, String userId) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .and(USERID.eq(userId))
                .fetchOptionalInto(SysUserConfig.class);
    }

    public Optional<SysUserConfig> findByProjectApiKey(String projectApiKey) {

        return dslContext.selectFrom(TABLE)
                .where(PROJECTAPIKEY.eq(projectApiKey))
                .fetchOptionalInto(SysUserConfig.class);
    }

    /**
     * 根据realmName查询所有项目配置,一般来说项目不会太多，所以一般不用分页
     * 如果需要分页可以使用findAllWithPagination
     * @param realmName
     * @return
     */
    public List<SysUserConfig> findAll(String realmName) {
        return dslContext.selectFrom(TABLE).where(KEYCLOAKREALM.eq(realmName))
                .fetchInto(SysUserConfig.class);
    }

    public PageResult<SysUserConfig> findAllWithPagination(int pageNumber, int pageSize, String realmName) {
        List<SysUserConfig> configs = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.condition(KEYCLOAKREALM.eq(realmName)),
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserConfig.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(DSL.condition(KEYCLOAKREALM.eq(realmName)))
                .fetchOne(0, Integer.class);

        return new PageResult<>(configs, pageNumber, pageSize, total);
    }

    public Boolean existsByProjectName(String projectName, String realmName) {
        try {
            return dslContext.selectFrom(TABLE)
                    .where(PROJECTNAME.eq(projectName).and(KEYCLOAKREALM.eq(realmName)))
                    .fetchOne(0, SysUserConfig.class) != null;
        } catch (Exception e) {
            return false;
        }
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