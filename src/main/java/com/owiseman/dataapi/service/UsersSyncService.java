package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUser;

import com.owiseman.dataapi.repository.SysUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.owiseman.jpa.util.PaginationHelper;

import com.owiseman.dataapi.dto.UserRegistrationRecord;

@Service
public class UsersSyncService {

    @Autowired
    SysUserRepository sysUserRepository;

    public UsersSyncService() {
    }

    @Async
    public void syncUsers(UserRegistrationRecord userRegistrationRecord) {
        assert userRegistrationRecord != null;
        SysUser sysUser = new SysUser(
                userRegistrationRecord.id(),
                userRegistrationRecord.username(),
                userRegistrationRecord.firstname(),
                userRegistrationRecord.lastname(),
                userRegistrationRecord.email(),
                Boolean.valueOf(OAuth2ConstantsExtends.FALSE),
                null,
                System.currentTimeMillis(),
                Boolean.valueOf(OAuth2ConstantsExtends.TRUE)
        );
        sysUserRepository.save(sysUser);
    }

    @Async
    public SysUser getSysUserById(String userId) {
        return sysUserRepository.findById(userId).get();
    }

    @Async
    public SysUser updateUserById(SysUser sysUser) {
        assert sysUser != null;
        if (sysUserRepository.findById(sysUser.getId()).isPresent())
            return    sysUserRepository.save(sysUser);
         else
             throw new RuntimeException("no sysUser exist !");
    }

    @Async
    public void deleteUser(String userId) {
        sysUserRepository.deleteById(userId);
    }

    @Async
    public void enableUser(String userId) {
        sysUserRepository.findById(userId).get().setEnabled(true);
    }

    @Async
    public void disableUser(String userId) {
        sysUserRepository.findById(userId).get().setEnabled(false);
    }
}
