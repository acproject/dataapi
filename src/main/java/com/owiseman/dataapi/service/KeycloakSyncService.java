package com.owiseman.dataapi.service;

import com.owiseman.dataapi.entity.SysKeycloakClient;
import com.owiseman.dataapi.entity.SysKeycloakRealm;
import com.owiseman.dataapi.repository.KeycloakClientRepository;
import com.owiseman.dataapi.repository.KeycloakRealmRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class KeycloakSyncService {
    private final KeycloakClientRepository keycloakClientRepository;
    private final KeycloakRealmRepository keycloakRealmRepository;


    @Autowired
    public KeycloakSyncService(KeycloakRealmRepository keycloakRealmRepository,
                               KeycloakClientRepository keycloakClientRepository
    ) {
        this.keycloakRealmRepository = keycloakRealmRepository;
        this.keycloakClientRepository = keycloakClientRepository;
    }

    @Async
    public void syncClient(SysKeycloakClient sysKeycloakClients) {
        if (!ObjectUtils.isEmpty(sysKeycloakClients)) {
            keycloakClientRepository.save(sysKeycloakClients);
        }

    }

    @Async
    public void syncRealm(SysKeycloakRealm sysKeycloakRealms) {
        if (!ObjectUtils.isEmpty(sysKeycloakRealms)) {
            sysKeycloakRealms.setRealm(sysKeycloakRealms.getRealm());
            keycloakRealmRepository.save(sysKeycloakRealms);
        }

    }

    @Async
    public void syncDeleteClient(String clientId) {
        if (!ObjectUtils.isEmpty(clientId)) {
            keycloakClientRepository.deleteById(clientId);
        }

    }

    @Async
    public void syncDeleteRealm(String realmName) {
        if (!ObjectUtils.isEmpty(realmName)) {
            keycloakRealmRepository.deleteById(realmName);
        }

    }

}
