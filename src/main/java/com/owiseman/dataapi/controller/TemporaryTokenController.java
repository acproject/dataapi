package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.TokenResponse;
import com.owiseman.dataapi.service.KeycloakTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("temporary/api")
public class TemporaryTokenController {
    @Autowired
    private KeycloakTokenService tokenService;
    @GetMapping("/token")
    public TokenResponse getToken() {
        return tokenService.getTokenResponse();
    }
}
