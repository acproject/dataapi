package com.owiseman.dataapi.entity;

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

    public SysUserConfig(String id, String keycloakRealm, String keycloakClientId, String keycloakClientSecret, String keycloakAuthUrl, String keycloakTokenUrl, String apnsKeyPath, String apnsTeamId, String apnsKeyId, String apnsBundleId, Boolean apnsProduction, String firebaseProjectId, String firebasePrivateKey, String firebaseClientEmail, String firebaseClientId, String firebaseServiceAccountPath, String databaseTableNamePrefix, String userId, Map<String, List<String>> attributes) {
        this.id = id;
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
    }
}
