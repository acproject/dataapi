package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUser;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSUSER.*;

import java.util.List;

@Repository
public class SysUserRepository {
    @Autowired
    private DSLContext dslContext;

    @Autowired
    public SysUserRepository() {

    }


    public SysUser save(SysUser user) {
        dslContext.insertInto(TABLE)
                .set(ID, user.getId())
                .set(USERNAME, user.getUsername())
                .set(FIRSTNAME, user.getFirstName())
                .set(LASTNAME, user.getLastName())
                .set(EMAIL, user.getEmail())
                .set(EMAILVERIFIED, user.getEmailVerified())
                .set(ATTRIBUTES, user.getAttributes())
                .set(CREATEDTIMESTAMP, user.getCreatedTimestamp())
                .set(ENABLED, user.getEnabled())
                .set(REALMNAME, user.getRealmName())
                .set(CLIENTID, user.getClientId())
                .execute();
        return user;
    }

    public void update(SysUser user) {
        dslContext.update(TABLE)
                .set(USERNAME, user.getUsername())
                .set(FIRSTNAME, user.getFirstName())
                .set(LASTNAME, user.getLastName())
                .set(EMAIL, user.getEmail())
                .set(EMAILVERIFIED, user.getEmailVerified())
                .set(ATTRIBUTES, user.getAttributes())
                .set(ENABLED, user.getEnabled())
                .set(REALMNAME, user.getRealmName())
                .set(CLIENTID, user.getClientId())
                .where(ID.eq(user.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public void deleteByRealmName(String realmName) {
        dslContext.deleteFrom(TABLE)
                .where(REALMNAME.eq(realmName))
                .execute();
    }

    public Optional<SysUser> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysUser.class);
    }

    public Optional<SysUser> findByEmail(String email) {
        return dslContext.selectFrom(TABLE)
                .where(EMAIL.eq(email))
                .fetchOptionalInto(SysUser.class);
    }

    public Optional<SysUser> findByUsername(String username) {
        return dslContext.selectFrom(TABLE)
                .where(USERNAME.eq(username))
                .fetchOptionalInto(SysUser.class);
    }

    public List<SysUser> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysUser.class);
    }

    // 在 SysUserRepository 类中新增分页方法
    public PageResult<SysUser> findAllWithPagination(int pageNumber, int pageSize) {
        // 获取分页数据
        List<SysUser> users = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.noCondition(),
                TABLE.getName(), // 确认 TABLE.getName() 返回正确的表名（如 "sys_user"）
                pageSize,
                pageNumber,
                SysUser.class
        );

        // 获取总记录数（直接查询代替 getTotalPages）
        int total = dslContext.selectCount()
                .from(TABLE)
                .where(DSL.noCondition())
                .fetchOne(0, Integer.class);

        return new PageResult<>(users, pageNumber, pageSize, total);
    }

    // 带条件的分页查询（扩展方法）
    public PageResult<SysUser> findByConditionWithPagination(Condition condition, int pageNumber, int pageSize) {
        List<SysUser> users = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUser.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(users, pageNumber, pageSize, total);
    }

    public PageResult<SysUser> findByRealmNameWithPagination(String realmName, int pageNumber, int pageSize) {
        Condition condition = REALMNAME.eq(realmName);
        
        List<SysUser> users = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUser.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(users, pageNumber, pageSize, total);
    }

    public void updateUserStatus(String userId, boolean enabled) {
        dslContext.update(TABLE)
                .set(ENABLED, enabled)
                .where(ID.eq(userId))
                .execute();
    }
}
