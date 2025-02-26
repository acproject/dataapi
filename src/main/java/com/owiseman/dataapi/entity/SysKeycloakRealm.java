package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "sys_keycloak_realm")
public class SysKeycloakRealm {
    @Id
    private String id;
    @Column(nullable = true)
    private String realm;
    @Column(nullable = true)
    private String displayName;
    @Column(nullable = true)
    private String displayNameHtml;
    @Column(nullable = true)
    private Integer notBefore;
    @Column(nullable = true)
    private String defaultSignatureAlgorithm;
    @Column(nullable = true)
    private Boolean revokeRefreshToken;
    @Column(nullable = true)
    private Integer refreshTokenMaxReuse;
    @Column(nullable = true)
    private Integer accessTokenLifespan;
    @Column(nullable = true)
    private Integer accessTokenLifespanForImplicitFlow;
    @Column(nullable = true)
    private Integer ssoSessionIdleTimeout;
    @Column(nullable = true)
    private Integer ssoSessionMaxLifespan;
    @Column(nullable = true)
    private Integer ssoSessionIdleTimeoutRememberMe;
    @Column(nullable = true)
    private Integer ssoSessionMaxLifespanRememberMe;
    @Column(nullable = true)
    private Integer offlineSessionIdleTimeout;
    @Column(nullable = true)
    private Boolean offlineSessionMaxLifespanEnabled;
    @Column(nullable = true)
    private Integer offlineSessionMaxLifespan;
    @Column(nullable = true)
    private Integer clientSessionIdleTimeout;
    @Column(nullable = true)
    private Integer clientSessionMaxLifespan;
    @Column(nullable = true)
    private Integer clientOfflineSessionIdleTimeout;
    @Column(nullable = true)
    private Integer clientOfflineSessionMaxLifespan;
    @Column(nullable = true)
    private Integer accessCodeLifespan;
    @Column(nullable = true)
    private Integer accessCodeLifespanUserAction;
    @Column(nullable = true)
    private Integer accessCodeLifespanLogin;
    @Column(nullable = true)
    private Integer actionTokenGeneratedByAdminLifespan;
    @Column(nullable = true)
    private Integer actionTokenGeneratedByUserLifespan;
    @Column(nullable = true)
    private Integer oauth2DeviceCodeLifespan;
    @Column(nullable = true)
    private Integer oauth2DevicePollingInterval;
    @Column(nullable = true)
    private Boolean enabled;
    @Column(nullable = true)
    private String sslRequired;

    public SysKeycloakRealm() {
    }

