package com.owiseman.dataapi.dto;

import com.owiseman.dataapi.entity.SysUser;

public class NormSysUserDto extends SysUser {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
