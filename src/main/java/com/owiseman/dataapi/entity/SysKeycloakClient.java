package com.owiseman.dataapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_keycloak_clients")
public class SysKeycloakClient {
    @Id
    private String id;
    @Column(name = "realm_name", nullable = true)
    private String realmName;
    @Column(name = "client_id", nullable = true)
    private String clientId;
    @Column(name = "secret", nullable = true)
    private String secret;
    @Column(name = "name", nullable = true)
    private String name;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "type", nullable = true)
    private String type;
    @Column(name = "root_url", nullable = true)
    private String rootUrl;
    @Column(name = "admin_url", nullable = true)
    private String adminUrl;
    @Column(name = "base_url", nullable = true)
    private String baseUrl;
    @Column(name = "surrogate_auth_required", nullable = true)
    private Boolean surrogateAuthRequired;
    @Column(name = "enabled", nullable = true)
    private Boolean enabled;
    @Column(name = "always_display_in_console", nullable = true)
    private Boolean alwaysDisplayInConsole;
    @Column(name = "client_authenticator_type", nullable = true)
    private String clientAuthenticatorType;
    @Column(name = "registration_access_token", nullable = true)
    private String registrationAccessToken;

    @Column(name = "is_for_project")
    private Boolean isForProject = false;

    public String getRegistrationAccessToken() {
        return registrationAccessToken;
    }

    public Boolean getForProject() {
        return isForProject;
    }

    public void setForProject(Boolean forProject) {
        isForProject = forProject;
    }

    public SysKeycloakClient() {
    }

    public SysKeycloakClient(String id, String realmName, String clientId, String secret, String name,
                             String description, String type, String rootUrl, String adminUrl,
                             String baseUrl, Boolean surrogateAuthRequired, Boolean enabled,
                             Boolean alwaysDisplayInConsole,
                             String clientAuthenticatorType, String registrationAccessToken, Boolean isForProject) {
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
        this.registrationAccessToken = registrationAccessToken;
        this.isForProject = isForProject;
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
        return registrationAccessToken;
    }

    public void setRegistrationAccessToken(String registrationAccessToken) {
        this.registrationAccessToken = registrationAccessToken;
    }

    public String getRealmName() {
        return realmName;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public static class Builder {
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
        private String registrationAccessToken;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder realmName(String realmName) {
            this.realmName = realmName;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder rootUrl(String rootUrl) {
            this.rootUrl = rootUrl;
            return this;
        }

        public Builder adminUrl(String adminUrl) {
            this.adminUrl = adminUrl;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder surrogateAuthRequired(Boolean surrogateAuthRequired) {
            this.surrogateAuthRequired = surrogateAuthRequired;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder alwaysDisplayInConsole(Boolean alwaysDisplayInConsole) {
            this.alwaysDisplayInConsole = alwaysDisplayInConsole;
            return this;
        }

        public Builder clientAuthenticatorType(String clientAuthenticatorType) {
            this.clientAuthenticatorType = clientAuthenticatorType;
            return this;
        }

        public Builder registrationAccessToken(String registrationAccessToken) {
            this.registrationAccessToken = registrationAccessToken;
            return this;
        }

        public SysKeycloakClient build() {
            SysKeycloakClient keycloakClient = new SysKeycloakClient();
            keycloakClient.setId(id);
            keycloakClient.setRealmName(realmName);
            keycloakClient.setClientId(clientId);
            keycloakClient.setSecret(secret);
            keycloakClient.setName(name);
            keycloakClient.setDescription(description);
            keycloakClient.setType(type);
            keycloakClient.setRootUrl(rootUrl);
            keycloakClient.setAdminUrl(adminUrl);
            keycloakClient.setBaseUrl(baseUrl);
            keycloakClient.setSurrogateAuthRequired(surrogateAuthRequired);
            keycloakClient.setEnabled(enabled);
            keycloakClient.setAlwaysDisplayInConsole(alwaysDisplayInConsole);
            keycloakClient.setClientAuthenticatorType(clientAuthenticatorType);
            keycloakClient.setRegistrationAccessToken(registrationAccessToken);
            return keycloakClient;
        }
    }
}