    public SysKeycloakRealm(String id, String realm, String displayName,
                            String displayNameHtml, Integer notBefore,
                            String defaultSignatureAlgorithm, Boolean revokeRefreshToken,
                            Integer refreshTokenMaxReuse, Integer accessTokenLifespan,
                            Integer accessTokenLifespanForImplicitFlow, Integer ssoSessionIdleTimeout,
                            Integer ssoSessionMaxLifespan, Integer ssoSessionIdleTimeoutRememberMe,
                            Integer ssoSessionMaxLifespanRememberMe, Integer offlineSessionIdleTimeout,
                            Boolean offlineSessionMaxLifespanEnabled, Integer offlineSessionMaxLifespan,
                            Integer clientSessionIdleTimeout, Integer clientSessionMaxLifespan,
                            Integer clientOfflineSessionIdleTimeout, Integer clientOfflineSessionMaxLifespan,
                            Integer accessCodeLifespan, Integer accessCodeLifespanUserAction,
                            Integer accessCodeLifespanLogin, Integer actionTokenGeneratedByAdminLifespan,
                            Integer actionTokenGeneratedByUserLifespan, Integer oauth2DeviceCodeLifespan,
                            Integer oauth2DevicePollingInterval, Boolean enabled, String sslRequired) {
        this.id = id;
        this.realm = realm;
        this.displayName = displayName;
        this.displayNameHtml = displayNameHtml;
        this.notBefore = notBefore;
        this.defaultSignatureAlgorithm = defaultSignatureAlgorithm;
        this.revokeRefreshToken = revokeRefreshToken;
        this.refreshTokenMaxReuse = refreshTokenMaxReuse;
        this.accessTokenLifespan = accessTokenLifespan;
        this.accessTokenLifespanForImplicitFlow = accessTokenLifespanForImplicitFlow;
        this.ssoSessionIdleTimeout = ssoSessionIdleTimeout;
        this.ssoSessionMaxLifespan = ssoSessionMaxLifespan;
        this.ssoSessionIdleTimeoutRememberMe = ssoSessionIdleTimeoutRememberMe;
        this.ssoSessionMaxLifespanRememberMe = ssoSessionMaxLifespanRememberMe;
        this.offlineSessionIdleTimeout = offlineSessionIdleTimeout;
        this.offlineSessionMaxLifespanEnabled = offlineSessionMaxLifespanEnabled;
        this.offlineSessionMaxLifespan = offlineSessionMaxLifespan;
        this.clientSessionIdleTimeout = clientSessionIdleTimeout;
        this.clientSessionMaxLifespan = clientSessionMaxLifespan;
        this.clientOfflineSessionIdleTimeout = clientOfflineSessionIdleTimeout;
        this.clientOfflineSessionMaxLifespan = clientOfflineSessionMaxLifespan;
        this.accessCodeLifespan = accessCodeLifespan;
        this.accessCodeLifespanUserAction = accessCodeLifespanUserAction;
        this.accessCodeLifespanLogin = accessCodeLifespanLogin;
        this.actionTokenGeneratedByAdminLifespan = actionTokenGeneratedByAdminLifespan;
        this.actionTokenGeneratedByUserLifespan = actionTokenGeneratedByUserLifespan;
        this.oauth2DeviceCodeLifespan = oauth2DeviceCodeLifespan;
        this.oauth2DevicePollingInterval = oauth2DevicePollingInterval;
        this.enabled = enabled;
        this.sslRequired = sslRequired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayNameHtml() {
        return displayNameHtml;
    }

    public void setDisplayNameHtml(String displayNameHtml) {
        this.displayNameHtml = displayNameHtml;
    }

    public Integer getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    public String getDefaultSignatureAlgorithm() {
        return defaultSignatureAlgorithm;
    }

    public void setDefaultSignatureAlgorithm(String defaultSignatureAlgorithm) {
        this.defaultSignatureAlgorithm = defaultSignatureAlgorithm;
    }

    public Boolean getRevokeRefreshToken() {
        return revokeRefreshToken;
    }

    public void setRevokeRefreshToken(Boolean revokeRefreshToken) {
        this.revokeRefreshToken = revokeRefreshToken;
    }

    public Integer getRefreshTokenMaxReuse() {
        return refreshTokenMaxReuse;
    }

    public void setRefreshTokenMaxReuse(Integer refreshTokenMaxReuse) {
        this.refreshTokenMaxReuse = refreshTokenMaxReuse;
    }

    public Integer getAccessTokenLifespan() {
        return accessTokenLifespan;
    }

    public void setAccessTokenLifespan(Integer accessTokenLifespan) {
        this.accessTokenLifespan = accessTokenLifespan;
    }

    public Integer getAccessTokenLifespanForImplicitFlow() {
        return accessTokenLifespanForImplicitFlow;
    }

    public void setAccessTokenLifespanForImplicitFlow(Integer accessTokenLifespanForImplicitFlow) {
        this.accessTokenLifespanForImplicitFlow = accessTokenLifespanForImplicitFlow;
    }

    public Integer getSsoSessionIdleTimeout() {
        return ssoSessionIdleTimeout;
    }

    public void setSsoSessionIdleTimeout(Integer ssoSessionIdleTimeout) {
        this.ssoSessionIdleTimeout = ssoSessionIdleTimeout;
    }

    public Integer getSsoSessionMaxLifespan() {
        return ssoSessionMaxLifespan;
    }

    public void setSsoSessionMaxLifespan(Integer ssoSessionMaxLifespan) {
        this.ssoSessionMaxLifespan = ssoSessionMaxLifespan;
    }

    public Integer getSsoSessionIdleTimeoutRememberMe() {
        return ssoSessionIdleTimeoutRememberMe;
    }

    public void setSsoSessionIdleTimeoutRememberMe(Integer ssoSessionIdleTimeoutRememberMe) {
        this.ssoSessionIdleTimeoutRememberMe = ssoSessionIdleTimeoutRememberMe;
    }

    public Integer getSsoSessionMaxLifespanRememberMe() {
        return ssoSessionMaxLifespanRememberMe;
    }

    public void setSsoSessionMaxLifespanRememberMe(Integer ssoSessionMaxLifespanRememberMe) {
        this.ssoSessionMaxLifespanRememberMe = ssoSessionMaxLifespanRememberMe;
    }

    public Integer getOfflineSessionIdleTimeout() {
        return offlineSessionIdleTimeout;
    }

    public void setOfflineSessionIdleTimeout(Integer offlineSessionIdleTimeout) {
        this.offlineSessionIdleTimeout = offlineSessionIdleTimeout;
    }

    public Boolean getOfflineSessionMaxLifespanEnabled() {
        return offlineSessionMaxLifespanEnabled;
    }

    public void setOfflineSessionMaxLifespanEnabled(Boolean offlineSessionMaxLifespanEnabled) {
        this.offlineSessionMaxLifespanEnabled = offlineSessionMaxLifespanEnabled;
    }

    public Integer getOfflineSessionMaxLifespan() {
        return offlineSessionMaxLifespan;
    }

    public void setOfflineSessionMaxLifespan(Integer offlineSessionMaxLifespan) {
        this.offlineSessionMaxLifespan = offlineSessionMaxLifespan;
    }

    public Integer getClientSessionIdleTimeout() {
        return clientSessionIdleTimeout;
    }

    public void setClientSessionIdleTimeout(Integer clientSessionIdleTimeout) {
        this.clientSessionIdleTimeout = clientSessionIdleTimeout;
    }

    public Integer getClientSessionMaxLifespan() {
        return clientSessionMaxLifespan;
    }

    public void setClientSessionMaxLifespan(Integer clientSessionMaxLifespan) {
        this.clientSessionMaxLifespan = clientSessionMaxLifespan;
    }

    public Integer getClientOfflineSessionIdleTimeout() {
        return clientOfflineSessionIdleTimeout;
    }

    public void setClientOfflineSessionIdleTimeout(Integer clientOfflineSessionIdleTimeout) {
        this.clientOfflineSessionIdleTimeout = clientOfflineSessionIdleTimeout;
    }

    public Integer getClientOfflineSessionMaxLifespan() {
        return clientOfflineSessionMaxLifespan;
    }

    public void setClientOfflineSessionMaxLifespan(Integer clientOfflineSessionMaxLifespan) {
        this.clientOfflineSessionMaxLifespan = clientOfflineSessionMaxLifespan;
    }

    public Integer getAccessCodeLifespan() {
        return accessCodeLifespan;
    }

    public void setAccessCodeLifespan(Integer accessCodeLifespan) {
        this.accessCodeLifespan = accessCodeLifespan;
    }

    public Integer getAccessCodeLifespanUserAction() {
        return accessCodeLifespanUserAction;
    }

    public void setAccessCodeLifespanUserAction(Integer accessCodeLifespanUserAction) {
        this.accessCodeLifespanUserAction = accessCodeLifespanUserAction;
    }

    public Integer getAccessCodeLifespanLogin() {
        return accessCodeLifespanLogin;
    }

    public void setAccessCodeLifespanLogin(Integer accessCodeLifespanLogin) {
        this.accessCodeLifespanLogin = accessCodeLifespanLogin;
    }

    public Integer getActionTokenGeneratedByAdminLifespan() {
        return actionTokenGeneratedByAdminLifespan;
    }

    public void setActionTokenGeneratedByAdminLifespan(Integer actionTokenGeneratedByAdminLifespan) {
        this.actionTokenGeneratedByAdminLifespan = actionTokenGeneratedByAdminLifespan;
    }

    public Integer getActionTokenGeneratedByUserLifespan() {
        return actionTokenGeneratedByUserLifespan;
    }

    public void setActionTokenGeneratedByUserLifespan(Integer actionTokenGeneratedByUserLifespan) {
        this.actionTokenGeneratedByUserLifespan = actionTokenGeneratedByUserLifespan;
    }

    public Integer getOauth2DeviceCodeLifespan() {
        return oauth2DeviceCodeLifespan;
    }

    public void setOauth2DeviceCodeLifespan(Integer oauth2DeviceCodeLifespan) {
        this.oauth2DeviceCodeLifespan = oauth2DeviceCodeLifespan;
    }

    public Integer getOauth2DevicePollingInterval() {
        return oauth2DevicePollingInterval;
    }

    public void setOauth2DevicePollingInterval(Integer oauth2DevicePollingInterval) {
        this.oauth2DevicePollingInterval = oauth2DevicePollingInterval;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSslRequired() {
        return sslRequired;
    }

    public void setSslRequired(String sslRequired) {
        this.sslRequired = sslRequired;
    }

    public static class Builder {
        private String id;
        private String realm;
        private String displayName;
        private String displayNameHtml;
        private Integer notBefore;
        private String defaultSignatureAlgorithm;
        private Boolean revokeRefreshToken;
        private Integer refreshTokenMaxReuse;
        private Integer accessTokenLifespan;
        private Integer accessTokenLifespanForImplicitFlow;
        private Integer ssoSessionIdleTimeout;
        private Integer ssoSessionMaxLifespan;
        private Integer ssoSessionIdleTimeoutRememberMe;
        private Integer ssoSessionMaxLifespanRememberMe;
        private Integer offlineSessionIdleTimeout;
        private Boolean offlineSessionMaxLifespanEnabled;
        private Integer offlineSessionMaxLifespan;
        private Integer clientSessionIdleTimeout;
        private Integer clientSessionMaxLifespan;
        private Integer clientOfflineSessionIdleTimeout;
        private Integer clientOfflineSessionMaxLifespan;
        private Integer accessCodeLifespan;
        private Integer accessCodeLifespanUserAction;
        private Integer accessCodeLifespanLogin;
        private Integer actionTokenGeneratedByAdminLifespan;
        private Integer actionTokenGeneratedByUserLifespan;
        private Integer oauth2DeviceCodeLifespan;
        private Integer oauth2DevicePollingInterval;
        private Boolean enabled;
        private String sslRequired;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder realm(String realm) {
            this.realm = realm;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder displayNameHtml(String displayNameHtml) {
            this.displayNameHtml = displayNameHtml;
            return this;
        }

        public Builder notBefore(Integer notBefore) {
            this.notBefore = notBefore;
            return this;
        }

        public Builder defaultSignatureAlgorithm(String defaultSignatureAlgorithm) {
            this.defaultSignatureAlgorithm = defaultSignatureAlgorithm;
            return this;
        }

        public Builder revokeRefreshToken(Boolean revokeRefreshToken) {
            this.revokeRefreshToken = revokeRefreshToken;
            return this;
        }

        public Builder refreshTokenMaxReuse(Integer refreshTokenMaxReuse) {
            this.refreshTokenMaxReuse = refreshTokenMaxReuse;
            return this;
        }

        public Builder accessTokenLifespan(Integer accessTokenLifespan) {
            this.accessTokenLifespan = accessTokenLifespan;
            return this;
        }

        public Builder accessTokenLifespanForImplicitFlow(Integer accessTokenLifespanForImplicitFlow) {
            this.accessTokenLifespanForImplicitFlow = accessTokenLifespanForImplicitFlow;
            return this;
        }

        public Builder ssoSessionIdleTimeout(Integer ssoSessionIdleTimeout) {
            this.ssoSessionIdleTimeout = ssoSessionIdleTimeout;
            return this;
        }

        public Builder ssoSessionMaxLifespan(Integer ssoSessionMaxLifespan) {
            this.ssoSessionMaxLifespan = ssoSessionMaxLifespan;
            return this;
        }

        public Builder ssoSessionIdleTimeoutRememberMe(Integer ssoSessionIdleTimeoutRememberMe) {
            this.ssoSessionIdleTimeoutRememberMe = ssoSessionIdleTimeoutRememberMe;
            return this;
        }

        public Builder ssoSessionMaxLifespanRememberMe(Integer ssoSessionMaxLifespanRememberMe) {
            this.ssoSessionMaxLifespanRememberMe = ssoSessionMaxLifespanRememberMe;
            return this;
        }

        public Builder offlineSessionIdleTimeout(Integer offlineSessionIdleTimeout) {
            this.offlineSessionIdleTimeout = offlineSessionIdleTimeout;
            return this;
        }

        public Builder offlineSessionMaxLifespanEnabled(Boolean offlineSessionMaxLifespanEnabled) {
            this.offlineSessionMaxLifespanEnabled = offlineSessionMaxLifespanEnabled;
            return this;
        }

        public Builder offlineSessionMaxLifespan(Integer offlineSessionMaxLifespan) {
            this.offlineSessionMaxLifespan = offlineSessionMaxLifespan;
            return this;
        }

        public Builder clientSessionIdleTimeout(Integer clientSessionIdleTimeout) {
            this.clientSessionIdleTimeout = clientSessionIdleTimeout;
            return this;
        }

        public Builder clientSessionMaxLifespan(Integer clientSessionMaxLifespan) {
            this.clientSessionMaxLifespan = clientSessionMaxLifespan;
            return this;
        }

        public Builder clientOfflineSessionIdleTimeout(Integer clientOfflineSessionIdleTimeout) {
            this.clientOfflineSessionIdleTimeout = clientOfflineSessionIdleTimeout;
            return this;
        }

        public Builder clientOfflineSessionMaxLifespan(Integer clientOfflineSessionMaxLifespan) {
            this.clientOfflineSessionMaxLifespan = clientOfflineSessionMaxLifespan;
            return this;
        }

        public Builder accessCodeLifespan(Integer accessCodeLifespan) {
            this.accessCodeLifespan = accessCodeLifespan;
            return this;
        }

        public Builder accessCodeLifespanUserAction(Integer accessCodeLifespanUserAction) {
            this.accessCodeLifespanUserAction = accessCodeLifespanUserAction;
            return this;
        }

        public Builder accessCodeLifespanLogin(Integer accessCodeLifespanLogin) {
            this.accessCodeLifespanLogin = accessCodeLifespanLogin;
            return this;
        }

        public Builder actionTokenGeneratedByAdminLifespan(Integer actionTokenGeneratedByAdminLifespan) {
            this.actionTokenGeneratedByAdminLifespan = actionTokenGeneratedByAdminLifespan;
            return this;
        }

        public Builder actionTokenGeneratedByUserLifespan(Integer actionTokenGeneratedByUserLifespan) {
            this.actionTokenGeneratedByUserLifespan = actionTokenGeneratedByUserLifespan;
            return this;
        }

        public Builder oauth2DeviceCodeLifespan(Integer oauth2DeviceCodeLifespan) {
            this.oauth2DeviceCodeLifespan = oauth2DeviceCodeLifespan;
            return this;
        }

        public Builder oauth2DevicePollingInterval(Integer oauth2DevicePollingInterval) {
            this.oauth2DevicePollingInterval = oauth2DevicePollingInterval;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder sslRequired(String sslRequired) {
            this.sslRequired = sslRequired;
            return this;
        }

        public SysKeycloakRealm build() {
            SysKeycloakRealm sysKeycloakRealm = new SysKeycloakRealm();
            sysKeycloakRealm.setId(id);
            sysKeycloakRealm.setRealm(realm);
            sysKeycloakRealm.setDisplayName(displayName);
            sysKeycloakRealm.setDisplayNameHtml(displayNameHtml);
            sysKeycloakRealm.setNotBefore(notBefore);
            sysKeycloakRealm.setDefaultSignatureAlgorithm(defaultSignatureAlgorithm);
            sysKeycloakRealm.setRevokeRefreshToken(revokeRefreshToken);
            sysKeycloakRealm.setRefreshTokenMaxReuse(refreshTokenMaxReuse);
            sysKeycloakRealm.setAccessTokenLifespan(accessTokenLifespan);
            sysKeycloakRealm.setAccessTokenLifespanForImplicitFlow(accessTokenLifespanForImplicitFlow);
            sysKeycloakRealm.setSsoSessionIdleTimeout(ssoSessionIdleTimeout);
            sysKeycloakRealm.setSsoSessionMaxLifespan(ssoSessionMaxLifespan);
            sysKeycloakRealm.setSsoSessionIdleTimeoutRememberMe(ssoSessionIdleTimeoutRememberMe);
            sysKeycloakRealm.setSsoSessionMaxLifespanRememberMe(ssoSessionMaxLifespanRememberMe);
            sysKeycloakRealm.setOfflineSessionIdleTimeout(offlineSessionIdleTimeout);
            sysKeycloakRealm.setOfflineSessionMaxLifespanEnabled(offlineSessionMaxLifespanEnabled);
            sysKeycloakRealm.setOfflineSessionMaxLifespan(offlineSessionMaxLifespan);
            sysKeycloakRealm.setClientSessionIdleTimeout(clientSessionIdleTimeout);
            sysKeycloakRealm.setClientSessionMaxLifespan(clientSessionMaxLifespan);
            sysKeycloakRealm.setClientOfflineSessionIdleTimeout(clientOfflineSessionIdleTimeout);
            sysKeycloakRealm.setClientOfflineSessionMaxLifespan(clientOfflineSessionMaxLifespan);
            sysKeycloakRealm.setAccessCodeLifespan(accessCodeLifespan);
            sysKeycloakRealm.setAccessCodeLifespanUserAction(accessCodeLifespanUserAction);
            sysKeycloakRealm.setAccessCodeLifespanLogin(accessCodeLifespanLogin);
            sysKeycloakRealm.setActionTokenGeneratedByAdminLifespan(actionTokenGeneratedByAdminLifespan);
            sysKeycloakRealm.setActionTokenGeneratedByUserLifespan(actionTokenGeneratedByUserLifespan);
            sysKeycloakRealm.setOauth2DeviceCodeLifespan(oauth2DeviceCodeLifespan);
            sysKeycloakRealm.setOauth2DevicePollingInterval(oauth2DevicePollingInterval);
            sysKeycloakRealm.setEnabled(enabled);
            sysKeycloakRealm.setSslRequired(sslRequired);

            return sysKeycloakRealm;
        }
    }
}
