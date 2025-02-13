package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysKeycloakClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeycloakClientRepository extends JpaRepository<SysKeycloakClient, String> {
}
