package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.ResetPassword;
import com.owiseman.dataapi.dto.UserRegistrationRecord;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    UserRegistrationRecord createUser(UserRegistrationRecord userRegistrationRecord, String token,String clientId);
    UserRepresentation getUserById(String userId, String token);
    void deleteUserById(String userId, String token);
    void emailVerification(String userId, String token);
    UserResource getUsersResourceById(String userId, String token);
    void updatePassword(String userId, String token);
    void updatePassword(ResetPassword resetPassword, String userId, String token);
    UserResource updateUser(String userId, String newEmail, String newFirstName, String newLastName,
                    Optional<Map<String, List<String>>> attributes, String token);
}
