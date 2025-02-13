package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.OAuth2ConstantsExtends;
import com.owiseman.dataapi.entity.SysUser;


//import com.owiseman.dataapi.entity.Tables;

import com.owiseman.dataapi.repository.SysUserRepository;
import org.jooq.DSLContext;
//import org.jooq.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.owiseman.dataapi.dto.UserRegistrationRecord;

@Service
public class UsersSyncService {

//    private final DSLContext dslContext;
//
//    @Autowired
//    public UsersSyncService(DSLContext dslContext) {
//        this.dslContext = dslContext;
//    }

    @Autowired
    SysUserRepository sysUserRepository;

//    public UsersSyncService(DSLContext dslContext) {
//        this.dslContext = dslContext;
//    }

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
//        var result = dslContext.insertInto(Tables.SYSUSER.TABLE)
//                .set(Tables.SYSUSER.ID, sysUser.getId())
//                .set(Tables.SYSUSER.USERNAME, sysUser.getUsername())
//                .set(Tables.SYSUSER.FIRSTNAME, sysUser.getFirstName())
//                .set(Tables.SYSUSER.LASTNAME, sysUser.getLastName())
//                .set(Tables.SYSUSER.EMAIL, sysUser.getEmail())
//                .set(Tables.SYSUSER.EMAILVERIFIED, sysUser.getEmailVerified())
//                .set(Tables.SYSUSER.ATTRIBUTES, sysUser.getAttributes())
//                .set(Tables.SYSUSER.CREATEDTIMESTAMP, sysUser.getCreatedTimestamp())
//                .set(Tables.SYSUSER.ENABLED, sysUser.getEnabled())
//                .execute();
        sysUserRepository.save(sysUser);
    }

    @Async
    public SysUser getSysUserById(String userId) {
//      return   dslContext.selectFrom(Tables.SYSUSER.TABLE)
//                .where(Tables.SYSUSER.ID.eq(userId))
//                .fetchOne().get(0, SysUser.class);
        return sysUserRepository.findById(userId).get();

    }

    @Async
    public SysUser updateUserById(SysUser sysUser) {
//         dslContext.update(Tables.SYSUSER.TABLE)
//                 .set(Tables.SYSUSER.ID, sysUser.getId())
//                .set(Tables.SYSUSER.USERNAME, sysUser.getUsername())
//                .set(Tables.SYSUSER.FIRSTNAME, sysUser.getFirstName())
//                .set(Tables.SYSUSER.LASTNAME, sysUser.getLastName())
//                .set(Tables.SYSUSER.EMAIL, sysUser.getEmail())
//                .set(Tables.SYSUSER.EMAILVERIFIED, sysUser.getEmailVerified())
//                .set(Tables.SYSUSER.ATTRIBUTES, sysUser.getAttributes())
//                .set(Tables.SYSUSER.CREATEDTIMESTAMP, sysUser.getCreatedTimestamp())
//                .set(Tables.SYSUSER.ENABLED, sysUser.getEnabled())
//                 .where(Tables.SYSUSER.ID.eq(sysUser.getId()))
//                 .execute();
//         return getSysUserById(sysUser.getId());
        assert sysUser != null;
        if (sysUserRepository.findById(sysUser.getId()).isPresent())
            return    sysUserRepository.save(sysUser);
         else
             throw new RuntimeException("no sysUser exist !");
    }

    @Async
    public void deleteUser(String userId) {
//        dslContext.deleteFrom(Tables.SYSUSER.TABLE)
//                .where(Tables.SYSUSER.ID.eq(userId))
//                .execute();
        sysUserRepository.deleteById(userId);
    }

    @Async
    public void enableUser(String userId) {
//       dslContext.update(Tables.SYSUSER.TABLE)
//               .set(Tables.SYSUSER.ENABLED, Boolean.TRUE)
//               .where(Tables.SYSUSER.ID.eq(userId)).execute();
        sysUserRepository.findById(userId).get().setEnabled(true);
    }

    @Async
    public void disableUser(String userId) {
//       dslContext.update(Tables.SYSUSER.TABLE)
//               .set(Tables.SYSUSER.ENABLED, Boolean.FALSE)
//               .where(Tables.SYSUSER.ID.eq(userId)).execute();
        sysUserRepository.findById(userId).get().setEnabled(false);
    }
}
