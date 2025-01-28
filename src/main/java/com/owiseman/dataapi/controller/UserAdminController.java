package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.UserCreateRequest;
import com.owiseman.dataapi.service.KeycloakUserService;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN'， 'SUPER_USER')")
public class UserAdminController {
    @Autowired
    private KeycloakUserService keycloakUserService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreateRequest request){
        String userId = keycloakUserService.createUser(request);
        return ResponseEntity.created(URI.create("/admin/users/" + userId)).build();
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable String userId, @RequestParam Boolean enabled){
        if (enabled) {
            keycloakUserService.enableUser(userId);
        } else {
            keycloakUserService.disableUser(userId);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> assignRole(@PathVariable String userId, @RequestBody RoleRepresentation request) {
        keycloakUserService.assignRole(userId, request.getName());
        return ResponseEntity.noContent().build();
    }
}
