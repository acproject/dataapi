package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysFilePermission;
import com.owiseman.dataapi.util.JooqContextHolder;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSFILEPERMISSION.*;

@Repository
public class SysFilePermissionRepository {
    private final DSLContext dslContext;

    @Autowired
    public SysFilePermissionRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    public SysFilePermission save(SysFilePermission permission) {
        dslContext.insertInto(TABLE)
                .set(ID, permission.getId())
                .set(FILEID, permission.getFileId())
                .set(USERID, permission.getUserId())
                .set(CANREAD, permission.isCanRead())
                .set(CANWRITE, permission.isCanWrite())
                .set(CANDELETE, permission.isCanDelete())
                .set(CANSHARE, permission.isCanShare())
                .execute();
        return permission;
    }

    public void update(SysFilePermission permission) {
        dslContext.update(TABLE)
                .set(FILEID, permission.getFileId())
                .set(USERID, permission.getUserId())
                .set(CANREAD, permission.isCanRead())
                .set(CANWRITE, permission.isCanWrite())
                .set(CANDELETE, permission.isCanDelete())
                .set(CANSHARE, permission.isCanShare())
                .where(ID.eq(permission.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysFilePermission> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysFilePermission.class);
    }

    public List<SysFilePermission> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysFilePermission.class);
    }

    public PageResult<SysFilePermission> findAllWithPagination(int pageNumber, int pageSize) {
        List<SysFilePermission> permissions = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.noCondition(),
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysFilePermission.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(DSL.noCondition())
                .fetchOne(0, Integer.class);

        return new PageResult<>(permissions, pageNumber, pageSize, total);
    }

    // 根据文件ID查询权限
    public List<SysFilePermission> findByFileId(String fileId) {
        return dslContext.selectFrom(TABLE)
                .where(FILEID.eq(fileId))
                .fetchInto(SysFilePermission.class);
    }

    // 根据用户ID查询权限
    public List<SysFilePermission> findByUserId(String userId) {
        return dslContext.selectFrom(TABLE)
                .where(USERID.eq(userId))
                .fetchInto(SysFilePermission.class);
    }

    // 查询特定用户对特定文件的权限
    public Optional<SysFilePermission> findByFileIdAndUserId(String fileId, String userId) {
        return dslContext.selectFrom(TABLE)
                .where(FILEID.eq(fileId)
                        .and(USERID.eq(userId)))
                .fetchOptionalInto(SysFilePermission.class);
    }

    // 分页查询用户的文件权限
    public PageResult<SysFilePermission> findByUserIdWithPagination(String userId, int pageNumber, int pageSize) {
        Condition condition = USERID.eq(userId);

        List<SysFilePermission> permissions = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysFilePermission.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(permissions, pageNumber, pageSize, total);
    }

    // 分页查询文件的权限列表
    public PageResult<SysFilePermission> findByFileIdWithPagination(String fileId, int pageNumber, int pageSize) {
        Condition condition = FILEID.eq(fileId);

        List<SysFilePermission> permissions = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysFilePermission.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(permissions, pageNumber, pageSize, total);
    }

    @Transactional
    public void batchInsert(List<SysFilePermission> permissions) {
        permissions.forEach(this::save);
    }

    // 删除文件的所有权限
    public void deleteByFileId(String fileId) {
        dslContext.deleteFrom(TABLE)
                .where(FILEID.eq(fileId))
                .execute();
    }

    // 删除用户的所有权限
    public void deleteByUserId(String userId) {
        dslContext.deleteFrom(TABLE)
                .where(USERID.eq(userId))
                .execute();
    }

    // 删除特定用户对特定文件的权限
    public void deleteByFileIdAndUserId(String fileId, String userId) {
        dslContext.deleteFrom(TABLE)
                .where(FILEID.eq(fileId)
                        .and(USERID.eq(userId)))
                .execute();
    }

    // 检查用户是否有特定权限
    public boolean hasPermission(String fileId, String userId, String permission) {
        return dslContext.selectFrom(TABLE)
                .where(FILEID.eq(fileId)
                        .and(USERID.eq(userId))
                        .and(DSL.field(permission).eq(true)))
                .fetchOptional()
                .isPresent();
    }
}