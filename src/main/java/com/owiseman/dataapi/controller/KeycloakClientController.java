package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.KeycloakClientDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.service.KeycloakRealmService;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
//@PreAuthorize("hasRole('ADMIN’, 'USER')")
public class KeycloakClientController {

    @Autowired
    private KeycloakRealmService keycloakRealmService;

//    @PutMapping("/update/{clientId}")
//    public ResponseEntity<KeycloakClientDto> updateClient(
//            @PathVariable String clientId,
//            @RequestBody ClientRepresentation clientRep) {
//        return ResponseEntity.ok(keycloakRealmService.updateClient(clientId, clientRep));
//    }


//    @PostMapping("/create")
//    public ResponseEntity<KeycloakClientDto> createClient(@RequestBody ClientRepresentation clientRepresentation) {
//        return ResponseEntity.ok(keycloakRealmService.createClient(clientRepresentation));
//    }

//    @GetMapping
//    public ResponseEntity<PageResult<ClientRepresentation>> findClientsByPage (
//            @RequestParam(defaultValue = "1") int page,
//        @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(keycloakRealmService.findClientsByPage(page, size));
//    }

    @GetMapping()
    public ResponseEntity<List<ClientRepresentation>> findAllClients(HttpServletRequest servletRequest) {
        return ResponseEntity.ok(keycloakRealmService.findAllClients(getTokenFromHeader(servletRequest)));
    }

    private String getTokenFromHeader(HttpServletRequest servletRequest) {
         String authHeader = servletRequest.getHeader("Authorization");
        String token = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return token;
    }
}
