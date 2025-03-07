package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysDataSource;
import com.owiseman.dataapi.util.JooqContextHolder;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSDATASOURCE.*;

@Repository
public class SysDataSourceRepository {
    private final DSLContext dslContext;

    @Autowired
    public SysDataSourceRepository() {
        this.dslContext = JooqContextHolder.getDslContext();
    }

    // 基本CRUD操作
    public SysDataSource save(SysDataSource dataSource) {
        dslContext.insertInto(TABLE)
                .set(ID, dataSource.getId())
                .set(TYPE, dataSource.getType())
                .set(CONFIG, dataSource.getConfig()) // JSONB字段处理
                .set(CREATEDBY, dataSource.getCreatedBy())
                .set(LASTTESTRESULT, dataSource.getLastTestResult())
                .execute();
        return dataSource;
    }

    public void update(SysDataSource dataSource) {
        dslContext.update(TABLE)
                .set(TYPE, dataSource.getType())
                .set(CONFIG, dataSource.getConfig())
                .set(CREATEDBY, dataSource.getCreatedBy())
                .set(LASTTESTRESULT, dataSource.getLastTestResult())
                .where(ID.eq(dataSource.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysDataSource> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysDataSource.class);
    }

    public List<SysDataSource> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysDataSource.class);
    }

    // 分页基础方法
    public PageResult<SysDataSource> findAllWithPagination(int pageNumber, int pageSize) {
        return findByConditionWithPagination(DSL.noCondition(), pageNumber, pageSize);
    }

    public PageResult<SysDataSource> findByConditionWithPagination(Condition condition,
                                                                  int pageNumber,
                                                                  int pageSize) {
        List<SysDataSource> list = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysDataSource.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(list, pageNumber, pageSize, total);
    }

    // 扩展方法：按类型查询（带分页+排序）
    public PageResult<SysDataSource> findByTypeWithPagination(String type,
                                                             int pageNumber,
                                                             int pageSize,
                                                             SortField<?>... sortFields) {
        Condition condition = TYPE.eq(type);

        List<SysDataSource> list = dslContext.selectFrom(TABLE)
                .where(condition)
                .orderBy(sortFields)
                .limit(pageSize)
                .offset((pageNumber - 1) * pageSize)
                .fetchInto(SysDataSource.class);

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(list, pageNumber, pageSize, total);
    }

    // 扩展方法：按创建者查询
    public List<SysDataSource> findByCreatedBy(String createdBy) {
        return dslContext.selectFrom(TABLE)
                .where(CREATEDBY.eq(createdBy))
                .fetchInto(SysDataSource.class);
    }
}
