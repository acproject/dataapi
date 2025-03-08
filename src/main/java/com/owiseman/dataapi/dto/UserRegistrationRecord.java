package com.owiseman.dataapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public record UserRegistrationRecord(
        String id,
        String username,
        String email,
        String firstname,
        String lastname,
        String password,
        Optional<String> realmName,
        @JsonProperty("user_id")
        Optional<String> userId,
        @JsonProperty("client_id")
        Optional<String> clientId
) {
}
