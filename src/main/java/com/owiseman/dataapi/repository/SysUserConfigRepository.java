package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.util.JooqContextHolder;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.owiseman.dataapi.entity.Tables.SYSUSERCONFIG.*;

@Repository
public class SysUserConfigRepository {

    private final DSLContext dslContext;

    @Autowired
    public SysUserConfigRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    public SysUserConfig save(SysUserConfig config) {
        if (config.getId() == null || config.getId().isEmpty()) {
            config.setId(UUID.randomUUID().toString());
        }
        dslContext.insertInto(TABLE)
                .set(dslContext.newRecord(TABLE, config))  // 自动映射同名字段
                .execute();
        return config;
    }

    public void update(SysUserConfig config) {
        // 先加载原始记录
        var record = dslContext.fetchOne(TABLE, ID.eq(config.getId()));
        record.from(config);
        record.changed(config.getId(), false);
        dslContext.update(TABLE)
                .set(record)
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysUserConfig> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysUserConfig.class);
    }

    public Optional<SysUserConfig> findByUserId(String userId) {
        return dslContext.selectFrom(TABLE)
                .where(USERID.eq(userId))
                .fetchOptionalInto(SysUserConfig.class);
    }

    public Optional<SysUserConfig> findByIdAndUserId(String id, String userId) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .and(USERID.eq(userId))
                .fetchOptionalInto(SysUserConfig.class);
    }


    public List<SysUserConfig> findAll(String realmName) {
        return dslContext.selectFrom(TABLE).where(KEYCLOAKREALM.eq(realmName))
                .fetchInto(SysUserConfig.class);
    }

    public PageResult<SysUserConfig> findAllWithPagination(int pageNumber, int pageSize, String realmName) {
        List<SysUserConfig> configs = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.condition(KEYCLOAKREALM.eq(realmName)),
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserConfig.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(DSL.condition(KEYCLOAKREALM.eq(realmName)))
                .fetchOne(0, Integer.class);

        return new PageResult<>(configs, pageNumber, pageSize, total);
    }

    public Boolean existsByProjectName(String projectName) {
        try {
            return dslContext.selectFrom(TABLE)
                    .where(PROJECTNAME.eq(projectName))
                    .fetchOne(0, SysUserConfig.class) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public PageResult<SysUserConfig> findByConditionWithPagination(Condition condition, int pageNumber, int pageSize) {
        List<SysUserConfig> configs = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserConfig.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(configs, pageNumber, pageSize, total);
    }

    public void deleteByUserId(String userId) {
        dslContext.deleteFrom(TABLE)
                .where(USERID.eq(userId))
                .execute();
    }
}