package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.util.JooqContextHolder;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectForUpdateStep;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.owiseman.dataapi.entity.Tables.SYSUSERFILE.*;

@Repository
public class SysUserFilesRepository {
    private final DSLContext dslContext;

    @Autowired
    public SysUserFilesRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    @Autowired
    private SysUserConfigRepository sysUserConfigRepository;

    public SysUserFile save(SysUserFile userFile) {
        dslContext.insertInto(TABLE)
                .set(ID, userFile.getId())
                .set(USERID, userFile.getUserId())
                .set(FID, userFile.getFid())
                .set(FILENAME, userFile.getFileName())
                .set(ISDIRECTORY, userFile.isDirectory())
                .set(SIZE, userFile.getSize())
                .set(STORAGETYPE, userFile.getStorageType())
                .set(PARENTID, userFile.getParentId())
                .set(UPLOADTIME, userFile.getUploadTime())
                .set(PROJECTID, userFile.getProjectId())
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
    public List<SysUserFile> findByFileNameLike(String pattern, String projectApiKey) {
        String projectId = sysUserConfigRepository.findByProjectApiKey(projectApiKey).get().getId();
        return dslContext.selectFrom(TABLE)
                .where(FILENAME.likeIgnoreCase("%" + pattern + "%")).and(PROJECTID.eq(projectId))
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
                                                              String parentId,
                                                              String projectApiKey,
                                                              int pageNumber,
                                                              int pageSize,
                                                              SortField<?>... sortFields) {
        Condition condition = DSL.noCondition();

        if (parentId != null) {
            condition = condition.and(PARENTID.eq(parentId));
        } else {
            condition = condition.and(PARENTID.isNull());
        }
        if (userId != null) {
            condition = condition.and(USERID.eq(userId));
        } else {
            throw new IllegalArgumentException("非法操作");
        }
        List<SysUserFile> files = new ArrayList<>();
        if (sortFields == null) {

            files = PaginationHelper.getPaginatedData(
                    dslContext,
                    condition,
                    TABLE.getName(),
                    pageSize,
                    pageNumber,
                    SysUserFile.class
            );
        } else {
            files = PaginationHelper.getPaginatedData(
                    dslContext,
                    condition,
                    TABLE.getName(),
                    pageSize,
                    pageNumber,
                    SysUserFile.class,
                    sortFields);
        }
        String projectId = sysUserConfigRepository.findByProjectApiKey(projectApiKey).get().getId();
        List<SysUserFile> filesProjectID = new ArrayList<>();
        for (SysUserFile file : files) {
            if (file.getProjectId() != null) {
                if (file.getProjectId().equals(projectId)) {
                    filesProjectID.add(file);
                }
            }
        }
        List<SysUserFile> directory = new ArrayList<>();
        for (SysUserFile file : files) {
            if (file.isDirectory()) {
                directory.add(file);
            }
        }

        files.clear();
        files.addAll(directory);
        files.addAll(filesProjectID);
        files = files.stream().distinct().collect(Collectors.toList()); // 去重
        /**
         * // 保留第一个出现的元素
         * List<File> distinctFiles = new ArrayList<>(files.stream()
         *         .collect(Collectors.toMap(
         *                 File::getId,
         *                 Function.identity(),
         *                 (existing, replacement) -> existing // 合并策略：保留已存在的值
         *         ))
         *         .values());
         *
         * // 保留最后一个出现的元素
         * List<File> distinctFilesLast = new ArrayList<>(files.stream()
         *         .collect(Collectors.toMap(
         *                 File::getId,
         *                 Function.identity(),
         *                 (existing, replacement) -> replacement // 合并策略：覆盖为新的值
         *         ))
         *         .values());
         */

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

    public void deleteByIdAndUserId(String id, String userId) {
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
