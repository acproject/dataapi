package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    public SysPlugin() {
    }
}
