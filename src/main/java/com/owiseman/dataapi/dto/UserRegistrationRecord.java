package com.owiseman.dataapi.dto;

public record UserRegistrationRecord(
        String id,
        String username,
        String email,
        String firstname,
        String lastname,
        String password
) {
}
