package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sys_table_metadata")
public class SysTableMetadata {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

    @Column(name = "table_name", nullable = false, unique = true)
    private String tableName;
     @Column(length = 1000)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = true)
    private Map<String, List<String>> tableDefinition;

    @Column(nullable = false, length = 20)
    private String status;

    @OneToMany(mappedBy = "sysTableMetadata", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("ordinalPosition")
    private List<SysColumnMetadata> columns;

    private String userId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, List<String>> getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(Map<String, List<String>> tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SysColumnMetadata> getColumns() {
        return columns;
    }

    public void setColumns(List<SysColumnMetadata> columns) {
        this.columns = columns;
    }

    public SysTableMetadata(String id, String tableName, String description, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, Map<String, List<String>> tableDefinition, String status, List<SysColumnMetadata> columns,String userId) {
        this.id = id;
        this.tableName = tableName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.tableDefinition = tableDefinition;
        this.status = status;
        this.columns = columns;
        this.userId = userId;
    }

    public SysTableMetadata() {
    }

    public static class Builder {
        private String id;
        private String tableName;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private Map<String, List<String>> tableDefinition;
        private String status;
        private List<SysColumnMetadata> columns;
        private String userId;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder tableDefinition(Map<String, List<String>> tableDefinition) {
            this.tableDefinition = tableDefinition;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder columns(List<SysColumnMetadata> columns) {
            this.columns = columns;
            return this;
        }

        public SysTableMetadata build() {
            SysTableMetadata tableMetadata = new SysTableMetadata();
            tableMetadata.setId(id);
            tableMetadata.setTableName(tableName);
            tableMetadata.setDescription(description);
            tableMetadata.setCreatedAt(createdAt);
            tableMetadata.setUpdatedAt(updatedAt);
            tableMetadata.setCreatedBy(createdBy);
            tableMetadata.setTableDefinition(tableDefinition);
            tableMetadata.setStatus(status);
            tableMetadata.setColumns(columns);
            tableMetadata.setUserId(userId);
            return tableMetadata;
        }
    }


}
