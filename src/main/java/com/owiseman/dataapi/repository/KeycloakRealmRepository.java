package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.entity.SysKeycloakRealm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeycloakRealmRepository extends JpaRepository<SysKeycloakRealm, String> {
}
