package com.owiseman.dataapi.dto;

public class PushRequest {
    private String deviceToken;
    private Platform platform;
    private String title;
    private String content;

    public PushRequest() {
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PushRequest(String deviceToken, Platform platform, String title, String content) {
        this.deviceToken = deviceToken;
        this.platform = platform;
        this.title = title;
        this.content = content;
    }
}
