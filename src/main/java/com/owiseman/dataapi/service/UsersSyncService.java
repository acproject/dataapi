package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.dto.NormSysUserDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUser;

import com.owiseman.dataapi.repository.SysUserRepository;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.owiseman.jpa.util.PaginationHelper;

import com.owiseman.dataapi.dto.UserRegistrationRecord;

@Service
public class UsersSyncService {

    SysUserRepository sysUserRepository;

    public UsersSyncService() {
        sysUserRepository = new SysUserRepository();
    }

    @Async
    public void syncNormUsers(NormSysUserDto normSysUserDto, String realm) {
        assert normSysUserDto != null;
        SysUser sysUser = new SysUser(
                normSysUserDto.getId(),
                normSysUserDto.getUsername(),
                normSysUserDto.getFirstName(),
                normSysUserDto.getLastName(),
                normSysUserDto.getEmail(),
                Boolean.valueOf(OAuth2ConstantsExtends.FALSE),
                normSysUserDto.getAttributes(),
                System.currentTimeMillis(),
                Boolean.valueOf(OAuth2ConstantsExtends.TRUE),
                realm,
                null,
                normSysUserDto.getProjectId()
        );
        sysUserRepository.save(sysUser);
    }

    /***
     * 适用于组织注册时的用户同步
     * @param userRegistrationRecord
     * @param realm
     * @param clientId
     */
    @Async
    public void syncUsers(UserRegistrationRecord userRegistrationRecord, String realm, String clientId) {
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
                Boolean.valueOf(OAuth2ConstantsExtends.TRUE),
                realm,
                clientId,
                null
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
            return sysUserRepository.save(sysUser);
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
