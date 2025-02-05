package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.ResetPassword;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord, String token);
    UserRepresentation getUserById(String userId, String token);
    void deleteUserById(String userId, String token);
    void emailVerification(String userId, String token);
    UserResource getUsersResourceById(String userId, String token);
    void updatePassword(String userId, String token);
    void updatePassword(ResetPassword resetPassword, String userId, String token);
}
