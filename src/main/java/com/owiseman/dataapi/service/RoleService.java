package com.owiseman.dataapi.service;

public interface RoleService {
    void assignRole(String userId, String roleName, String token);
}
