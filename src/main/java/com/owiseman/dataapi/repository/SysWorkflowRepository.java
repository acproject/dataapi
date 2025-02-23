package com.owiseman.dataapi.repository;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysWorkflow;
import com.owiseman.jpa.util.PaginationHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.owiseman.dataapi.entity.Tables.SYSWORKFLOW.*;

@Repository
public class SysWorkflowRepository {
    @Autowired
    private DSLContext dslContext;

    public SysWorkflow save(SysWorkflow workflow) {
        dslContext.insertInto(TABLE)
                .set(ID, workflow.getId())
                .set(NAME, workflow.getName())
                .set(DEFINITION, workflow.getDefinition())
                .set(CREATEBY, workflow.getCreateBy())
                .set(CREATEAT, workflow.getCreateAt())
                .set(UPDATEAT, workflow.getUpdateAt())
                .execute();
        return workflow;
    }

    public void update(SysWorkflow workflow) {
        dslContext.update(TABLE)
                .set(NAME, workflow.getName())
                .set(DEFINITION, workflow.getDefinition())
                .set(CREATEBY, workflow.getCreateBy())
                .set(CREATEAT, workflow.getCreateAt())
                .set(UPDATEAT, workflow.getUpdateAt())
                .where(ID.eq(workflow.getId()))
                .execute();
    }

    public void deleteById(String id) {
        dslContext.deleteFrom(TABLE)
                .where(ID.eq(id))
                .execute();
    }

    public Optional<SysWorkflow> findById(String id) {
        return dslContext.selectFrom(TABLE)
                .where(ID.eq(id))
                .fetchOptionalInto(SysWorkflow.class);
    }

    public List<SysWorkflow> findAll() {
        return dslContext.selectFrom(TABLE)
                .fetchInto(SysWorkflow.class);
    }

    public PageResult<SysWorkflow> findAllWithPagination(int pageNumber, int pageSize) {
        List<SysWorkflow> workflows = PaginationHelper.getPaginatedData(
                dslContext,
                DSL.noCondition(),
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysWorkflow.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(DSL.noCondition())
                .fetchOne(0, Integer.class);

        return new PageResult<>(workflows, pageNumber, pageSize, total);
    }

    public PageResult<SysWorkflow> findByConditionWithPagination(Condition condition, int pageNumber, int pageSize) {
        List<SysWorkflow> workflows = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                TABLE.getName(),
                pageSize,
                pageNumber,
                SysWorkflow.class
        );

        int total = dslContext.selectCount()
                .from(TABLE)
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(workflows, pageNumber, pageSize, total);
    }
}
