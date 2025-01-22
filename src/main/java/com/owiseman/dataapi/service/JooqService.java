package com.owiseman.dataapi.service;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import com.owiseman.jpa.util.TableAndDataUtil;
import com.owiseman.jpa.model.DataRecord;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JooqService {

    private DSLContext dslContext;
    public DataRecord executeSQL(String json) throws Exception {
        return TableAndDataUtil.processRequest(dslContext, json);
    }
}
