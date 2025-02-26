package com.owiseman.dataapi.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import java.util.List;
import java.util.Map;

@Entity
@Table(name = "sys_users")
public class SysUser {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "username")
    private String username;
    @Column(nullable = true)
    private String firstName;
    @Column(nullable = true)
    private String lastName;
    @Column
    private String email;
    @Column(nullable = true)
    private Boolean emailVerified;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = true)
    private Map<String, List<String>> attributes;
    @Column(nullable = true)
    private Long createdTimestamp;
    @Column(nullable = true)
    private Boolean enabled;
    @Column(nullable = true)
    private String realmName;
    @Column(nullable = true)
    private String clientId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public SysUser(String id, String username, String firstName, String lastName, String email, Boolean emailVerified, Map<String, List<String>> attributes, Long createdTimestamp, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.attributes = attributes;
        this.createdTimestamp = createdTimestamp;
        this.enabled = enabled;
    }

    public SysUser(String id, String username, String firstName, String lastName, String email, Boolean emailVerified, Map<String, List<String>> attributes, Long createdTimestamp, Boolean enabled, String realmName, String clientId) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.attributes = attributes;
        this.createdTimestamp = createdTimestamp;
        this.enabled = enabled;
        this.realmName = realmName;
        this.clientId = clientId;
    }

    public SysUser() {
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

    public static class Builder {
        private String id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private Boolean emailVerified;
        private Map<String, List<String>> attributes;
        private Long createdTimestamp;
        private Boolean enabled;
        private String realmName;
        private String clientId;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder emailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder attributes(Map<String, List<String>> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder createdTimestamp(Long createdTimestamp) {
            this.createdTimestamp = createdTimestamp;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
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

        public SysUser build() {
            SysUser user = new SysUser();
            user.setId(id);
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setEmailVerified(emailVerified);
            user.setAttributes(attributes);
            user.setCreatedTimestamp(createdTimestamp);
            user.setEnabled(enabled);
            user.setRealmName(realmName);
            user.setClientId(clientId);
            return user;
        }
    }
}
