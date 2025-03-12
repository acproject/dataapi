package com.owiseman.dataapi.dto;

import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.util.JwtParserUtil;

import java.util.List;
import java.util.Map;

public class LoginResponseDto {
    private TokenResponse tokenResponse;

    private String clientId;
    private String email;
    private String realmName;
    private String userId;
    private String username;
    private Map<String, List<String>> attributes;

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TokenResponse getTokenResponse() {
        return tokenResponse;
    }

    public void setTokenResponse(TokenResponse tokenResponse) {
        this.tokenResponse = tokenResponse;
        if(tokenResponse.getAccessToken() != null) {
            this.setUserId(JwtParserUtil.getUserId(tokenResponse.getAccessToken()));
            this.setRealmName(JwtParserUtil.getRealmName(tokenResponse.getAccessToken()));
            this.setUsername(JwtParserUtil.getUsername(tokenResponse.getAccessToken()));
            this.setEmail(JwtParserUtil.getEmail(tokenResponse.getAccessToken()));
            this.setClientId(JwtParserUtil.getClientId(tokenResponse.getAccessToken()));
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getEmail() {
        return email;
    }

    public String getRealmName() {
        return realmName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
