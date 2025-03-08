package com.owiseman.dataapi.dto;

// 该DTO主要用于非Password类型获得的Token信息
public record TokenDto(
        String accessToken,
        Integer expiresIn,
        Integer refreshExpiresIn,
        String tokenType,
        Integer notBeforePolicy,
        String scope
) {
}
