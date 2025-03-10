package com.owiseman.dataapi.dto;

import org.keycloak.representations.idm.ClientRepresentation;

public class CreateKeycloakClientDto {
    private ClientRepresentation clientRepresentation;

    private String realmName;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private Boolean isForProject;

    public CreateKeycloakClientDto(ClientRepresentation clientRepresentation, String token, String realmName, String clientId, String clientSecret, String username, String password,Boolean isForProject) {
        this.clientRepresentation = clientRepresentation;

        this.realmName = realmName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
        this.isForProject = isForProject;
    }

    public Boolean getForProject() {
        return isForProject;
    }

    public void setForProject(Boolean forProject) {
        isForProject = forProject;
    }

    public CreateKeycloakClientDto() {
    }

    public ClientRepresentation getClientRepresentation() {
        return clientRepresentation;
    }

    public void setClientRepresentation(ClientRepresentation clientRepresentation) {
        this.clientRepresentation = clientRepresentation;
    }



    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
