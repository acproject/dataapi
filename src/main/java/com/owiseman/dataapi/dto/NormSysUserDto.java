package com.owiseman.dataapi.dto;

import com.owiseman.dataapi.entity.SysUser;

public class NormSysUserDto extends SysUser {
    private String password;

    private String projectApikey;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProjectApikey() {
        return projectApikey;
    }

    public void setProjectApikey(String projectApikey) {
        this.projectApikey = projectApikey;
    }
}
