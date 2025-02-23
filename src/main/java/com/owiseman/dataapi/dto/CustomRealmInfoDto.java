package com.owiseman.dataapi.dto;

public record CustomRealmInfoDto(
        String token,
        String realm,
        String clientId,
        String clientSecret,
        String username,
        String password
) {
}
