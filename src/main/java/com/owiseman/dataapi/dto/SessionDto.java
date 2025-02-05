package com.owiseman.dataapi.dto;


import java.util.Map;

public class SessionDto {
    private String userId;
    private String sessionUrl;
    private String sessionId;
    private String message;
    private Map<String, String> data;

    public SessionDto() {
    }

    public SessionDto(String userId, String sessionUrl, String sessionId, String message, Map<String, String> data) {
        this.userId = userId;
        this.sessionUrl = sessionUrl;
        this.sessionId = sessionId;
        this.message = message;
        this.data = data;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
