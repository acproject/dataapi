package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.TokenResponse;
import com.owiseman.dataapi.repository.KeycloakClientRepository;
import com.owiseman.dataapi.repository.SysUserRepository;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.security.Key;
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

    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    KeycloakClientRepository keycloakClientRepository;

//    private final RestTemplate restTemplate;
//
//    public KeycloakTokenService() {
//        this.restTemplate = new RestTemplate();
//    }

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
        RestTemplate restTemplate = new RestTemplate();
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
        formData.add("grant_type", OAuth2ConstantsExtends.PASSWORD);
        formData.add("username", OAuth2ConstantsExtends.ADMIN);
        formData.add("password", userInfo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(formData, headers);
        RestTemplate restTemplate = new RestTemplate();
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
        // 获得client Id 和client Secret
        String clinetId = sysUserRepository.findByUsername(username).get().getClientId();
        String clientSecret = keycloakClientRepository.findByClientId(clinetId).get().getSecret();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clinetId);
        formData.add("grant_type", OAuth2Constants.PASSWORD);
        formData.add("username", username);
        formData.add("password", password);
        formData.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        // 替换 URL 中的 realm
        String realmTokenUrl = tokenUrl.replace("/realms/master/", "/realms/" + realm + "/");
        RestTemplate restTemplate = new RestTemplate();
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


    /**
     * 获取指定用户的token
     * 
     * @param username 用户名
     * @param password 密码
     * @param realm 领域名称
     * @return token响应
     */
    public TokenResponse getTokenForMasterAdminUser(String username, String password, String realm) {

        
        // 如果不是master realm，需要修改URL
        if (!"master".equals(realm)) {
            tokenUrl = tokenUrl.replace("/realms/master/", "/realms/" + realm + "/");
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("username", username);
        map.add("password", password);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                    tokenUrl, 
                    request, 
                    TokenResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("认证失败: " + e.getMessage());
        }
    }



}
