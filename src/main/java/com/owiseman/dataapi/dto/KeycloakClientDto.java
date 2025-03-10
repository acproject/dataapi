package com.owiseman.dataapi.dto;

import org.keycloak.representations.idm.ClientRepresentation;

public record KeycloakClientDto(
        String id,
        String clientId,
        String secret,
        String name,
        String description,
        String type,
        String rootUrl,
        String adminUrl,
        String baseUrl,
        Boolean surrogateAuthRequired,
        Boolean enabled,
        Boolean alwaysDisplayInConsole,
        String clientAuthenticatorType,
        String registrationAccessToken,
        Boolean isForProject
) {
}
