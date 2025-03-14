package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;

/**
 * 多对多的关联表
 */
@Entity
@Table(name = "sys_user_to_soft_role",indexes = {
        @Index(name = " idx_user_roles", columnList ="user_id, role_code")
})
public class SysUserToSoftRole {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_code")
    private String roleCode;

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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public SysUserToSoftRole(String id, String userId, String roleCode) {
        this.id = id;
        this.userId = userId;
        this.roleCode = roleCode;
    }

    public SysUserToSoftRole() {
    }

    public static class Builder {
        private String id;
        private String userId;
        private String roleCode;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder roleCode(String roleCode) {
            this.roleCode = roleCode;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public SysUserToSoftRole build() {
            SysUserToSoftRole sysUserToSoftRole = new SysUserToSoftRole();
            sysUserToSoftRole.setId(id);
            sysUserToSoftRole.setUserId(userId);
            sysUserToSoftRole.setRoleCode(roleCode);
            return sysUserToSoftRole;
        }

    }
}
