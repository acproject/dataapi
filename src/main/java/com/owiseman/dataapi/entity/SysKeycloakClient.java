package com.owiseman.dataapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_keycloak_clients")
public class SysKeycloakClient {
    @Id
    private String id;
    private String realmName;
    private String clientId;
    private String secret;
    private String name;
    private String description;
    private String type;
    private String rootUrl;
    private String adminUrl;
    private String baseUrl;
    private Boolean surrogateAuthRequired;
    private Boolean enabled;
    private Boolean alwaysDisplayInConsole;
    private String clientAuthenticatorType;
    private String registrationAccessToke;

    public SysKeycloakClient() {
    }

    public SysKeycloakClient(String id, String realmName, String clientId, String secret, String name,
                             String description, String type, String rootUrl, String adminUrl,
                             String baseUrl, Boolean surrogateAuthRequired, Boolean enabled,
                             Boolean alwaysDisplayInConsole,
                             String clientAuthenticatorType, String registrationAccessToke) {
        this.id = id;
        this.realmName = realmName;
        this.clientId = clientId;
        this.secret = secret;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rootUrl = rootUrl;
        this.adminUrl = adminUrl;
        this.baseUrl = baseUrl;
        this.surrogateAuthRequired = surrogateAuthRequired;
        this.enabled = enabled;
        this.alwaysDisplayInConsole = alwaysDisplayInConsole;
        this.clientAuthenticatorType = clientAuthenticatorType;
        this.registrationAccessToke = registrationAccessToke;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getAdminUrl() {
        return adminUrl;
    }

    public void setAdminUrl(String adminUrl) {
        this.adminUrl = adminUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean getSurrogateAuthRequired() {
        return surrogateAuthRequired;
    }

    public void setSurrogateAuthRequired(Boolean surrogateAuthRequired) {
        this.surrogateAuthRequired = surrogateAuthRequired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAlwaysDisplayInConsole() {
        return alwaysDisplayInConsole;
    }

    public void setAlwaysDisplayInConsole(Boolean alwaysDisplayInConsole) {
        this.alwaysDisplayInConsole = alwaysDisplayInConsole;
    }

    public String getClientAuthenticatorType() {
        return clientAuthenticatorType;
    }

    public void setClientAuthenticatorType(String clientAuthenticatorType) {
        this.clientAuthenticatorType = clientAuthenticatorType;
    }

    public String getRegistrationAccessToke() {
        return registrationAccessToke;
    }

    public void setRegistrationAccessToke(String registrationAccessToke) {
        this.registrationAccessToke = registrationAccessToke;
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }
}
