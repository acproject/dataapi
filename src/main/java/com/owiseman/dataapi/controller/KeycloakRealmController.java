package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.service.KeycloakRealmService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.owiseman.dataapi.util.HttpHeaderUtil.getTokenFromHeader;
@RestController
@RequestMapping("/realms")
@PreAuthorize("hasRole('ADMIN’)")
public class KeycloakRealmController {
    @Autowired
    private KeycloakRealmService keycloakRealmService;


}
