package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.dto.UserDto;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import com.owiseman.dataapi.service.KeycloakUserService;
import com.owiseman.dataapi.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.representations.idm.RoleRepresentation;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {
    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRecord request, HttpServletRequest servletRequest){
         String authHeader = servletRequest.getHeader("Authorization");
         String token = "";
           if (authHeader !=null && authHeader.startsWith("Bearer ")) {
               token = authHeader.substring(7);
           }
           try {
               var user = keycloakUserService.createUser(request, token);
               return ResponseEntity.ok().body(user);
           } catch (RuntimeException e) {
               return ResponseEntity.badRequest().body(e.toString());
           }
    }

    @GetMapping
    public List<UserRepresentation> getAllUsers(HttpServletRequest servletRequest){
        String authHeader = servletRequest.getHeader("Authorization");
         String token = "";
           if (authHeader !=null && authHeader.startsWith("Bearer ")) {
               token = authHeader.substring(7);
           }
           return keycloakUserService.getUsersResource(token).list();
    }

    @GetMapping("/paginated")
    public PageResult<UserDto> getUsersPaginated(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest servletRequest
    ) {
        String authHeader = servletRequest.getHeader("Authorization");
         String token = "";
           if (authHeader !=null && authHeader.startsWith("Bearer ")) {
               token = authHeader.substring(7);
           }
           int first = (page - 1) * size;
           int max = size;
           int total = keycloakUserService.getUsersResource(token).count();
           List<UserRepresentation> users = keycloakUserService.getUsersResource(token).search(null, first, max);
           List<UserDto> userDtos = users.stream().map(UserDto::from).toList();

           return new PageResult<>(userDtos, page, size, total);
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable String userId,
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
