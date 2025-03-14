package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "sys_soft_role")
public class SysSoftRole {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    @Column(name = "id")
    private String id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "is_active")
    private Boolean isActive;

    public SysSoftRole() {
    }

    public SysSoftRole(String id, String roleName, String roleDescription, String roleCode, Boolean isActive) {
        this.id = id;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
        this.roleCode = roleCode;
        this.isActive = isActive;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public static class Builder {
        private String id;
        private String roleName;
        private String roleDescription;
        private String roleCode;
        private Boolean isActive;

        public Builder id(String id) {
            this.id = id;
            return this;
        }
        public Builder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }
        public Builder roleDescription(String roleDescription) {
            this.roleDescription = roleDescription;
            return this;
        }
        public Builder roleCode(String roleCode) {
            this.roleCode = roleCode;
            return this;
        }
        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }
        public SysSoftRole build() {
            SysSoftRole sysSoftRole = new SysSoftRole();
            sysSoftRole.setId(id);
            sysSoftRole.setRoleName(roleName);
            sysSoftRole.setRoleDescription(roleDescription);
            sysSoftRole.setRoleCode(roleCode);
            sysSoftRole.setActive(isActive);
            return sysSoftRole;
        }
    }
}
