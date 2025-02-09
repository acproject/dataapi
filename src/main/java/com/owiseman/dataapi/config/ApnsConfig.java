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
    private boolean production = false;

    // 添加连接池配置参数
    private int toThreads = Runtime.getRuntime().availableProcessors() * 2;
    private int maxConcurrentStreams = 10_000;

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

    public int getToThreads() {
        return toThreads;
    }

    public void setToThreads(int toThreads) {
        this.toThreads = toThreads;
    }

    public int getMaxConcurrentStreams() {
        return maxConcurrentStreams;
    }

    public void setMaxConcurrentStreams(int maxConcurrentStreams) {
        this.maxConcurrentStreams = maxConcurrentStreams;
    }

    public ApnsConfig(Resource keyPath, String teamId, String keyId, String bundleId, boolean production, int toThreads, int maxConcurrentStreams) {
        this.keyPath = keyPath;
        this.teamId = teamId;
        this.keyId = keyId;
        this.bundleId = bundleId;
        this.production = production;
        this.toThreads = toThreads;
        this.maxConcurrentStreams = maxConcurrentStreams;
    }
}
