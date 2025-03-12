package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.KeycloakRealmDto;
import com.owiseman.dataapi.service.KeycloakAdminUtils;
import com.owiseman.dataapi.service.KeycloakRealmService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.owiseman.dataapi.util.HttpHeaderUtil.getTokenFromHeader;

@RestController
@RequestMapping("/realms")
public class KeycloakRealmController {

    @Autowired
    KeycloakRealmService keycloakRealmService;
    @PostMapping()
    public void createRealm(@RequestBody KeycloakRealmDto keycloakRealmDto, HttpServletRequest servletRequest) {
        String token = getTokenFromHeader(servletRequest);
        if (keycloakRealmDto.getEnabled() != null)
            keycloakRealmService.createRealm(keycloakRealmDto.getName(), token);
    }

    @DeleteMapping("/{projectId}")
    public void deleteRealm(@PathVariable String realmName, HttpServletRequest servletRequest) {
        String token = getTokenFromHeader(servletRequest);
        keycloakRealmService.deleteRealmByName(realmName, token);
    }

}
