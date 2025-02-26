package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.plugins.PluginStatus;
import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sys_plugins")
public class SysPlugin {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

    private String name;
    private String version;

    private Integer port;

    @Enumerated(EnumType.STRING)
    private PluginStatus status;

    private String wasmPath;
    private LocalDateTime lastHeartbeat;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "config_schema")
    private Map<String, List<String>> configSchema;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "runtime_config")
    private Map<String, List<String>> runtimeConfig;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, List<String>> getConfigSchema() {
        return configSchema;
    }

    public void setConfigSchema(Map<String, List<String>> configSchema) {
        this.configSchema = configSchema;
    }

    public Map<String, List<String>> getRuntimeConfig() {
        return runtimeConfig;
    }

    public void setRuntimeConfig(Map<String, List<String>> runtimeConfig) {
        this.runtimeConfig = runtimeConfig;
    }

    public SysPlugin(String id, String name, String version, Map<String, List<String>> configSchema, Map<String, List<String>> runtimeConfig) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.configSchema = configSchema;
        this.runtimeConfig = runtimeConfig;
    }

    public SysPlugin(String id, String name, String version, PluginStatus status, String wasmPath, LocalDateTime lastHeartbeat, Map<String,
            List<String>> configSchema, Map<String, List<String>> runtimeConfig) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.status = status;
        this.wasmPath = wasmPath;
        this.lastHeartbeat = lastHeartbeat;
        this.configSchema = configSchema;
        this.runtimeConfig = runtimeConfig;
    }

    public PluginStatus getStatus() {
        return status;
    }

    public void setStatus(PluginStatus status) {
        this.status = status;
    }

    public String getWasmPath() {
        return wasmPath;
    }

    public void setWasmPath(String wasmPath) {
        this.wasmPath = wasmPath;
    }

    public LocalDateTime getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(LocalDateTime lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public SysPlugin(String id, String name, String version, Integer port, PluginStatus status, String wasmPath, LocalDateTime lastHeartbeat, Map<String, List<String>> configSchema, Map<String, List<String>> runtimeConfig) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.port = port;
        this.status = status;
        this.wasmPath = wasmPath;
        this.lastHeartbeat = lastHeartbeat;
        this.configSchema = configSchema;
        this.runtimeConfig = runtimeConfig;
    }

    public SysPlugin() {
    }

    public static class Builder {
        private String id;
        private String name;
        private String version;
        private Integer port;
        private PluginStatus status;
        private String wasmPath;
        private LocalDateTime lastHeartbeat;
        private Map<String, List<String>> configSchema;
        private Map<String, List<String>> runtimeConfig;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder status(PluginStatus status) {
            this.status = status;
            return this;
        }

        public Builder wasmPath(String wasmPath) {
            this.wasmPath = wasmPath;
            return this;
        }

        public Builder lastHeartbeat(LocalDateTime lastHeartbeat) {
            this.lastHeartbeat = lastHeartbeat;
            return this;
        }

        public Builder configSchema(Map<String, List<String>> configSchema) {
            this.configSchema = configSchema;
            return this;
        }

        public Builder runtimeConfig(Map<String, List<String>> runtimeConfig) {
            this.runtimeConfig = runtimeConfig;
            return this;
        }

        public SysPlugin build() {
            SysPlugin plugin = new SysPlugin();
            plugin.setId(id);
            plugin.setName(name);
            plugin.setVersion(version);
            plugin.setPort(port);
            plugin.setStatus(status);
            plugin.setWasmPath(wasmPath);
            plugin.setLastHeartbeat(lastHeartbeat);
            plugin.setConfigSchema(configSchema);
            plugin.setRuntimeConfig(runtimeConfig);
            return plugin;
        }
    }
}
