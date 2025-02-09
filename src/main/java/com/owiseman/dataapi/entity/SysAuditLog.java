package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sys_audit_logs")
public class SysAuditLog {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

    private String userId;
    private String actionType;
    private String targetType;
    private String targetId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "details")
    private Map<String, List<String>> details;

    private LocalDateTime timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Map<String, List<String>> getDetails() {
        return details;
    }

    public void setDetails(Map<String, List<String>> details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public SysAuditLog(String id, String userId, String actionType, String targetType, String targetId, Map<String, List<String>> details, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.actionType = actionType;
        this.targetType = targetType;
        this.targetId = targetId;
        this.details = details;
        this.timestamp = timestamp;
    }

    public SysAuditLog() {
    }
}
