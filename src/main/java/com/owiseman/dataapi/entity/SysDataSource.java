package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sys_data_sources")
public class SysDataSource {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

    private String type;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "config")
    private String config;

    private String createdBy;
    @Column(name = "last_test_result")
    private Boolean lastTestResult;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getLastTestResult() {
        return lastTestResult;
    }

    public void setLastTestResult(Boolean lastTestResult) {
        this.lastTestResult = lastTestResult;
    }

    public SysDataSource(String id, String type, String config, String createdBy, Boolean lastTestResult) {
        this.id = id;
        this.type = type;
        this.config = config;
        this.createdBy = createdBy;
        this.lastTestResult = lastTestResult;
    }

    public SysDataSource() {
    }

    public static class Builder {
        private String id;
        private String type;
        private String config;
        private String createdBy;
        private Boolean lastTestResult;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder config(String config) {
            this.config = config;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder lastTestResult(Boolean lastTestResult) {
            this.lastTestResult = lastTestResult;
            return this;
        }

        public SysDataSource build() {
            SysDataSource dataSource = new SysDataSource();
            dataSource.setId(id);
            dataSource.setType(type);
            dataSource.setConfig(config);
            dataSource.setCreatedBy(createdBy);
            dataSource.setLastTestResult(lastTestResult);
            return dataSource;
        }
    }
}
