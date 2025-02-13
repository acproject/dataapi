package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "sys_keycloak_realms")
public class SysKeycloakRealm {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
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
}
