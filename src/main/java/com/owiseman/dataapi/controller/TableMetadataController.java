package com.owiseman.dataapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysColumnMetadata;
import com.owiseman.dataapi.entity.SysTableMetadata;
import com.owiseman.dataapi.service.TableMetadataService;
import com.owiseman.jpa.model.DataRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
public class TableMetadataController {

    private final TableMetadataService tableService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TableMetadataController(TableMetadataService tableService, ObjectMapper objectMapper) {
        this.tableService = tableService;
        this.objectMapper = objectMapper;
    }

    /**
     * 创建表结构
     * POST /api/tables
     */
    @PostMapping
    public ResponseEntity<?> createTable(@RequestBody JsonNode tableDefinition,
                                         @RequestHeader("X-User-Id") String userId) {
        try {
            SysTableMetadata metadata = tableService.createTableWithMetadata(tableDefinition, userId);
            return ResponseEntity.created(URI.create("/api/tables/" + metadata.getId()))
                    .body(metadata);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "创建表失败",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * 删除表
     * DELETE /api/tables/{tableName}
     */
    @DeleteMapping("/{tableName}")
    public ResponseEntity<?> deleteTable(@PathVariable String tableName) {
        try {
            DataRecord result = tableService.dropTable(tableName);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", result
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 修改表结构
     * PUT /api/tables/alter
     */
    @PutMapping("/alter")
    public ResponseEntity<?> alterTable(@RequestBody JsonNode alterDefinition) {
        try {
            DataRecord result = tableService.alterTable(alterDefinition);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "修改表结构失败",
                "detail", e.getMessage()
            ));
        }
    }

    /**
     * 分页查询表元数据
     * GET /api/tables?page=1&size=20
     */
    @GetMapping
    public PageResult<SysTableMetadata> getTables(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return tableService.getTablesWithPagination(page, size);
    }

    /**
     * 获取表详细信息
     * GET /api/tables/{tableId}
     */
    @GetMapping("/{tableId}")
    public ResponseEntity<?> getTableDetail(@PathVariable String tableId) {
        return tableService.getTableById(tableId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 同步数据库元数据
     * POST /api/tables/sync
     */
    @PostMapping("/sync")
    public ResponseEntity<?> syncMetadata() {
        try {
            tableService.syncDatabaseTablesWithMetadata();
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "元数据同步完成"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "同步失败",
                "detail", e.getMessage()
            ));
        }
    }

    /**
     * 获取表的列信息
     * GET /api/tables/{tableId}/columns
     */
    @GetMapping("/{tableId}/columns")
    public ResponseEntity<?> getTableColumns(@PathVariable String tableId) {
        return tableService.getTableById(tableId)
                .map(table -> {
                    List<SysColumnMetadata> columns = tableService.getTableColumns(table.getId());
                    return ResponseEntity.ok(columns);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 查询列详细信息
     * GET /api/tables/{tableId}/columns/{columnName}
     */
    @GetMapping("/{tableId}/columns/{columnName}")
    public ResponseEntity<?> getColumnDetail(
            @PathVariable String tableId,
            @PathVariable String columnName) {
        return tableService.getTableById(tableId)
                .flatMap(table -> tableService.findColumnByTableIdAndColumnName(table, columnName))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 检查表是否存在
     * HEAD /api/tables/exists?name=tableName
     */
    @RequestMapping(value = "/exists", method = RequestMethod.HEAD)
    public ResponseEntity<?> checkTableExists(@RequestParam String name) {
        boolean exists = tableService.tableExists(name);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
