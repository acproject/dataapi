package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.dto.UserDto;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import com.owiseman.dataapi.service.KeycloakUserService;
import com.owiseman.dataapi.service.RoleService;
import com.owiseman.dataapi.util.HttpHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.representations.idm.RoleRepresentation;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/users")
public class UserAdminController {
    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private RoleService roleService;

    @PostMapping("update")
    public ResponseEntity<?> updateUser(@RequestBody UserRepresentation request, HttpServletRequest servletRequest) {
        var token = HttpHeaderUtil.getTokenFromHeader(servletRequest);
        try {
            var user = keycloakUserService.updateUser(request.getId(), request.getEmail(),
                    request.getFirstName(), request.getLastName(),
                    Optional.ofNullable(request.getAttributes()), token);
            return ResponseEntity.ok().body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRecord request, HttpServletRequest servletRequest) {
        var token = HttpHeaderUtil.getTokenFromHeader(servletRequest);
        try {
            var user = keycloakUserService.createUser(request, token);
            return ResponseEntity.ok().body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }

    @GetMapping
    public List<UserRepresentation> getAllUsers(HttpServletRequest servletRequest) {
        var token = HttpHeaderUtil.getTokenFromHeader(servletRequest);
        return keycloakUserService.getUsersResource(token).list();
    }

    @GetMapping("/paginated")
    public PageResult<UserDto> getUsersPaginated(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest servletRequest
    ) {
        var token = HttpHeaderUtil.getTokenFromHeader(servletRequest);
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
                                              HttpServletRequest servletRequest) {
        var token = HttpHeaderUtil.getTokenFromHeader(servletRequest);
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
        var token = HttpHeaderUtil.getTokenFromHeader(servletRequest);
        roleService.assignRole(userId, request.getName(), token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId,HttpServletRequest servletRequest) {
        var token = HttpHeaderUtil.getTokenFromHeader(servletRequest);
        keycloakUserService.deleteUserById(userId, token);
    }
}
