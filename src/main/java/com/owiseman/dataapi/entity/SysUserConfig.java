package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.dto.Platform;
import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sys_user_config")
public class SysUserConfig {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

    private String projectName;

    @Enumerated(EnumType.STRING)
    private Platform platform;
    // keycloak的基础配置
    private String keycloakRealm;
    private String keycloakClientId;
    private String keycloakClientSecret;
    private String keycloakAuthUrl;
    private String keycloakTokenUrl;
    // apns的基础配置
    private String apnsKeyPath;
    private String apnsTeamId;
    private String apnsKeyId;
    private String apnsBundleId;
    private Boolean apnsProduction;

    // firebase的基础配置
    private String firebaseProjectId;
    private String firebasePrivateKey;
    private String firebaseClientEmail;
    private String firebaseClientId;
    private String firebaseServiceAccountPath;
    // 规则为realm名+ "_" + tableName +"_" = "realm_table_"
    private String databaseTableNamePrefix;

    @Column(nullable = false)
    private String userId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = true)
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
    private String s3BucketName;
    private String s3Region;
    private String s3AccessKey;
    private String s3SecretKey;
    private String s3Endpoint;

    // 阿里云 OSS 配置
    private String ossEndpoint;
    private String ossBucketName;
    private String ossAccessKeyId;
    private String ossAccessKeySecret;
    private String ossRegion;

    // SeaweedFS 配置
    private String seaweedFsMasterUrl;
    private Integer seaweedFsReplication;
    private String seaweedFsCollection;

    // 存储类型（seaweedfs/s3/aliyun）
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
    private String wechatAppId;
    private String wechatAppSecret;
    private String wechatMchId;
    private String wechatApiKey;

    // 支付宝小程序配置
    private String alipayAppId;
    private String alipayPrivateKey;
    private String alipayPublicKey;
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
}
