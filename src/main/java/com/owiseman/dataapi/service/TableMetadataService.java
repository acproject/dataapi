package com.owiseman.dataapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysColumnMetadata;
import com.owiseman.dataapi.entity.SysTableMetadata;
import com.owiseman.dataapi.repository.SysColumnMetadataRepository;
import com.owiseman.dataapi.repository.SysTableMetadataRepository;
import com.owiseman.dataapi.util.JsonUtil;
import com.owiseman.jpa.model.DataRecord;
import com.owiseman.jpa.util.PaginationHelper;
import com.owiseman.jpa.util.TableAndDataUtil;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.owiseman.dataapi.entity.Tables.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 表元数据服务
 */
@Service
public class TableMetadataService {

    private final SysTableMetadataRepository tableMetadataRepository;
    private final SysColumnMetadataRepository columnMetadataRepository;
    private final DSLContext dslContext;
    private final TableAndDataUtil tableAndDataUtil;
    private final ObjectMapper objectMapper;

    @Autowired
    public TableMetadataService(
            SysTableMetadataRepository tableMetadataRepository,
            SysColumnMetadataRepository columnMetadataRepository,
            DSLContext dslContext,
            TableAndDataUtil tableAndDataUtil) {
        this.tableMetadataRepository = tableMetadataRepository;
        this.columnMetadataRepository = columnMetadataRepository;
        this.dslContext = dslContext;
        this.tableAndDataUtil = tableAndDataUtil;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 创建表并保存元数据
     *
     * @param rootNode 表定义JSON
     * @param userId 创建者ID
     * @return 创建的表元数据
     */
    @Transactional
    public SysTableMetadata createTableWithMetadata(JsonNode rootNode, String userId) {
        // 1. 使用TableAndDataUtil创建表
        DataRecord dataRecord = tableAndDataUtil.createTable(dslContext, rootNode);
        
        // 2. 保存表元数据
        String tableName = rootNode.get("table").asText();
        JsonNode columnsNode = rootNode.get("columns");
        
        SysTableMetadata tableMetadata = new SysTableMetadata();
        tableMetadata.setId(UUID.randomUUID().toString());
        tableMetadata.setTableName(tableName);
        tableMetadata.setDescription(rootNode.has("description") ? rootNode.get("description").asText() : "");
        tableMetadata.setCreatedAt(LocalDateTime.now());
        tableMetadata.setUpdatedAt(LocalDateTime.now());
        tableMetadata.setCreatedBy(userId);
        tableMetadata.setTableDefinition(JsonUtil.convertToMap(rootNode));
        tableMetadata.setStatus("ACTIVE");
        
        tableMetadataRepository.save(tableMetadata);
        
        // 3. 保存列元数据
        if (columnsNode != null && columnsNode.isArray()) {
            List<SysColumnMetadata> columnMetadataList = new ArrayList<>();
            
            for (int i = 0; i < columnsNode.size(); i++) {
                JsonNode column = columnsNode.get(i);
                String columnName = column.get("name").asText();
                String dataType = column.get("type").asText();
                boolean isPrimaryKey = column.has("primary_key") && column.get("primary_key").asBoolean();
                boolean isNullable = !column.has("null") || column.get("null").asBoolean();
                String defaultValue = column.has("default") ? column.get("default").asText() : null;
                
                SysColumnMetadata columnMetadata = new SysColumnMetadata();
                columnMetadata.setId(UUID.randomUUID().toString());
                columnMetadata.setSysTableMetadata(tableMetadata);
                columnMetadata.setColumnName(columnName);
                columnMetadata.setDataType(dataType);
                columnMetadata.setPrimaryKey(isPrimaryKey);
                columnMetadata.setNullable(isNullable);
                columnMetadata.setDefaultValue(defaultValue);
                columnMetadata.setDescription(column.has("description") ? column.get("description").asText() : "");
                columnMetadata.setOrdinalPosition(i);
                
                columnMetadataList.add(columnMetadata);
            }
            
            columnMetadataRepository.saveAll(columnMetadataList);
        }
        
        return tableMetadata;
    }
    
    /**
     * 删除表并更新元数据状态
     *
     * @param tableName 表名
     * @return 操作结果
     */
    @Transactional
    public DataRecord dropTable(String tableName) {
        try {
            // 1. 构建删除表的JSON
            JsonNode rootNode = objectMapper.createObjectNode().put("table", tableName);
            
            // 2. 使用TableAndDataUtil删除表
            DataRecord dataRecord = tableAndDataUtil.dropTable(dslContext, rootNode);
            
            // 3. 更新元数据状态
            Optional<SysTableMetadata> tableMetadataOpt = tableMetadataRepository.findByTableName(tableName);
            if (tableMetadataOpt.isPresent()) {
                SysTableMetadata tableMetadata = tableMetadataOpt.get();
                tableMetadata.setStatus("DELETED");
                tableMetadata.setUpdatedAt(LocalDateTime.now());
                tableMetadataRepository.save(tableMetadata);
            }
            
            return dataRecord;
        } catch (Exception e) {
            throw new RuntimeException("删除表失败: " + tableName, e);
        }
    }
    
    /**
     * 修改表结构并更新元数据
     *
     * @param rootNode 表修改定义JSON
     * @return 操作结果
     */
    @Transactional
    public DataRecord alterTable(JsonNode rootNode) {
        try {
            // 1. 使用TableAndDataUtil修改表
            DataRecord dataRecord = tableAndDataUtil.alterTable(dslContext, rootNode);
            
            // 2. 更新表元数据
            String tableName = rootNode.get("table").asText();
            Optional<SysTableMetadata> tableMetadataOpt = tableMetadataRepository.findByTableName(tableName);
            
            if (tableMetadataOpt.isPresent()) {
                SysTableMetadata tableMetadata = tableMetadataOpt.get();
                tableMetadata.setUpdatedAt(LocalDateTime.now());
                tableMetadata.setTableDefinition(JsonUtil.convertToMap(rootNode));
                tableMetadataRepository.save(tableMetadata);
                
                // 3. 更新列元数据
                JsonNode columnsNode = rootNode.get("columns");
                if (columnsNode != null && columnsNode.isArray()) {
                    for (JsonNode column : columnsNode) {
                        String columnName = column.get("name").asText();
                        String operation = column.has("operation") ? column.get("operation").asText() : "add";
                        
                        switch (operation.toLowerCase()) {
                            case "add":
                                // 添加新列
                                String dataType = column.get("type").asText();
                                boolean isPrimaryKey = column.has("primary_key") && column.get("primary_key").asBoolean();
                                boolean isNullable = !column.has("null") || column.get("null").asBoolean();
                                String defaultValue = column.has("default") ? column.get("default").asText() : null;
                                
                                SysColumnMetadata newColumn = new SysColumnMetadata();
                                newColumn.setId(UUID.randomUUID().toString());
                                newColumn.setSysTableMetadata(tableMetadata);
                                newColumn.setColumnName(columnName);
                                newColumn.setDataType(dataType);
                                newColumn.setPrimaryKey(isPrimaryKey);
                                newColumn.setNullable(isNullable);
                                newColumn.setDefaultValue(defaultValue);
                                newColumn.setDescription(column.has("description") ? column.get("description").asText() : "");
                                
                                // 获取最大的ordinalPosition并加1
                                int maxPosition = columnMetadataRepository.findMaxOrdinalPositionByTableId(tableMetadata.getId());
                                newColumn.setOrdinalPosition(maxPosition + 1);
                                
                                columnMetadataRepository.save(newColumn);
                                break;
                                
                            case "modify":
                                // 修改列
                                Optional<SysColumnMetadata> columnOpt = columnMetadataRepository.findByTableIdAndColumnName(
                                        tableMetadata.getId(), columnName);
                                
                                if (columnOpt.isPresent()) {
                                    SysColumnMetadata existingColumn = columnOpt.get();
                                    
                                    if (column.has("type")) {
                                        existingColumn.setDataType(column.get("type").asText());
                                    }
                                    
                                    if (column.has("null")) {
                                        existingColumn.setNullable(column.get("null").asBoolean());
                                    }
                                    
                                    if (column.has("default")) {
                                        existingColumn.setDefaultValue(column.get("default").asText());
                                    }
                                    
                                    if (column.has("description")) {
                                        existingColumn.setDescription(column.get("description").asText());
                                    }
                                    
                                    if (column.has("primary_key")) {
                                        existingColumn.setPrimaryKey(column.get("primary_key").asBoolean());
                                    }
                                    
                                    columnMetadataRepository.save(existingColumn);
                                }
                                break;
                                
                            case "drop":
                                // 删除列
                                columnMetadataRepository.deleteByTableIdAndColumnName(tableMetadata.getId(), columnName);
                                break;
                        }
                    }
                }
            }
            
            return dataRecord;
        } catch (Exception e) {
            throw new RuntimeException("修改表结构失败", e);
        }
    }



    /**
     * 根据表ID和列名删除列元数据
     * 
     * @param tableId 表ID
     * @param columnName 列名
     */
    @Transactional
    public void deleteByTableIdAndColumnName(SysTableMetadata tableId, String columnName) {
        List<SysColumnMetadata> columns = columnMetadataRepository.findByTableSysTableMetadata(tableId);
        columns.stream()
            .filter(column -> column.getColumnName().equals(columnName))
            .findFirst()
            .ifPresent(column -> columnMetadataRepository.deleteById(column.getId()));
    }
    
    /**
     * 获取所有表元数据
     *
     * @return 表元数据列表
     */
    public List<SysTableMetadata> getAllTables() {
        return tableMetadataRepository.findByStatusNot("DELETED");
    }

     /**
     * 批量保存列元数据
     * 
     * @param columns 列元数据列表
     * @return 保存后的列元数据列表
     */
    @Transactional
    public List<SysColumnMetadata> saveAllColumnMetadata(List<SysColumnMetadata> columns) {
        for (SysColumnMetadata column : columns) {
            columnMetadataRepository.save(column);
        }
        return columns;
    }

    /**
     * 根据表ID和列名查找列元数据
     *
     * @param columnName 列名
     * @return 列元数据
     */
    public Optional<SysColumnMetadata> findColumnByTableIdAndColumnName(SysTableMetadata sysTableMetadata, String columnName) {
        List<SysColumnMetadata> columns = columnMetadataRepository.findByTableSysTableMetadata(sysTableMetadata);
        return columns.stream()
            .filter(column -> column.getColumnName().equals(columnName))
            .findFirst();
    }


    /**
     * 分页查询表元数据（排除已删除的表）
     * 
     * @param pageNumber 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public PageResult<SysTableMetadata> findByStatusNotWithPagination(String status, int pageNumber, int pageSize) {
        // 使用repository的方法实现
        Condition condition = DSL.field("status").ne(status);
        List<SysTableMetadata> tables = PaginationHelper.getPaginatedData(
                dslContext,
                condition,
                "sys_table_metadata",
                pageSize,
                pageNumber,
                SysTableMetadata.class
        );

        int total = dslContext.selectCount()
                .from("sys_table_metadata")
                .where(condition)
                .fetchOne(0, Integer.class);

        return new PageResult<>(tables, pageNumber, pageSize, total);
    }
    
    
    /**
     * 根据表名获取表元数据
     *
     * @param tableName 表名
     * @return 表元数据
     */
    public Optional<SysTableMetadata> getTableByName(String tableName) {
        return tableMetadataRepository.findByTableName(tableName);
    }
    
    /**
     * 根据表ID获取表元数据
     * @return 表元数据
     */
    public Optional<SysTableMetadata> getTableById(String id) {
        return tableMetadataRepository.findById(id);
    }
    


    /**
     * 根据表ID查找最大的ordinalPosition
     *
     * @return 最大的ordinalPosition
     */
    public int findMaxOrdinalPositionByTableId(SysTableMetadata tableMetadata) {
        List<SysColumnMetadata> columns = columnMetadataRepository.findByTableSysTableMetadata(tableMetadata);
        return columns.isEmpty() ? 0 : 
            columns.stream().mapToInt(SysColumnMetadata::getOrdinalPosition).max().orElse(0);
    }
    


    /**
     * 根据表ID查找列元数据并按ordinalPosition排
     * @return 列元数据列表
     */
    public List<SysColumnMetadata> findByTableIdOrderByOrdinalPosition(SysTableMetadata sysTableMetadata) {
        List<SysColumnMetadata> columns = columnMetadataRepository.findByTableSysTableMetadata(sysTableMetadata);
        return columns.stream()
            .sorted(Comparator.comparingInt(SysColumnMetadata::getOrdinalPosition))
            .collect(Collectors.toList());
    }
    

    /**
     * 获取数据库中的所有表（包括非通过本工具创建的表）
     *
     * @return 表名列表
     */
    public List<String> getAllDatabaseTables() {
        return dslContext.select(DSL.field("table_name"))
                .from("information_schema.tables")
                .where(DSL.field("table_schema").eq("public"))
                .fetchInto(String.class);
    }

    /**
     * 查询所有表名
     * 
     * @return 表名列表
     */
    public List<String> findAllTableNames() {
        return dslContext.select(DSL.field("table_name"))
                .from("sys_table_metadata")
                .where(DSL.field("status").ne("DELETED"))
                .fetchInto(String.class);
    }

      /**
     * 检查表名是否存在（排除已删除的表）
     * 
     * @param tableName 表名
     * @param status 排除的状态
     * @return 是否存在
     */
    public boolean existsByTableNameAndStatusNot(String tableName, String status) {
        return dslContext.fetchExists(
                dslContext.selectOne()
                        .from("sys_table_metadata")
                        .where(DSL.field("table_name").eq(tableName))
                        .and(DSL.field("status").ne(status))
        );
    }

     /**
     * 查询状态不为指定值的表
     * 
     * @param status 排除的状态
     * @return 表元数据列表
     */
    public List<SysTableMetadata> findByStatusNot(String status) {
        return dslContext.selectFrom("sys_table_metadata")
                .where(DSL.field("status").ne(status))
                .fetchInto(SysTableMetadata.class);
    }
    
   /**
     * 完成同步数据库表与元数据的方法
     */
    @Transactional
    public void syncDatabaseTablesWithMetadata() {
        List<String> databaseTables = getAllDatabaseTables();
        List<String> metadataTables = findAllTableNames();
        
        for (String tableName : databaseTables) {
            // 跳过元数据表和系统表
            if (tableName.equals("sys_table_metadata") || tableName.equals("sys_column_metadata") || 
                    tableName.startsWith("pg_") || tableName.startsWith("information_schema")) {
                continue;
            }
            
            if (!metadataTables.contains(tableName)) {
                // 创建表元数据
                SysTableMetadata tableMetadata = new SysTableMetadata();
                tableMetadata.setId(UUID.randomUUID().toString());
                tableMetadata.setTableName(tableName);
                tableMetadata.setDescription("自动同步的表");
                tableMetadata.setCreatedAt(LocalDateTime.now());
                tableMetadata.setUpdatedAt(LocalDateTime.now());
                tableMetadata.setCreatedBy("system");
                tableMetadata.setStatus("ACTIVE");
                
                tableMetadataRepository.save(tableMetadata);
                
                // 获取表结构并创建列元数据
                List<Map<String, Object>> columns = dslContext
                        .select(
                                DSL.field("column_name"),
                                DSL.field("data_type"),
                                DSL.field("is_nullable"),
                                DSL.field("column_default"),
                                DSL.field("ordinal_position")
                        )
                        .from("information_schema.columns")
                        .where(DSL.field("table_name").eq(tableName))
                        .and(DSL.field("table_schema").eq("public"))
                        .orderBy(DSL.field("ordinal_position"))
                        .fetchMaps();
                
                // 获取主键信息
                List<String> primaryKeys = dslContext
                        .select(DSL.field("column_name"))
                        .from("information_schema.key_column_usage")
                        .join("information_schema.table_constraints")
                        .on(DSL.field("information_schema.key_column_usage.constraint_name")
                                .eq(DSL.field("information_schema.table_constraints.constraint_name")))
                        .where(DSL.field("information_schema.table_constraints.table_name").eq(tableName))
                        .and(DSL.field("information_schema.table_constraints.constraint_type").eq("PRIMARY KEY"))
                        .fetchInto(String.class);
                
                // 创建列元数据
                for (Map<String, Object> column : columns) {
                    String columnName = (String) column.get("column_name");
                    String dataType = (String) column.get("data_type");
                    boolean isNullable = "YES".equals(column.get("is_nullable"));
                    String defaultValue = (String) column.get("column_default");
                    int position = ((Number) column.get("ordinal_position")).intValue();
                    boolean isPrimaryKey = primaryKeys.contains(columnName);
                    
                    SysColumnMetadata columnMetadata = new SysColumnMetadata();
                    columnMetadata.setId(UUID.randomUUID().toString());
                    columnMetadata.setSysTableMetadata(tableMetadata);
                    columnMetadata.setColumnName(columnName);
                    columnMetadata.setDataType(dataType);
                    columnMetadata.setPrimaryKey(isPrimaryKey);
                    columnMetadata.setNullable(isNullable);
                    columnMetadata.setDefaultValue(defaultValue);
                    columnMetadata.setDescription("自动同步的列");
                    columnMetadata.setOrdinalPosition(position - 1); // 数据库从1开始，我们从0开始
                    
                    columnMetadataRepository.save(columnMetadata);
                }
            }
        }
    }
}