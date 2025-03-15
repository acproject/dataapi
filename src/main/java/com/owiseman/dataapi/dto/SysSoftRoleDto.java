package com.owiseman.dataapi.dto;

import com.owiseman.dataapi.entity.SysSoftRole;

public class SysSoftRoleDto {
    private SysSoftRole sysSoftRole;



    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

     public SysSoftRole getSysSoftRole() {
        return sysSoftRole;
    }

    public void setSysSoftRole(SysSoftRole sysSoftRole) {
        this.sysSoftRole = sysSoftRole;
    }
}
