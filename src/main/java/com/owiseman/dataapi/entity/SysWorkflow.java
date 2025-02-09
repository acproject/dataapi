package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sys_workflows")
public class SysWorkflow {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, List<String>> definition;

    private String createBy;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

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

    public Map<String, List<String>> getDefinition() {
        return definition;
    }

    public void setDefinition(Map<String, List<String>> definition) {
        this.definition = definition;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public SysWorkflow(String id, String name, Map<String, List<String>> definition, String createBy, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.name = name;
        this.definition = definition;
        this.createBy = createBy;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
    public SysWorkflow() {
    }
}
