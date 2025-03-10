package com.owiseman.dataapi.config;

import org.keycloak.OAuth2Constants;

public interface OAuth2ConstantsExtends extends OAuth2Constants {
    String ADMIN = "admin";
    boolean FALSE = false;
    boolean TRUE = true;
    String DEFAULT_CLIENT_ID = "admin-cli";
    String DEFAULT_REALM_NAME = "master";
}
