package com.owiseman.dataapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectCreateRequest(
        @NotBlank(message = "项目名不能为空")
        String projectName,

        @NotNull(message = "平台类型不能为空")
        Platform platform

) {
}
