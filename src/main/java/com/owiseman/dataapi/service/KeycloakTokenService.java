package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.TokenResponse;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class KeycloakTokenService {
    @Value("${keycloak.urls.token}")
    private String tokenUrl;

    @Value("${keycloak.resource}")
    private String clientId;

//    @Value("${keycloak.credentials.secret}")
//    private String clientSecret;

    @Value("${keycloak.client-info}")
    private String userInfo;

    private final RestTemplate restTemplate;

    public KeycloakTokenService() {
        this.restTemplate = new RestTemplate();
    }

    public TokenResponse getTokenResponse(Optional<String> username, Optional<String> password, Optional<String> clientSecret, Optional<String> grantType) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        if (!clientSecret.isEmpty()) formData.add("client_secret", clientSecret.get());
        formData.add("grant_type", grantType.isEmpty()? OAuth2ConstantsExtends.CLIENT_CREDENTIALS : grantType.get());
        formData.add("username", username.isEmpty()?  OAuth2ConstantsExtends.ADMIN : username.get());
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

    public TokenResponse getTokenResponse() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
//        formData.add("client_secret", clientSecret);
        formData.add("grant_type", OAuth2ConstantsExtends.CLIENT_CREDENTIALS);
        formData.add("username", OAuth2ConstantsExtends.ADMIN);
        formData.add("password", userInfo);
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


    public String getTokenByUsernameAndPassword(String username, String password, String realm) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("grant_type", OAuth2Constants.PASSWORD);
        formData.add("username", username);
        formData.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        // 替换 URL 中的 realm
        String realmTokenUrl = tokenUrl.replace("/realms/master/", "/realms/" + realm + "/");

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                realmTokenUrl,
                request,
                TokenResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getAccessToken();
        } else {
            throw new RuntimeException("认证失败: " + response.getStatusCode());
        }
    }



}
