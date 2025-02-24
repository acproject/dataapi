package com.owiseman.dataapi.service;

import com.owiseman.dataapi.entity.SysKeycloakClient;
import com.owiseman.dataapi.entity.SysKeycloakRealm;
import com.owiseman.dataapi.entity.SysUser;
import com.owiseman.dataapi.repository.KeycloakClientRepository;
import com.owiseman.dataapi.repository.KeycloakRealmRepository;
import com.owiseman.dataapi.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class KeycloakSyncService {
    private final KeycloakClientRepository keycloakClientRepository;
    private final KeycloakRealmRepository keycloakRealmRepository;
    private final SysUserRepository sysUserRepository;

    @Autowired
    public KeycloakSyncService(KeycloakRealmRepository keycloakRealmRepository,
                              KeycloakClientRepository keycloakClientRepository,
                              SysUserRepository sysUserRepository) {
        this.keycloakRealmRepository = keycloakRealmRepository;
        this.keycloakClientRepository = keycloakClientRepository;
        this.sysUserRepository = sysUserRepository;
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
            keycloakClientRepository.deleteByIdOrClientId(clientId);
        }

    }

    @Async
    public void syncDeleteRealm(String realmName) {
        if (!ObjectUtils.isEmpty(realmName)) {
            keycloakRealmRepository.deleteByIdOrName(realmName);
        }

    }

    @Async
    public void syncUser(SysUser user) {
        if (!ObjectUtils.isEmpty(user)) {
            // 检查用户是否存在
            sysUserRepository.findById(user.getId())
                    .ifPresentOrElse(
                            existingUser -> {
                                // 如果用户存在，更新用户信息
                                sysUserRepository.update(user);
                            },
                            () -> {
                                // 如果用户不存在，创建新用户
                                sysUserRepository.save(user);
                            }
                    );
        }
    }

    @Async
    public void syncDeleteUser(String userId) {
        if (!ObjectUtils.isEmpty(userId)) {
            sysUserRepository.deleteById(userId);
        }
    }
}
