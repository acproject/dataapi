package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.dto.Platform;
import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

/**
 * 该表其实是项目的配置信息表，也是用于创建project的
 */
@Entity
@Table(name = "sys_user_config")
public class SysUserConfig {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    @Column(name = "id")
    private String id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    private Platform platform;
    // keycloak的基础配置
    @Column(name = "keycloak_realm")
    private String keycloakRealm;
    @Column(name = "keycloak_client_id")
    private String keycloakClientId;
    @Column(name = "keycloak_client_secret")
    private String keycloakClientSecret;
    @Column(name = "keycloak_auth_url")
    private String keycloakAuthUrl;
    @Column(name = "keycloak_token_url")
    private String keycloakTokenUrl;
    // apns的基础配置
    @Column(name = "apns_key_path")
    private String apnsKeyPath;
    @Column(name = "apns_team_id")
    private String apnsTeamId;
    @Column(name = "apns_key_id")
    private String apnsKeyId;
    @Column(name = "apns_bundle_id")
    private String apnsBundleId;
    @Column(name = "apns_production")
    private Boolean apnsProduction;

    // firebase的基础配置
    @Column(name = "firebase_project_id")
    private String firebaseProjectId;
    @Column(name = "firebase_private_key")
    private String firebasePrivateKey;
    @Column(name = "firebase_client_email")
    private String firebaseClientEmail;
    @Column(name = "firebase_client_id")
    private String firebaseClientId;
    @Column(name = "firebase_service_account_path")
    private String firebaseServiceAccountPath;
    // 规则为realm名+ "_" + tableName +"_" = "realm_table_"
    @Column(name = "database_table_name_prefix")
    private String databaseTableNamePrefix;

    @Column(name = "user_id",nullable = false)
    private String userId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb", nullable = true)
    private Map<String, List<String>> attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeycloakRealm() {
        return keycloakRealm;
    }

    public void setKeycloakRealm(String keycloakRealm) {
        this.keycloakRealm = keycloakRealm;
    }

    public String getKeycloakClientId() {
        return keycloakClientId;
    }

    public void setKeycloakClientId(String keycloakClientId) {
        this.keycloakClientId = keycloakClientId;
    }

    public String getKeycloakClientSecret() {
        return keycloakClientSecret;
    }

    public void setKeycloakClientSecret(String keycloakClientSecret) {
        this.keycloakClientSecret = keycloakClientSecret;
    }

    public String getKeycloakAuthUrl() {
        return keycloakAuthUrl;
    }

    public void setKeycloakAuthUrl(String keycloakAuthUrl) {
        this.keycloakAuthUrl = keycloakAuthUrl;
    }

    public String getKeycloakTokenUrl() {
        return keycloakTokenUrl;
    }

    public void setKeycloakTokenUrl(String keycloakTokenUrl) {
        this.keycloakTokenUrl = keycloakTokenUrl;
    }

    public String getApnsKeyPath() {
        return apnsKeyPath;
    }

    public void setApnsKeyPath(String apnsKeyPath) {
        this.apnsKeyPath = apnsKeyPath;
    }

    public String getApnsTeamId() {
        return apnsTeamId;
    }

    public void setApnsTeamId(String apnsTeamId) {
        this.apnsTeamId = apnsTeamId;
    }

    public String getApnsKeyId() {
        return apnsKeyId;
    }

    public void setApnsKeyId(String apnsKeyId) {
        this.apnsKeyId = apnsKeyId;
    }

    public String getApnsBundleId() {
        return apnsBundleId;
    }

    public void setApnsBundleId(String apnsBundleId) {
        this.apnsBundleId = apnsBundleId;
    }

    public Boolean getApnsProduction() {
        return apnsProduction;
    }

    public void setApnsProduction(Boolean apnsProduction) {
        this.apnsProduction = apnsProduction;
    }

    public String getFirebaseProjectId() {
        return firebaseProjectId;
    }

    public void setFirebaseProjectId(String firebaseProjectId) {
        this.firebaseProjectId = firebaseProjectId;
    }

    public String getFirebasePrivateKey() {
        return firebasePrivateKey;
    }

    public void setFirebasePrivateKey(String firebasePrivateKey) {
        this.firebasePrivateKey = firebasePrivateKey;
    }

