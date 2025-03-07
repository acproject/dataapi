package com.owiseman.dataapi.entity;

import com.owiseman.dataapi.dto.Platform;
import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_device_tokens")
public class SysDeviceToken {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;
    @Column(name = "token")
    private String token;
    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Column(name = "last_active")
    private LocalDateTime lastActive;
    @Column(name = "active")
    private Boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public LocalDateTime getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public SysDeviceToken(String userId, String token, Platform platform, LocalDateTime lastActive, Boolean active) {
        this.userId = userId;
        this.token = token;
        this.platform = platform;
        this.lastActive = lastActive;
        this.active = active;
    }

    public SysDeviceToken() {
    }

    public static class Builder {
        private String id;
        private String userId;
        private String token;
        private Platform platform;
        private LocalDateTime lastActive;
        private Boolean active;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder platform(Platform platform) {
            this.platform = platform;
            return this;
        }

        public Builder lastActive(LocalDateTime lastActive) {
            this.lastActive = lastActive;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public SysDeviceToken build() {
            SysDeviceToken deviceToken = new SysDeviceToken();
            deviceToken.setId(id);
            deviceToken.setUserId(userId);
            deviceToken.setToken(token);
            deviceToken.setPlatform(platform);
            deviceToken.setLastActive(lastActive);
            deviceToken.setActive(active);
            return deviceToken;
        }
    }
}
