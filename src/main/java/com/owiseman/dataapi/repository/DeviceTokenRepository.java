package com.owiseman.dataapi.repository;

public interface DeviceTokenRepository {
    boolean isValidToken(String token);
    void invalidateToken(String token);
    void updateLastUsed(String token);
}
