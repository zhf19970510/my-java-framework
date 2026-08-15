package com.zhf.controller;

import com.zhf.entity.DynamicColumn;
import com.zhf.service.DynamicColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * t_dynamic_column 表的 Controller
 * <p>
 * 提供单值插入、foreach批量插入、Batch Executor批量插入、
 * SQL改写批量插入、CRUD 等 REST 接口。
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
@Slf4j
@RestController
@RequestMapping("/api/dynamic-column")
public class DynamicColumnController {

    @Autowired
    private DynamicColumnService dynamicColumnService;

    /**
     * 单条插入
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> insert(@RequestBody DynamicColumn entity) {
        StopWatch stopWatch = new StopWatch("单条插入");
        stopWatch.start("insert");
        int rows = dynamicColumnService.insert(entity);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

        Map<String, Object> result = new HashMap<>();
        result.put("affectedRows", rows);
        result.put("timeMs", stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 批量插入 - MyBatis foreach(多值INSERT)
     */
    @PostMapping("/batch/foreach")
    public ResponseEntity<Map<String, Object>> insertBatch(@RequestBody List<DynamicColumn> list) {
        StopWatch stopWatch = new StopWatch("foreach批量插入");
        stopWatch.start("insertBatch");
        int rows = dynamicColumnService.insertBatch(list);
        stopWatch.stop();
        log.info("foreach批量插入完成, 数量: {}, 耗时: {} ms", list.size(), stopWatch.getTotalTimeMillis());

        Map<String, Object> result = new HashMap<>();
        result.put("method", "foreach");
        result.put("count", list.size());
        result.put("affectedRows", rows);
        result.put("timeMs", stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 批量插入 - Batch Executor
     */
    @PostMapping("/batch/batch-executor")
    public ResponseEntity<Map<String, Object>> batchInsertWithBatchExecutor(@RequestBody List<DynamicColumn> list) {
        StopWatch stopWatch = new StopWatch("Batch Executor批量插入");
        stopWatch.start("batchInsertWithBatchExecutor");
        int rows = dynamicColumnService.batchInsertWithBatchExecutor(list);
        stopWatch.stop();
        log.info("Batch Executor批量插入完成, 数量: {}, 耗时: {} ms", list.size(), stopWatch.getTotalTimeMillis());

        Map<String, Object> result = new HashMap<>();
        result.put("method", "batch-executor");
        result.put("count", list.size());
        result.put("affectedRows", rows);
        result.put("timeMs", stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 批量插入 - SQL改写
     */
    @PostMapping("/batch/sql-rewrite")
    public ResponseEntity<Map<String, Object>> batchInsertWithSqlRewrite(@RequestBody List<DynamicColumn> list) {
        StopWatch stopWatch = new StopWatch("SQL改写批量插入");
        stopWatch.start("batchInsertWithSqlRewrite");
        int rows = dynamicColumnService.batchInsertWithSqlRewrite(list);
        stopWatch.stop();
        log.info("SQL改写批量插入完成, 数量: {}, 耗时: {} ms", list.size(), stopWatch.getTotalTimeMillis());

        Map<String, Object> result = new HashMap<>();
        result.put("method", "sql-rewrite");
        result.put("count", list.size());
        result.put("affectedRows", rows);
        result.put("timeMs", stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 更新
     */
    @PutMapping
    public ResponseEntity<Map<String, Object>> update(@RequestBody DynamicColumn entity) {
        StopWatch stopWatch = new StopWatch("更新");
        stopWatch.start("update");
        int rows = dynamicColumnService.update(entity);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

        Map<String, Object> result = new HashMap<>();
        result.put("affectedRows", rows);
        result.put("timeMs", stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 根据主键删除
     */
    @DeleteMapping("/{pkId}")
    public ResponseEntity<Map<String, Object>> deleteById(@PathVariable String pkId) {
        StopWatch stopWatch = new StopWatch("删除");
        stopWatch.start("deleteById");
        int rows = dynamicColumnService.deleteById(pkId);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

        Map<String, Object> result = new HashMap<>();
        result.put("affectedRows", rows);
        result.put("timeMs", stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok(result);
    }

    /**
     * 根据主键查询
     */
    @GetMapping("/{pkId}")
    public ResponseEntity<DynamicColumn> findById(@PathVariable String pkId) {
        StopWatch stopWatch = new StopWatch("根据主键查询");
        stopWatch.start("findById");
        DynamicColumn entity = dynamicColumnService.findById(pkId);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

        if (entity == null) {
            log.warn("记录不存在, pkId: {}", pkId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    /**
     * 查询所有记录
     */
    @GetMapping
    public ResponseEntity<List<DynamicColumn>> findAll() {
        StopWatch stopWatch = new StopWatch("查询所有记录");
        stopWatch.start("findAll");
        List<DynamicColumn> list = dynamicColumnService.findAll();
        stopWatch.stop();
        log.info("查询所有记录完成, 数量: {}, 耗时: {} ms", list.size(), stopWatch.getTotalTimeMillis());
        return ResponseEntity.ok(list);
    }
}
