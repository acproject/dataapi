package com.owiseman.dataapi.service;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.owiseman.jpa.util.TableAndDataUtil;
import com.owiseman.jpa.model.DataRecord;

import java.util.List;

@Service

public class JooqService {

    @Autowired
    private DSLContext dslContext;
    public DataRecord executeSQL(String json) throws Exception {
        return TableAndDataUtil.processRequest(dslContext, json);
    }
}