    public String getFirebaseClientEmail() {
        return firebaseClientEmail;
    }

    public void setFirebaseClientEmail(String firebaseClientEmail) {
        this.firebaseClientEmail = firebaseClientEmail;
    }

    public String getFirebaseClientId() {
        return firebaseClientId;
    }

    public void setFirebaseClientId(String firebaseClientId) {
        this.firebaseClientId = firebaseClientId;
    }

    public String getFirebaseServiceAccountPath() {
        return firebaseServiceAccountPath;
    }

    public void setFirebaseServiceAccountPath(String firebaseServiceAccountPath) {
        this.firebaseServiceAccountPath = firebaseServiceAccountPath;
    }

    public String getDatabaseTableNamePrefix() {
        return databaseTableNamePrefix;
    }

    public void setDatabaseTableNamePrefix(String databaseTableNamePrefix) {
        this.databaseTableNamePrefix = databaseTableNamePrefix;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public SysUserConfig() {
    }


    // AWS S3 配置
    @Column(name = "s3_bucket_name")
    private String s3BucketName;
    @Column(name = "s3_region")
    private String s3Region;
    @Column(name = "s3_access_key")
    private String s3AccessKey;
    @Column(name = "s3_secret_key")
    private String s3SecretKey;
    @Column(name = "s3_endpoint")
    private String s3Endpoint;

    // 阿里云 OSS 配置
    @Column(name = "oss_endpoint")
    private String ossEndpoint;
    @Column(name = "oss_bucket_name")
    private String ossBucketName;
    @Column(name = "oss_access_key_id")
    private String ossAccessKeyId;
    @Column(name = "oss_access_key_secret")
    private String ossAccessKeySecret;
    @Column(name = "oss_region")
    private String ossRegion;

    // SeaweedFS 配置
    @Column(name = "seaweedfs_master_url")
    private String seaweedFsMasterUrl;
    @Column(name = "seaweedfs_replication")
    private Integer seaweedFsReplication;
    @Column(name = "seaweedfs_collection")
    private String seaweedFsCollection;

    // 存储类型（seaweedfs/s3/aliyun）
    @Column(name = "storage_type")
    private String storageType;

    // AWS S3 的 getter/setter
    public String getS3BucketName() {
        return s3BucketName;
    }

    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    public String getS3Region() {
        return s3Region;
    }

    public void setS3Region(String s3Region) {
        this.s3Region = s3Region;
    }

    public String getS3AccessKey() {
        return s3AccessKey;
    }

    public void setS3AccessKey(String s3AccessKey) {
        this.s3AccessKey = s3AccessKey;
    }

    public String getS3SecretKey() {
        return s3SecretKey;
    }

    public void setS3SecretKey(String s3SecretKey) {
        this.s3SecretKey = s3SecretKey;
    }

    public String getS3Endpoint() {
        return s3Endpoint;
    }

    public void setS3Endpoint(String s3Endpoint) {
        this.s3Endpoint = s3Endpoint;
    }

    // 阿里云 OSS 的 getter/setter
    public String getOssEndpoint() {
        return ossEndpoint;
    }

    public void setOssEndpoint(String ossEndpoint) {
        this.ossEndpoint = ossEndpoint;
    }

    public String getOssBucketName() {
        return ossBucketName;
    }

    public void setOssBucketName(String ossBucketName) {
        this.ossBucketName = ossBucketName;
    }

    public String getOssAccessKeyId() {
        return ossAccessKeyId;
    }

    public void setOssAccessKeyId(String ossAccessKeyId) {
        this.ossAccessKeyId = ossAccessKeyId;
    }

    public String getOssAccessKeySecret() {
        return ossAccessKeySecret;
    }

    public void setOssAccessKeySecret(String ossAccessKeySecret) {
        this.ossAccessKeySecret = ossAccessKeySecret;
    }

    public String getOssRegion() {
        return ossRegion;
    }

    public void setOssRegion(String ossRegion) {
        this.ossRegion = ossRegion;
    }

    // SeaweedFS 的 getter/setter
    public String getSeaweedFsMasterUrl() {
        return seaweedFsMasterUrl;
    }

    public void setSeaweedFsMasterUrl(String seaweedFsMasterUrl) {
        this.seaweedFsMasterUrl = seaweedFsMasterUrl;
    }

    public Integer getSeaweedFsReplication() {
        return seaweedFsReplication;
    }

    public void setSeaweedFsReplication(Integer seaweedFsReplication) {
        this.seaweedFsReplication = seaweedFsReplication;
    }

    public String getSeaweedFsCollection() {
        return seaweedFsCollection;
    }

    public void setSeaweedFsCollection(String seaweedFsCollection) {
        this.seaweedFsCollection = seaweedFsCollection;
    }

    // 存储类型的 getter/setter
    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    // 微信小程序配置
    @Column(name="wechat_app_id")
    private String wechatAppId;
    @Column(name="wechat_app_secret")
    private String wechatAppSecret;
    @Column(name="wechat_mch_id")
    private String wechatMchId;
    @Column(name="wechat_api_key")
    private String wechatApiKey;

    // 支付宝小程序配置
    @Column(name="alipay_app_id")
    private String alipayAppId;
    @Column(name="alipay_private_key")
    private String alipayPrivateKey;
    @Column(name="alipay_public_key")
    private String alipayPublicKey;
    @Column(name="alipay_gateway_url")
    private String alipayGatewayUrl;

    // 微信小程序的 getter/setter
    public String getWechatAppId() {
        return wechatAppId;
    }

    public void setWechatAppId(String wechatAppId) {
        this.wechatAppId = wechatAppId;
    }

    public String getWechatAppSecret() {
        return wechatAppSecret;
    }

    public void setWechatAppSecret(String wechatAppSecret) {
        this.wechatAppSecret = wechatAppSecret;
    }

    public String getWechatMchId() {
        return wechatMchId;
    }

    public void setWechatMchId(String wechatMchId) {
        this.wechatMchId = wechatMchId;
    }

    public String getWechatApiKey() {
        return wechatApiKey;
    }

    public void setWechatApiKey(String wechatApiKey) {
        this.wechatApiKey = wechatApiKey;
    }

    // 支付宝小程序的 getter/setter
    public String getAlipayAppId() {
        return alipayAppId;
    }

    public void setAlipayAppId(String alipayAppId) {
        this.alipayAppId = alipayAppId;
    }

    public String getAlipayPrivateKey() {
        return alipayPrivateKey;
    }

    public void setAlipayPrivateKey(String alipayPrivateKey) {
        this.alipayPrivateKey = alipayPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getAlipayGatewayUrl() {
        return alipayGatewayUrl;
    }

    public void setAlipayGatewayUrl(String alipayGatewayUrl) {
        this.alipayGatewayUrl = alipayGatewayUrl;
    }

    public SysUserConfig(String id, String projectName, Platform platform, String keycloakRealm, String keycloakClientId, String keycloakClientSecret, String keycloakAuthUrl,
                         String keycloakTokenUrl, String apnsKeyPath, String apnsTeamId, String apnsKeyId, String apnsBundleId, Boolean apnsProduction, String firebaseProjectId,
                         String firebasePrivateKey, String firebaseClientEmail, String firebaseClientId, String firebaseServiceAccountPath, String databaseTableNamePrefix,
                         String userId, Map<String, List<String>> attributes, String s3BucketName, String s3Region, String s3AccessKey, String s3SecretKey, String s3Endpoint,
                         String ossEndpoint, String ossBucketName, String ossAccessKeyId, String ossAccessKeySecret, String ossRegion, String seaweedFsMasterUrl, Integer seaweedFsReplication,
                         String seaweedFsCollection, String storageType, String wechatAppId, String wechatAppSecret, String wechatMchId, String wechatApiKey, String wechatPayCertPath, String alipayAppId,
                         String alipayPrivateKey, String alipayPublicKey, String alipayGatewayUrl, String aplipayCertPath, String notifyUrl, String returnUrl, String description) {
        this.id = id;
        this.projectName = projectName;
        this.platform = platform;
        this.keycloakRealm = keycloakRealm;
        this.keycloakClientId = keycloakClientId;
        this.keycloakClientSecret = keycloakClientSecret;
        this.keycloakAuthUrl = keycloakAuthUrl;
        this.keycloakTokenUrl = keycloakTokenUrl;
        this.apnsKeyPath = apnsKeyPath;
        this.apnsTeamId = apnsTeamId;
        this.apnsKeyId = apnsKeyId;
        this.apnsBundleId = apnsBundleId;
        this.apnsProduction = apnsProduction;
        this.firebaseProjectId = firebaseProjectId;
        this.firebasePrivateKey = firebasePrivateKey;
        this.firebaseClientEmail = firebaseClientEmail;
        this.firebaseClientId = firebaseClientId;
        this.firebaseServiceAccountPath = firebaseServiceAccountPath;
        this.databaseTableNamePrefix = databaseTableNamePrefix;
        this.userId = userId;
        this.attributes = attributes;
        this.s3BucketName = s3BucketName;
        this.s3Region = s3Region;
        this.s3AccessKey = s3AccessKey;
        this.s3SecretKey = s3SecretKey;
        this.s3Endpoint = s3Endpoint;
        this.ossEndpoint = ossEndpoint;
        this.ossBucketName = ossBucketName;
        this.ossAccessKeyId = ossAccessKeyId;
        this.ossAccessKeySecret = ossAccessKeySecret;
        this.ossRegion = ossRegion;
        this.seaweedFsMasterUrl = seaweedFsMasterUrl;
        this.seaweedFsReplication = seaweedFsReplication;
        this.seaweedFsCollection = seaweedFsCollection;
        this.storageType = storageType;
        this.wechatAppId = wechatAppId;
        this.wechatAppSecret = wechatAppSecret;
        this.wechatMchId = wechatMchId;
        this.wechatApiKey = wechatApiKey;
        this.wechatPayCertPath = wechatPayCertPath;
        this.alipayAppId = alipayAppId;
        this.alipayPrivateKey = alipayPrivateKey;
        this.alipayPublicKey = alipayPublicKey;
        this.alipayGatewayUrl = alipayGatewayUrl;
        this.aplipayCertPath = aplipayCertPath;
        this.notifyUrl = notifyUrl;
        this.returnUrl = returnUrl;
        this.description = description;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String wechatPayCertPath;

    public String getWechatPayCertPath() {
        return wechatPayCertPath;
    }

    public void setWechatPayCertPath(String wechatPayCertPath) {
        this.wechatPayCertPath = wechatPayCertPath;
    }

    private String notifyUrl;
    private String returnUrl;

    private String aplipayCertPath;

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getAplipayCertPath() {
        return aplipayCertPath;
    }

    public void setAplipayCertPath(String aplipayCertPath) {
        this.aplipayCertPath = aplipayCertPath;
    }

    public static class Builder {
        private String id;
        private String projectName;
        private Platform platform;
        private String keycloakRealm;
        private String keycloakClientId;
        private String keycloakClientSecret;
        private String keycloakAuthUrl;
        private String keycloakTokenUrl;
        private String apnsKeyPath;
        private String apnsTeamId;
        private String apnsKeyId;
        private String apnsBundleId;
        private Boolean apnsProduction;
        private String firebaseProjectId;
        private String firebasePrivateKey;
        private String firebaseClientEmail;
        private String firebaseClientId;
        private String firebaseServiceAccountPath;
        private String databaseTableNamePrefix;
        private String userId;
        private Map<String, List<String>> attributes;
        private String s3BucketName;
        private String s3Region;
        private String s3AccessKey;
        private String s3SecretKey;
        private String s3Endpoint;
        private String ossEndpoint;
        private String ossBucketName;
        private String ossAccessKeyId;
        private String ossAccessKeySecret;
        private String ossRegion;
        private String seaweedFsMasterUrl;
        private Integer seaweedFsReplication;
        private String seaweedFsCollection;
        private String storageType;
        private String wechatAppId;
        private String wechatAppSecret;
        private String wechatMchId;
        private String wechatApiKey;
        private String wechatPayCertPath;
        private String alipayAppId;
        private String alipayPrivateKey;
        private String alipayPublicKey;
        private String alipayGatewayUrl;
        private String aplipayCertPath;
        private String notifyUrl;
        private String returnUrl;
        private String description;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder platform(Platform platform) {
            this.platform = platform;
            return this;
        }

        public Builder keycloakRealm(String keycloakRealm) {
            this.keycloakRealm = keycloakRealm;
            return this;
        }

        public Builder keycloakClientId(String keycloakClientId) {
            this.keycloakClientId = keycloakClientId;
            return this;
        }

        public Builder keycloakClientSecret(String keycloakClientSecret) {
            this.keycloakClientSecret = keycloakClientSecret;
            return this;
        }

        public Builder keycloakAuthUrl(String keycloakAuthUrl) {
            this.keycloakAuthUrl = keycloakAuthUrl;
            return this;
        }

        public Builder keycloakTokenUrl(String keycloakTokenUrl) {
            this.keycloakTokenUrl = keycloakTokenUrl;
            return this;
        }

        public Builder apnsKeyPath(String apnsKeyPath) {
            this.apnsKeyPath = apnsKeyPath;
            return this;
        }

        public Builder apnsTeamId(String apnsTeamId) {
            this.apnsTeamId = apnsTeamId;
            return this;
        }

        public Builder apnsKeyId(String apnsKeyId) {
            this.apnsKeyId = apnsKeyId;
            return this;
        }

        public Builder apnsBundleId(String apnsBundleId) {
            this.apnsBundleId = apnsBundleId;
            return this;
        }

        public Builder apnsProduction(Boolean apnsProduction) {
            this.apnsProduction = apnsProduction;
            return this;
        }

        public Builder firebaseProjectId(String firebaseProjectId) {
            this.firebaseProjectId = firebaseProjectId;
            return this;
        }

        public Builder firebasePrivateKey(String firebasePrivateKey) {
            this.firebasePrivateKey = firebasePrivateKey;
            return this;
        }

        public Builder firebaseClientEmail(String firebaseClientEmail) {
            this.firebaseClientEmail = firebaseClientEmail;
            return this;
        }

        public Builder firebaseClientId(String firebaseClientId) {
            this.firebaseClientId = firebaseClientId;
            return this;
        }

        public Builder firebaseServiceAccountPath(String firebaseServiceAccountPath) {
            this.firebaseServiceAccountPath = firebaseServiceAccountPath;
            return this;
        }

        public Builder databaseTableNamePrefix(String databaseTableNamePrefix) {
            this.databaseTableNamePrefix = databaseTableNamePrefix;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder attributes(Map<String, List<String>> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder s3BucketName(String s3BucketName) {
            this.s3BucketName = s3BucketName;
            return this;
        }

        public Builder s3Region(String s3Region) {
            this.s3Region = s3Region;
            return this;
        }

        public Builder s3AccessKey(String s3AccessKey) {
            this.s3AccessKey = s3AccessKey;
            return this;
        }

        public Builder s3SecretKey(String s3SecretKey) {
            this.s3SecretKey = s3SecretKey;
            return this;
        }

        public Builder s3Endpoint(String s3Endpoint) {
            this.s3Endpoint = s3Endpoint;
            return this;
        }

        public Builder ossEndpoint(String ossEndpoint) {
            this.ossEndpoint = ossEndpoint;
            return this;
        }

        public Builder ossBucketName(String ossBucketName) {
            this.ossBucketName = ossBucketName;
            return this;
        }

        public Builder ossAccessKeyId(String ossAccessKeyId) {
            this.ossAccessKeyId = ossAccessKeyId;
            return this;
        }

        public Builder ossAccessKeySecret(String ossAccessKeySecret) {
            this.ossAccessKeySecret = ossAccessKeySecret;
            return this;
        }

        public Builder ossRegion(String ossRegion) {
            this.ossRegion = ossRegion;
            return this;
        }

        public Builder seaweedFsMasterUrl(String seaweedFsMasterUrl) {
            this.seaweedFsMasterUrl = seaweedFsMasterUrl;
            return this;
        }

        public Builder seaweedFsReplication(Integer seaweedFsReplication) {
            this.seaweedFsReplication = seaweedFsReplication;
            return this;
        }

        public Builder seaweedFsCollection(String seaweedFsCollection) {
            this.seaweedFsCollection = seaweedFsCollection;
            return this;
        }

        public Builder storageType(String storageType) {
            this.storageType = storageType;
            return this;
        }

        public Builder wechatAppId(String wechatAppId) {
            this.wechatAppId = wechatAppId;
            return this;
        }

        public Builder wechatAppSecret(String wechatAppSecret) {
            this.wechatAppSecret = wechatAppSecret;
            return this;
        }

        public Builder wechatMchId(String wechatMchId) {
            this.wechatMchId = wechatMchId;
            return this;
        }

        public Builder wechatApiKey(String wechatApiKey) {
            this.wechatApiKey = wechatApiKey;
            return this;
        }

        public Builder wechatPayCertPath(String wechatPayCertPath) {
            this.wechatPayCertPath = wechatPayCertPath;
            return this;
        }

        public Builder alipayAppId(String alipayAppId) {
            this.alipayAppId = alipayAppId;
            return this;
        }

        public Builder alipayPrivateKey(String alipayPrivateKey) {
            this.alipayPrivateKey = alipayPrivateKey;
            return this;
        }

        public Builder alipayPublicKey(String alipayPublicKey) {
            this.alipayPublicKey = alipayPublicKey;
            return this;
        }

        public Builder alipayGatewayUrl(String alipayGatewayUrl) {
            this.alipayGatewayUrl = alipayGatewayUrl;
            return this;
        }

        public Builder aplipayCertPath(String aplipayCertPath) {
            this.aplipayCertPath = aplipayCertPath;
            return this;
        }

        public Builder notifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }

        public Builder returnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public SysUserConfig build() {
            SysUserConfig config = new SysUserConfig();
            config.setId(id);
            config.setProjectName(projectName);
            config.setPlatform(platform);
            config.setKeycloakRealm(keycloakRealm);
            config.setKeycloakClientId(keycloakClientId);
            config.setKeycloakClientSecret(keycloakClientSecret);
            config.setKeycloakAuthUrl(keycloakAuthUrl);
            config.setKeycloakTokenUrl(keycloakTokenUrl);
            config.setApnsKeyPath(apnsKeyPath);
            config.setApnsTeamId(apnsTeamId);
            config.setApnsKeyId(apnsKeyId);
            config.setApnsBundleId(apnsBundleId);
            config.setApnsProduction(apnsProduction);
            config.setFirebaseProjectId(firebaseProjectId);
            config.setFirebasePrivateKey(firebasePrivateKey);
            config.setFirebaseClientEmail(firebaseClientEmail);
            config.setFirebaseClientId(firebaseClientId);
            config.setFirebaseServiceAccountPath(firebaseServiceAccountPath);
            config.setDatabaseTableNamePrefix(databaseTableNamePrefix);
            config.setUserId(userId);
            config.setAttributes(attributes);
            config.setS3BucketName(s3BucketName);
            config.setS3Region(s3Region);
            config.setS3AccessKey(s3AccessKey);
            config.setS3SecretKey(s3SecretKey);
            config.setS3Endpoint(s3Endpoint);
            config.setOssEndpoint(ossEndpoint);
            config.setOssBucketName(ossBucketName);
            config.setOssAccessKeyId(ossAccessKeyId);
            config.setOssAccessKeySecret(ossAccessKeySecret);
            config.setOssRegion(ossRegion);
            config.setSeaweedFsMasterUrl(seaweedFsMasterUrl);
            config.setSeaweedFsReplication(seaweedFsReplication);
            config.setSeaweedFsCollection(seaweedFsCollection);
            config.setStorageType(storageType);
            config.setWechatAppId(wechatAppId);
            config.setWechatAppSecret(wechatAppSecret);
            config.setWechatMchId(wechatMchId);
            config.setWechatApiKey(wechatApiKey);
            config.setWechatPayCertPath(wechatPayCertPath);
            config.setAlipayAppId(alipayAppId);
            config.setAlipayPrivateKey(alipayPrivateKey);
            config.setAlipayPublicKey(alipayPublicKey);
            config.setAlipayGatewayUrl(alipayGatewayUrl);
            config.setAplipayCertPath(aplipayCertPath);
            config.setNotifyUrl(notifyUrl);
            config.setReturnUrl(returnUrl);
            config.setDescription(description);
            return config;
        }
    }
}
