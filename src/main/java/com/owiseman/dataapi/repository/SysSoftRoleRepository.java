package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysSoftRole;
import com.owiseman.dataapi.util.JooqContextHolder;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.owiseman.dataapi.entity.Tables.SYSSOFTROLE.*;

@Repository
public class SysSoftRoleRepository {
    private final DSLContext dslContext;

    @Autowired
    public SysSoftRoleRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    // 基础CRUD操作
    public SysSoftRole save(SysSoftRole role) {
        if (role.getId() == null) {
            role.setId(UUID.randomUUID().toString());
        }
        if (!findByNameAndRealm(role.getRoleName(), role.getRealm()).isEmpty()) {
            throw new RuntimeException("角色名称已存在");
        }

        dslContext.insertInto(TABLE)
                .set(ID, role.getId())
                .set(ROLENAME, role.getRoleName())
                .set(ROLEDESCRIPTION, role.getRoleDescription())
                .set(ROLECODE, role.getRoleCode())
                .set(ISACTIVE, role.getActive())
                .set(REALM, role.getRealm())
                .execute();
        return role;
    }

    public SysSoftRole update(SysSoftRole role) {
        dslContext.update(TABLE)
                .set(ROLENAME, role.getRoleName())
                .set(ROLEDESCRIPTION, role.getRoleDescription())
                .set(ROLECODE, role.getRoleCode())
                .set(ISACTIVE, role.getActive())
                .set(REALM, role.getRealm())
                .where(ID.eq(role.getId()))
                .execute();
        return role;
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysSoftRole> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysSoftRole.class);
    }

    public Optional<SysSoftRole> findByRoleCode(String roleCode) {
        return dslContext.selectFrom(TABLE)
                .where(ROLECODE.eq(roleCode))
                .fetchOptionalInto(SysSoftRole.class);
    }

    public Optional<SysSoftRole> findByNameAndRealm(String name, String realm) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(name)).and(REALM.eq(realm))
                .fetchOptionalInto(SysSoftRole.class);
    }

    public List<SysSoftRole> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysSoftRole.class);
    }

    // 分页基础方法
    public PageResult<SysSoftRole> findAllWithPagination(String realm ,int pageNumber, int pageSize) {
        var condition = REALM.eq(realm);
        return findByConditionWithPagination(condition, pageNumber, pageSize);
    }

    // 带条件分页（通用）
    public PageResult<SysSoftRole> findByConditionWithPagination(Condition condition,
                                                                 int pageNumber,
                                                                 int pageSize) {
        List<SysSoftRole> roles = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysSoftRole.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(roles, pageNumber, pageSize, total);
    }

    // 扩展方法：按角色名称查询（带分页+排序）
    public PageResult<SysSoftRole> findByRoleNameWithPagination(String roleName,
                                                               int pageNumber,
                                                               int pageSize,
                                                               SortField<?>... sortFields) {
        Condition condition = ROLENAME.eq(roleName);

        List<SysSoftRole> roles = dslContext.selectFrom(TABLE)
                .where(condition)
                .orderBy(sortFields)
                .limit(pageSize)
                .offset((pageNumber - 1) * pageSize)
                .fetchInto(SysSoftRole.class);

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(roles, pageNumber, pageSize, total);
    }

    // 按是否激活状态统计数量
    public long countByIsActive(Boolean isActive) {
        return dslContext.selectCount()
                .from(TABLE)
                .where(ISACTIVE.eq(isActive))
                .fetchOne(0, Long.class);
    }

    // 批量插入（事务管理）
    @Transactional
    public void batchInsert(List<SysSoftRole> roles) {
        roles.forEach(role -> dslContext.insertInto(TABLE)
                .set(ID, role.getId())
                .set(ROLENAME, role.getRoleName())
                .set(ROLEDESCRIPTION, role.getRoleDescription())
                .set(ROLECODE, role.getRoleCode())
                .set(ISACTIVE, role.getActive())
                .execute());
    }
}
