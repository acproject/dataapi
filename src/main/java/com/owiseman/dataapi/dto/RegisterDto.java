package com.owiseman.dataapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDto(
    @NotBlank(message = "用户名不能为空")
    String username,

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    String email,

    @NotBlank(message = "组织名不能为空")
    String organization,

    @NotBlank(message = "电话号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入有效的手机号")
    String phone,

    String firstName,

    String lastName,

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "密码长度不能小于8位")
    String password,

    @NotBlank(message = "确认密码不能为空")
    String confirmPassword
) {
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}