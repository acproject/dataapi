package com.owiseman.dataapi.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
@ConfigurationProperties(prefix = "apns")
public class ApnsConfig {
    private Resource keyPath;
    private String teamId;
    private String keyId;
    private String bundleId;
    private boolean production;

    public Resource getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(Resource keyPath) {
        this.keyPath = keyPath;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public ApnsConfig() {
    }

    public ApnsConfig(Resource keyPath, String teamId, String keyId, String bundleId, boolean production) {
        this.keyPath = keyPath;
        this.teamId = teamId;
        this.keyId = keyId;
        this.bundleId = bundleId;
        this.production = production;
    }
}
