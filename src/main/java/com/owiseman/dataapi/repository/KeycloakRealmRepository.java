package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysKeycloakRealm;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.owiseman.dataapi.entity.Tables.SYSKEYCLOAKREALM.*;

@Repository
public class KeycloakRealmRepository {

    private final DSLContext dslContext;

    @Autowired
    public KeycloakRealmRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    // 基础分页查询
    public PageResult<SysKeycloakRealm> findAllWithPagination(int pageNumber, int pageSize) {
        return findByConditionWithPagination(DSL.noCondition(), pageNumber, pageSize);
    }

    // 带条件分页（通用）
    public PageResult<SysKeycloakRealm> findByConditionWithPagination(Condition condition,
                                                                     int pageNumber,
                                                                     int pageSize) {
        List<SysKeycloakRealm> realms = PaginationHelper.getPaginatedData(
            dslContext,
            condition,
            TABLE.getName(),
            pageSize,
            pageNumber,
            SysKeycloakRealm.class
        );

        int total = dslContext.selectCount()
            .from(TABLE)
            .where(condition)
            .fetchOne(0, Integer.class);

        return new PageResult<>(realms, pageNumber, pageSize, total);
    }

    // 带排序的分页查询
    public PageResult<SysKeycloakRealm> findAllWithSorting(int pageNumber,
                                                          int pageSize,
                                                          SortField<?>... sortFields) {
        List<SysKeycloakRealm> realms = dslContext.selectFrom(TABLE)
            .orderBy(sortFields)
            .limit(pageSize)
            .offset((pageNumber - 1) * pageSize)
            .fetchInto(SysKeycloakRealm.class);

        int total = dslContext.selectCount()
            .from(TABLE)
            .fetchOne(0, Integer.class);

        return new PageResult<>(realms, pageNumber, pageSize, total);
    }

    // 扩展方法：按领域名称搜索
    public PageResult<SysKeycloakRealm> searchByRealmName(String keyword,
                                                        int pageNumber,
                                                        int pageSize) {
        Condition condition = REALM.likeIgnoreCase("%" + keyword + "%");
        return findByConditionWithPagination(condition, pageNumber, pageSize);
    }

    // 扩展方法：按启用状态分页
    public PageResult<SysKeycloakRealm> findByEnabledStatus(boolean enabled,
                                                           int pageNumber,
                                                           int pageSize) {
        return findByConditionWithPagination(ENABLED.eq(enabled), pageNumber, pageSize);
    }

    // 原有方法增强
    public SysKeycloakRealm findById(String id) {
        try {
            return (SysKeycloakRealm) dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysKeycloakRealm.class)
                .orElseThrow(() -> new IllegalArgumentException("Realm not found"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void save(SysKeycloakRealm realm) {
        // 完整字段保存示例（根据表结构调整）
        dslContext.insertInto(TABLE)
            .set(ID, realm.getId())
            .set(REALM, realm.getRealm())
            .set(ENABLED, realm.getEnabled())
            .set(ACCESSTOKENLIFESPAN, realm.getAccessTokenLifespan())
            .set(SSOSESSIONMAXLIFESPAN, realm.getSsoSessionMaxLifespan())
            .set(SSLREQUIRED, realm.getSslRequired())
            // 添加其他字段...
            .execute();
    }

    @Transactional
    public void update(SysKeycloakRealm realm) {
        // 完整字段更新示例
        dslContext.update(TABLE)
            .set(REALM, realm.getRealm())
            .set(ENABLED, realm.getEnabled())
            .set(ACCESSTOKENLIFESPAN, realm.getAccessTokenLifespan())
            .set(SSOSESSIONMAXLIFESPAN, realm.getSsoSessionMaxLifespan())
            .set(SSLREQUIRED, realm.getSslRequired())
            // 更新其他字段...
            .where(ID.eq(realm.getId()))
            .execute();
    }

    // 新增批量操作
    @Transactional
    public void batchUpdateTokenLifespan(int newLifespan, List<String> realmIds) {
        dslContext.batchUpdate(
                (UpdatableRecord<?>) realmIds.stream()
                    .map(id -> dslContext.update(TABLE)
                        .set(ACCESSTOKENLIFESPAN, newLifespan)
                        .where(ID.eq(id)))
                    .collect(Collectors.toList())
        ).execute();
    }
}
