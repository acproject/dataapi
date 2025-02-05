package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.TokenResponse;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class KeycloakTokenService {
    @Value("${keycloak.urls.token}")
    private String tokenUrl;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.user-info}")
    private String userInfo;

    private final RestTemplate restTemplate;

    public KeycloakTokenService() {
        this.restTemplate = new RestTemplate();
    }

    public TokenResponse getTokenResponse(Optional<String> username, Optional<String> password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", OAuth2Constants.CLIENT_CREDENTIALS);
        formData.add("username", username.isEmpty()?  OAuth2ConstantsExtends.USER_ADMIN : username.get());
        formData.add("password", password.isEmpty()? userInfo : password.get());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(formData, headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                tokenUrl,
                request,
                TokenResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get access token:" + response.getStatusCode());
        }

    }


}
