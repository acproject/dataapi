package com.owiseman.dataapi.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
    @NotBlank(message = "用户名或邮箱不能为空")
    String principal,    // 可以是用户名或邮箱
    
    @NotBlank(message = "密码不能为空")
    String password
) {}