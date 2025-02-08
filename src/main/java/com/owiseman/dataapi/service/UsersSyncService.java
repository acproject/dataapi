package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.entity.SysUser;


import com.owiseman.dataapi.entity.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.owiseman.dataapi.dto.UserRegistrationRecord;

@Service
public class UsersSyncService {

    private final DSLContext dslContext;

    @Autowired
    public UsersSyncService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }
    @Async
    public void syncUsers(UserRegistrationRecord userRegistrationRecord) {
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
        var result = dslContext.insertInto(Tables.SYSUSER.TABLE)
                .set(Tables.SYSUSER.ID, sysUser.getId())
                .set(Tables.SYSUSER.USERNAME, sysUser.getUsername())
                .set(Tables.SYSUSER.FIRSTNAME, sysUser.getFirstName())
                .set(Tables.SYSUSER.LASTNAME, sysUser.getLastName())
                .set(Tables.SYSUSER.EMAIL, sysUser.getEmail())
                .set(Tables.SYSUSER.EMAILVERIFIED, sysUser.getEmailVerified())
                .set(Tables.SYSUSER.ATTRIBUTES, sysUser.getAttributes())
                .set(Tables.SYSUSER.CREATEDTIMESTAMP, sysUser.getCreatedTimestamp())
                .set(Tables.SYSUSER.ENABLED, sysUser.getEnabled())
                .execute();

    }

    public SysUser getSysUserById(String userId) {
      return   dslContext.selectFrom(Tables.SYSUSER.TABLE)
                .where(Tables.SYSUSER.ID.eq(userId))
                .fetchOne().get(0, SysUser.class);

    }

    public SysUser updateUserById(SysUser sysUser) {
         dslContext.update(Tables.SYSUSER.TABLE)
                 .set(Tables.SYSUSER.ID, sysUser.getId())
                .set(Tables.SYSUSER.USERNAME, sysUser.getUsername())
                .set(Tables.SYSUSER.FIRSTNAME, sysUser.getFirstName())
                .set(Tables.SYSUSER.LASTNAME, sysUser.getLastName())
                .set(Tables.SYSUSER.EMAIL, sysUser.getEmail())
                .set(Tables.SYSUSER.EMAILVERIFIED, sysUser.getEmailVerified())
                .set(Tables.SYSUSER.ATTRIBUTES, sysUser.getAttributes())
                .set(Tables.SYSUSER.CREATEDTIMESTAMP, sysUser.getCreatedTimestamp())
                .set(Tables.SYSUSER.ENABLED, sysUser.getEnabled())
                 .where(Tables.SYSUSER.ID.eq(sysUser.getId()))
                 .execute();
         return getSysUserById(sysUser.getId());
    }

    public void deleteUser(String userId) {
        dslContext.deleteFrom(Tables.SYSUSER.TABLE)
                .where(Tables.SYSUSER.ID.eq(userId))
                .execute();
    }

    public void enableUser(String userId) {
       dslContext.update(Tables.SYSUSER.TABLE)
               .set(Tables.SYSUSER.ENABLED, Boolean.TRUE)
               .where(Tables.SYSUSER.ID.eq(userId)).execute();
    }

    public void disableUser(String userId) {
       dslContext.update(Tables.SYSUSER.TABLE)
               .set(Tables.SYSUSER.ENABLED, Boolean.FALSE)
               .where(Tables.SYSUSER.ID.eq(userId)).execute();
    }
}
