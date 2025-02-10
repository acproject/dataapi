package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.LoginRequest;
import com.owiseman.dataapi.service.KeycloakTokenService;
import com.owiseman.dataapi.service.KeycloakUserService;

import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.admin.client.Keycloak;

import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class AuthController {

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private KeycloakTokenService keycloakTokenService;

     @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

     @Value("${keycloak.urls.token}")
    private String serverUrl;
    @Value("${keycloak.realm}")
    private String realm;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest){
       try{
           String authHeader = servletRequest.getHeader("Authorization");
           if (authHeader !=null && authHeader.startsWith("Bearer ")) {
               request.setToken(authHeader.substring(7));
           }
           HttpHeaders headers = new HttpHeaders();
           headers.setBearerAuth(request.getToken());

           var user = keycloakUserService.authenticate(request.getUsername(), request.getPassword(),
            request.getToken());
            return ResponseEntity
                    .ok().headers(headers).body(user);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LoginRequest request, HttpServletRequest servletRequest){

       String token ="";
        String authHeader = servletRequest.getHeader("Authorization");
           if (authHeader !=null && authHeader.startsWith("Bearer ")) {
              token=authHeader.substring(7);
           }
       Keycloak keycloak = KeycloakBuilder.builder()
               .serverUrl(serverUrl)
               .realm(realm)
               .clientId(clientId)
               .clientSecret(clientSecret)
               .username(request.getUsername())
               .password(request.getPassword())
               .authorization(token)
               .build();
       keycloak.close();
       return ResponseEntity.ok().build();
    }
}
