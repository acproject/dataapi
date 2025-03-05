package com.owiseman.dataapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owiseman.jpa.model.DataRecord;
import com.owiseman.jpa.util.TableAndDataUtil;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JooqService {
    private final DSLContext dslContext;
    private final ObjectMapper objectMapper;
    private final TableMetadataService tableMetadataService;

    @Autowired
    public JooqService(DSLContext dslContext, TableMetadataService tableMetadataService) {
        this.dslContext = dslContext;
        this.tableMetadataService = tableMetadataService;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    public DataRecord executeSQL(String json) throws Exception {
        JsonNode rootNode = objectMapper.readTree(json);
        String operation = rootNode.get("operation").asText();
        String userId = rootNode.has("userId") ? rootNode.get("userId").asText() : "system";

        // 处理DDL操作，调用TableMetadataService
        switch (operation) {
            case "create_table":
                // 创建表并保存元数据
                tableMetadataService.createTableWithMetadata(rootNode, userId);
                return TableAndDataUtil.getInstance().createTable(dslContext, rootNode);

            case "create_batch_table":
                // 批量创建表并保存元数据
                JsonNode tablesNode = rootNode.get("tables");

                if (tablesNode != null && tablesNode.isArray()) {
                    for (JsonNode tableNode : tablesNode) {
                        tableMetadataService.createTableWithMetadata(tableNode, userId);
                        TableAndDataUtil.getInstance().createTable(dslContext, tableNode);
                    }
                    DataRecord result = new DataRecord("批量创建表成功", "批量插入", null, null);
                    return result;
                }
                return new DataRecord("批量创建表失败", "批量插入", null, null);


            case "drop_table":
                // 删除表并更新元数据状态
                String tableName = rootNode.get("table").asText();
                return tableMetadataService.dropTable(tableName);

            case "alter_table":
                // 修改表结构并更新元数据
                return tableMetadataService.alterTable(rootNode);

            // 其他非DDL操作，直接调用TableAndDataUtil
            default:
                return processNonDDLRequest(rootNode, operation);
        }
    }

    private DataRecord processNonDDLRequest(JsonNode rootNode, String operation) throws Exception {
        switch (operation) {
            case "insert":
                return TableAndDataUtil.getInstance().insertData(dslContext, rootNode);

            case "insert_batch":
                return TableAndDataUtil.getInstance().insertBatchData(dslContext, rootNode);

            case "update_data":
                return TableAndDataUtil.getInstance().updateData(dslContext, rootNode);

            case "update_batch":
                return TableAndDataUtil.getInstance().updateBatchData(dslContext, rootNode);

            case "delete":
                return TableAndDataUtil.getInstance().deleteData(dslContext, rootNode);

            case "select":
                return TableAndDataUtil.getInstance().selectData(dslContext, rootNode);

            case "select_batch":
                return TableAndDataUtil.getInstance().selectJoinData(dslContext, rootNode);

            case "add_foreign":
                return TableAndDataUtil.getInstance().addForeignKey(dslContext, rootNode);

            case "create_index":
                return TableAndDataUtil.getInstance().createIndex(dslContext, rootNode);

            case "drop_index":
                return TableAndDataUtil.getInstance().dropIndex(dslContext, rootNode);

            default:
                throw new IllegalArgumentException("不支持的操作: " + operation);
        }
    }
}
