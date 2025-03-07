package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.CreateKeycloakClientDto;
import com.owiseman.dataapi.dto.KeycloakClientDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.service.KeycloakClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.owiseman.dataapi.util.HttpHeaderUtil.getTokenFromHeader;

@RestController
@RequestMapping("/clients")
@PreAuthorize("hasRole('ADMIN’)")
public class KeycloakClientController {

    @Autowired
    private KeycloakClientService keycloakRealmService;

    @PutMapping("/update/{clientId}")
    public ResponseEntity<KeycloakClientDto> updateClient(
            @PathVariable String clientId,
            @RequestBody ClientRepresentation clientRep,
            HttpServletRequest servletRequest
            ) {
        // todo 需要修改
        return ResponseEntity.ok(keycloakRealmService.updateClient(clientId, clientRep, getTokenFromHeader(servletRequest)));
    }


    @PostMapping("/create")
    public ResponseEntity<KeycloakClientDto> createClient(
            @RequestBody CreateKeycloakClientDto createKeycloakClientDto,
            HttpServletRequest servletRequest
            ) {
        return ResponseEntity.ok(
                keycloakRealmService.createClient(createKeycloakClientDto, getTokenFromHeader(servletRequest)));
    }

    @GetMapping("/paginated")
    public ResponseEntity<PageResult<ClientRepresentation>> findClientsByPage (
            @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
            HttpServletRequest servletRequest) {
        return ResponseEntity.ok(keycloakRealmService.findClientsByPage(page, size, getTokenFromHeader(servletRequest)));
    }

    @GetMapping()
    public ResponseEntity<List<ClientRepresentation>> findAllClients(HttpServletRequest servletRequest) {
        return ResponseEntity.ok(keycloakRealmService.findAllClients(getTokenFromHeader(servletRequest)));
    }


}
