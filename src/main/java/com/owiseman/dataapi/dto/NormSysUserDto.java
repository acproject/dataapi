package com.owiseman.dataapi.dto;

import com.owiseman.dataapi.entity.SysUser;

public class NormSysUserDto extends SysUser {
    private String password;

    private String projectApiKey;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProjectApiKey() {
        return projectApiKey;
    }

    public void setProjectApiKey(String projectApiKey) {
        this.projectApiKey = projectApiKey;
    }
}
