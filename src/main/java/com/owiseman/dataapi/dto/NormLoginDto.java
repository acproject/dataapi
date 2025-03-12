package com.owiseman.dataapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record NormLoginDto(
        @NotBlank(message = "用户名或邮箱不能为空")
        String principal,    // 可以是用户名或邮箱

        @NotBlank(message = "密码不能为空")
        String password,

        @JsonProperty("project_id")
        @NotBlank(message = "project id 不能为空")
        String projectId
) {
}
