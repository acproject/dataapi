package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUserFile;
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
import java.util.UUID;

import static com.owiseman.dataapi.entity.Tables.SYSUSERFILE.*;

@Repository
public class SysUserFilesRepository {
    private final DSLContext dslContext;

    @Autowired
    public SysUserFilesRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    public SysUserFile save(SysUserFile userFile) {
        if (userFile.getId() == null || userFile.getId().isEmpty()) {
            userFile.setId(UUID.randomUUID().toString());
        }
        dslContext.insertInto(TABLE)
                .set(ID, userFile.getId())
                .set(USERID, userFile.getUserId())
                .set(FID, userFile.getFid())
                .set(FILENAME, userFile.getFileName())
                .set(ISDIRECTORY, userFile.isDirectory())
                .set(SIZE, userFile.getSize())
                .set(UPLOADTIME, userFile.getUploadTime())
                .execute();
        return userFile;
    }

    public void update(SysUserFile userFile) {
        dslContext.update(TABLE)
                .set(USERID, userFile.getUserId())
                .set(FID, userFile.getFid())
                .set(FILENAME, userFile.getFileName())
                .set(SIZE, userFile.getSize())
                .set(UPLOADTIME, userFile.getUploadTime())
                .where(ID.eq(userFile.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysUserFile> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysUserFile.class);
    }

    public List<SysUserFile> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysUserFile.class);
    }

    public PageResult<SysUserFile> findAllWithPagination(int pageNumber, int pageSize) {
        List<SysUserFile> files = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.noCondition(),
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserFile.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(DSL.noCondition())
                .fetchOne(0, Integer.class);

        return new PageResult<>(files, pageNumber, pageSize, total);
    }

    // 根据用户ID查询文件
    public List<SysUserFile> findByUserId(String userId) {
        return dslContext.selectFrom(TABLE)
                .where(USERID.eq(userId))
                .fetchInto(SysUserFile.class);
    }

    // 根据文件名模糊查询
    public List<SysUserFile> findByFileNameLike(String pattern) {
        return dslContext.selectFrom(TABLE)
                .where(FILENAME.likeIgnoreCase("%" + pattern + "%"))
                .fetchInto(SysUserFile.class);
    }

    // 根据用户ID分页查询文件
    public PageResult<SysUserFile> findByUserIdWithPagination(String userId, int pageNumber, int pageSize) {
        Condition condition = USERID.eq(userId);

        List<SysUserFile> files = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserFile.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(files, pageNumber, pageSize, total);
    }

    // 根据文件名模糊分页查询
    public PageResult<SysUserFile> findByFileNameLikeWithPagination(String pattern, int pageNumber, int pageSize) {
        Condition condition = FILENAME.likeIgnoreCase("%" + pattern + "%");

        List<SysUserFile> files = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserFile.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(files, pageNumber, pageSize, total);
    }
    // 添加排序参数扩展
    public PageResult<SysUserFile> findByUserIdWithPagination(String userId,
                                                              int pageNumber,
                                                              int pageSize,
                                                              SortField<?>... sortFields) {
        Condition condition = USERID.eq(userId);

        List<SysUserFile> files = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserFile.class,
                sortFields // 传递排序参数
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(files, pageNumber, pageSize, total);
    }


    @Transactional
    public void batchInsert(List<SysUserFile> files) {
        files.forEach(this::save);
    }

    public PageResult<SysUserFile> findByConditionWithPagination(Condition condition, int pageNumber, int pageSize) {
        List<SysUserFile> files = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysUserFile.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(files, pageNumber, pageSize, total);
    }

    public Optional<SysUserFile> findByIdAndUserId(String id, String userId) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id).and(USERID.eq(userId)))
                .fetchOptionalInto(SysUserFile.class);
    }

    public  void deleteByIdAndUserId(String id, String userId) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id).and(USERID.eq(userId)))
                .execute();
    }
    // 查找用户的根目录
    public Optional<SysUserFile> findRootDirectoryByUserId(String userId) {
        return dslContext.selectFrom(TABLE)
                .where(USERID.eq(userId)
                        .and(PARENTID.isNull())
                        .and(ISDIRECTORY.eq(true)))
                .fetchOptionalInto(SysUserFile.class);
    }

    // 查找目录下的所有文件和子目录
    public List<SysUserFile> findByParentId(String parentId) {
        return dslContext.selectFrom(TABLE)
                .where(PARENTID.eq(parentId))
                .fetchInto(SysUserFile.class);
    }

    // 根据路径查找文件或目录
    public Optional<SysUserFile> findByPath(String userId, String path) {
        return dslContext.selectFrom(TABLE)
                .where(USERID.eq(userId)
                        .and(PATH.eq(path)))
                .fetchOptionalInto(SysUserFile.class);
    }
}
