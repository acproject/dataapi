package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysAuditLog;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSAUDITLOG.*;

@Repository
public class SysAuditLogRepository {
    @Autowired
    private DSLContext dslContext;

    // 基础CRUD操作
    public SysAuditLog save(SysAuditLog log) {
        dslContext.insertInto(TABLE)
                .set(ID, log.getId())
                .set(USERID, log.getUserId())
                .set(ACTIONTYPE, log.getActionType())
                .set(TARGETTYPE, log.getTargetType())
                .set(TARGETID, log.getTargetId())
                .set(DETAILS, log.getDetails()) // JSONB字段
                .set(TIMESTAMP, log.getTimestamp())
                .execute();
        return log;
    }

    public void update(SysAuditLog log) {
        dslContext.update(TABLE)
                .set(USERID, log.getUserId())
                .set(ACTIONTYPE, log.getActionType())
                .set(TARGETTYPE, log.getTargetType())
                .set(TARGETID, log.getTargetId())
                .set(DETAILS, log.getDetails())
                .set(TIMESTAMP, log.getTimestamp())
                .where(ID.eq(log.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysAuditLog> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysAuditLog.class);
    }

    public List<SysAuditLog> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysAuditLog.class);
    }

    // 分页基础方法
    public PageResult<SysAuditLog> findAllWithPagination(int pageNumber, int pageSize) {
        return findByConditionWithPagination(DSL.noCondition(), pageNumber, pageSize);
    }

    // 带条件分页（通用）
    public PageResult<SysAuditLog> findByConditionWithPagination(Condition condition,
                                                                 int pageNumber,
                                                                 int pageSize) {
        List<SysAuditLog> logs = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysAuditLog.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(logs, pageNumber, pageSize, total);
    }

    // 扩展方法：按用户查询（带分页+排序）
    public PageResult<SysAuditLog> findByUserWithPagination(String userId,
                                                            int pageNumber,
                                                            int pageSize,
                                                            SortField<?>... sortFields) {
        Condition condition = USERID.eq(userId);

        List<SysAuditLog> logs = dslContext.selectFrom(TABLE)
                .where(condition)
                .orderBy(sortFields)
                .limit(pageSize)
                .offset((pageNumber - 1) * pageSize)
                .fetchInto(SysAuditLog.class);

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(logs, pageNumber, pageSize, total);
    }

    // 时间范围查询（精确到秒）
    public List<SysAuditLog> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return dslContext.selectFrom(TABLE)
                .where(TIMESTAMP.between(start, end))
                .orderBy(TIMESTAMP.desc())
                .fetchInto(SysAuditLog.class);
    }

    // 组合条件查询示例：按操作类型+目标类型
    public PageResult<SysAuditLog> findByActionAndTarget(String actionType,
                                                         String targetType,
                                                         int pageNumber,
                                                         int pageSize) {
        Condition condition = ACTIONTYPE.eq(actionType)
                .and(TARGETTYPE.eq(targetType));

        return findByConditionWithPagination(condition, pageNumber, pageSize);
    }

    // 按操作类型统计数量
    public long countByActionType(String actionType) {
        return dslContext.selectCount()
                .from(TABLE)
                .where(ACTIONTYPE.eq(actionType))
                .fetchOne(0, Long.class);
    }

    // 查询最近N条日志
    public List<SysAuditLog> findRecentLogs(int limit) {
        return dslContext.selectFrom(TABLE)
                .orderBy(TIMESTAMP.desc())
                .limit(limit)
                .fetchInto(SysAuditLog.class);
    }

    // 查询包含特定JSON属性的日志（PostgreSQL jsonb语法）
    public List<SysAuditLog> findByJsonAttribute(String key, String value) {
        return dslContext.selectFrom(TABLE)
                .where(DSL.condition("details @> ?::jsonb",
                        DSL.inline("{\"" + key + "\": \"" + value + "\"}")))
                .fetchInto(SysAuditLog.class);
    }

    // 批量插入（事务管理）
    @Transactional
    public void batchInsert(List<SysAuditLog> logs) {
        logs.forEach(log -> dslContext.insertInto(TABLE)
                .set(ID, log.getId())
                .set(USERID, log.getUserId())
                .set(ACTIONTYPE, log.getActionType())
                .set(TARGETTYPE, log.getTargetType())
                .set(TARGETID, log.getTargetId())
                .set(DETAILS, log.getDetails()) // JSONB字段
                .set(TIMESTAMP, log.getTimestamp())
                .execute());
    }


}
