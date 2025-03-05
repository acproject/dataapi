package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysTableMetadata;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSTABLEMETADATA.*;

@Repository
public class SysTableMetadataRepository {
    @Autowired
    private DSLContext dslContext;

   public SysTableMetadata save(SysTableMetadata table) {
        dslContext.insertInto(TABLE)
                .set(ID, table.getId())
                .set(TABLENAME, table.getTableName())  // 使用正确的字段名 TABLENAME
                .set(DESCRIPTION, table.getDescription())
                .set(CREATEDAT, table.getCreatedAt())   // 字段名 CREATEDAT
                .set(UPDATEDAT, table.getUpdatedAt())   // 字段名 UPDATEDAT
                .set(CREATEDBY, table.getCreatedBy())
                .set(TABLEDEFINITION, table.getTableDefinition())
                .set(STATUS, table.getStatus())
                .execute();
        return table;
    }

    public void update(SysTableMetadata table) {
        dslContext.update(TABLE)
                .set(TABLENAME, table.getTableName())
                .set(DESCRIPTION, table.getDescription())
                .set(UPDATEDAT, table.getUpdatedAt())
                .set(CREATEDBY, table.getCreatedBy())
                .set(TABLEDEFINITION, table.getTableDefinition())
                .set(STATUS, table.getStatus())
                .where(ID.eq(table.getId()))
                .execute();
    }

    public Optional<SysTableMetadata> findByTableName(String tableName) {
        return dslContext.selectFrom(TABLE)
                .where(TABLENAME.eq(tableName))  // 使用正确的字段名 TABLENAME
                .fetchOptionalInto(SysTableMetadata.class);
    }


    public PageResult<SysTableMetadata> findAllWithPagination(int pageNumber, int pageSize) {
        List<SysTableMetadata> tables = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.noCondition(),
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysTableMetadata.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .fetchOne(0, Integer.class);

        return new PageResult<>(tables, pageNumber, pageSize, total);
    }

    public PageResult<SysTableMetadata> findByStatusWithPagination(String status, int pageNumber, int pageSize) {
        Condition condition = STATUS.eq(status);
        List<SysTableMetadata> tables = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysTableMetadata.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(tables, pageNumber, pageSize, total);
    }


    public List<SysTableMetadata> findByStatusNot(String deleted) {
        return dslContext.selectFrom(TABLE)
                .where(STATUS.ne(deleted))
                .fetchInto(SysTableMetadata.class);
    }

    public Optional<SysTableMetadata> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysTableMetadata.class);
    }
}
