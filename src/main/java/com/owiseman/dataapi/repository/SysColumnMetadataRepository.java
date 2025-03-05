package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysColumnMetadata;
import com.owiseman.dataapi.entity.SysTableMetadata;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableRecord;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static com.owiseman.dataapi.entity.Tables.SYSCOLUMNMETADATA.*;

@Repository
public class SysColumnMetadataRepository {
    @Autowired
    private DSLContext dslContext;

    public SysColumnMetadata save(SysColumnMetadata column) {
        dslContext.insertInto(TABLE)
                .set(ID, column.getId())
                .set(SYSTABLEMETADATA, column.getSysTableMetadata())  // 使用正确的字段名 TABLE_ID
                .set(COLUMNNAME, column.getColumnName())
                .set(DATATYPE, column.getDataType())
                .set(PRIMARYKEY, column.isPrimaryKey())  // 修正字段名 PRIMARYKEY
                .set(NULLABLE, column.isNullable())       // 修正字段名 NULLABLE
                .set(DEFAULTVALUE, column.getDefaultValue())
                .set(DESCRIPTION, column.getDescription())
                .set(ORDINALPOSITION, column.getOrdinalPosition())
                .execute();
        return column;
    }

    public void saveAll(List<SysColumnMetadata> columns){
        columns.forEach(column -> {
            dslContext.insertInto(TABLE)
                    .set(ID, column.getId())
                    .set(SYSTABLEMETADATA, column.getSysTableMetadata())
                    .set(COLUMNNAME, column.getColumnName())
                    .set(DATATYPE, column.getDataType())
                    .set(PRIMARYKEY, column.isPrimaryKey())
                    .set(NULLABLE, column.isNullable())
                    .set(DEFAULTVALUE, column.getDefaultValue())
                    .set(DESCRIPTION, column.getDescription())
                    .set(ORDINALPOSITION, column.getOrdinalPosition())
                    .execute();
        });
    }

    public void update(SysColumnMetadata column) {
        dslContext.update(TABLE)
                .set(SYSTABLEMETADATA, column.getSysTableMetadata())
                .set(COLUMNNAME, column.getColumnName())
                .set(DATATYPE, column.getDataType())
                .set(PRIMARYKEY, column.isPrimaryKey())
                .set(NULLABLE, column.isNullable())
                .set(DEFAULTVALUE, column.getDefaultValue())
                .set(DESCRIPTION, column.getDescription())
                .set(ORDINALPOSITION, column.getOrdinalPosition())
                .where(ID.eq(column.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysColumnMetadata> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysColumnMetadata.class);
    }

    public List<SysColumnMetadata> findByTableId(String sysTableMetadata) {
        return dslContext.selectFrom(TABLE)
                .where(SYSTABLEMETADATA.eq(sysTableMetadata))  // 使用正确的字段名 TABLE_ID
                .fetchInto(SysColumnMetadata.class);
    }


    public PageResult<SysColumnMetadata> findAllWithPagination(int pageNumber, int pageSize) {
        List<SysColumnMetadata> columns = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.noCondition(),
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysColumnMetadata.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .fetchOne(0, Integer.class);

        return new PageResult<>(columns, pageNumber, pageSize, total);
    }

    public PageResult<SysColumnMetadata> findByConditionWithPagination(Condition condition, int pageNumber, int pageSize) {
        List<SysColumnMetadata> columns = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysColumnMetadata.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(columns, pageNumber, pageSize, total);
    }

    public Optional<SysColumnMetadata> findByTableIdAndColumnName(String tableId, String columnName) {
        return dslContext.selectFrom(TABLE)
                .where(SYSTABLEMETADATA.eq(tableId))
                .and(COLUMNNAME.eq(columnName))
                .fetchOptionalInto(SysColumnMetadata.class);
    }

    public Integer findMaxOrdinalPositionByTableId(String id) {
        return (Integer) dslContext.select(DSL.max(ORDINALPOSITION))
                .from(TABLE)
                .where(SYSTABLEMETADATA.eq(id))
                .fetchOne(0, Integer.class);
    }

    public void deleteByTableIdAndColumnName(String id, String columnName) {
        dslContext.deleteFrom(TABLE)
                .where(SYSTABLEMETADATA.eq(id))
                .and(COLUMNNAME.eq(columnName))
                .execute();
    }

    public List<SysColumnMetadata> findByTableSysTableMetadata(SysTableMetadata sysTableMetadata) {
        if (sysTableMetadata != null) {
            return dslContext.selectFrom(TABLE)
                    .where(SYSTABLEMETADATA.eq(sysTableMetadata.getId()))
                    .fetchInto(SysColumnMetadata.class);
        }
        return null;
    }
}
