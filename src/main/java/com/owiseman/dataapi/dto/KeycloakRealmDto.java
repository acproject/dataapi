package com.owiseman.dataapi.dto;

import java.util.Optional;

public class KeycloakRealmDto {
    private String name;
    private Boolean enabled;

    public KeycloakRealmDto(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public KeycloakRealmDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
