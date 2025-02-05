package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.UserRegistrationRecord;
import com.owiseman.dataapi.service.KeycloakUserService;
import com.owiseman.dataapi.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.representations.idm.RoleRepresentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/admin/users")
public class UserAdminController {
    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationRecord request, HttpServletRequest servletRequest){
         String authHeader = servletRequest.getHeader("Authorization");
         String token = "";
           if (authHeader !=null && authHeader.startsWith("Bearer ")) {
               token = authHeader.substring(7);
           }
        var username = keycloakUserService.createUser(request, token).username();
        return ResponseEntity.created(URI.create("/admin/users/" + username)).build();
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable String userId,
                                                   @RequestParam Boolean enabled,
                                                   HttpServletRequest servletRequest){
        String authHeader = servletRequest.getHeader("Authorization");
         String token = "";
           if (authHeader !=null && authHeader.startsWith("Bearer ")) {
               token = authHeader.substring(7);
           }
        if (enabled) {
            keycloakUserService.enableUser(userId, token);
        } else {
            keycloakUserService.disableUser(userId, token);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> assignRole(@PathVariable String userId,
                                           @RequestBody RoleRepresentation request,
                                           HttpServletRequest servletRequest) {
         String authHeader = servletRequest.getHeader("Authorization");
         String token = "";
           if (authHeader !=null && authHeader.startsWith("Bearer ")) {
               token = authHeader.substring(7);
           }
           roleService.assignRole(userId, request.getName(), token);
        return ResponseEntity.noContent().build();
    }
}
