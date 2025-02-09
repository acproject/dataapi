package com.owiseman.dataapi.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Repository
public class SysDeviceTokenRepository implements DeviceTokenRepository{

    @PersistenceContext
    private EntityManager em;
    @Override
    public boolean isValidToken(String token) {
        return em.createQuery(
                "SELECT t.active FROM SysDeviceToken t WHERE t.token = : token", Boolean.class
        )
                .setParameter("token", token)
                .getSingleResult();
    }

    @Override
    @Transactional
    public void invalidateToken(String token) {
        em.createQuery("UPDATE SysDeviceToken t SET t.active = false WHERE t.token = : token")
                .setParameter("token", token)
                .executeUpdate();
    }

    @Override
    public void updateLastUsed(String token) {
        em.createQuery("UPDATE SysDeviceToken t SET t.lastActive := lastUsed WHERE t.token = :token")
                .setParameter("token", token)
                .setParameter("lastActive", LocalDateTime.now())
                .executeUpdate();
    }
}
