package com.zhf.controller;

import com.zhf.entity.User;
import com.zhf.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 单条插入
     */
    @PostMapping
    public ResponseEntity<User> insert(@RequestBody User user) {
        StopWatch stopWatch = new StopWatch("单条插入");
        stopWatch.start("insert");
        User savedUser = userService.insert(user);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        return ResponseEntity.ok(savedUser);
    }

    /**
     * 批量插入 - MyBatis Batch Executor
     */
    @PostMapping("/batch/batch-executor")
    public ResponseEntity<Map<String, Object>> batchInsertWithBatchExecutor(@RequestBody List<User> users) {
        StopWatch stopWatch = new StopWatch("MyBatis Batch Executor 批量插入");
        stopWatch.start("batchInsert");
        List<User> savedUsers = userService.batchInsertWithBatchExecutor(users);
        stopWatch.stop();

        Map<String, Object> result = new HashMap<>();
        result.put("method", "MyBatis Batch Executor");
        result.put("count", savedUsers.size());
        result.put("timeMs", stopWatch.getTotalTimeMillis());
        result.put("users", savedUsers);

        log.info("批量插入完成，方法: {}, 数量: {}, 耗时: {} ms",
                result.get("method"), result.get("count"), result.get("timeMs"));
        return ResponseEntity.ok(result);
    }

    /**
     * 批量插入 - JDBC Batch
     */
    @PostMapping("/batch/jdbc-batch")
    public ResponseEntity<Map<String, Object>> batchInsertWithJdbcBatch(@RequestBody List<User> users) {
        StopWatch stopWatch = new StopWatch("JDBC Batch 批量插入");
        stopWatch.start("batchInsert");
        int[] updateCounts = userService.batchInsertWithJdbcBatch(users);
        stopWatch.stop();

        Map<String, Object> result = new HashMap<>();
        result.put("method", "JDBC Batch");
        result.put("count", updateCounts.length);
        result.put("timeMs", stopWatch.getTotalTimeMillis());

        log.info("批量插入完成，方法: {}, 数量: {}, 耗时: {} ms",
                result.get("method"), result.get("count"), result.get("timeMs"));
        return ResponseEntity.ok(result);
    }

    /**
     * 批量插入 - 多值 INSERT
     */
    @PostMapping("/batch/multi-value")
    public ResponseEntity<Map<String, Object>> batchInsertWithMultiValue(@RequestBody List<User> users) {
        StopWatch stopWatch = new StopWatch("多值 INSERT 批量插入");
        stopWatch.start("batchInsert");
        int affectedRows = userService.batchInsertWithMultiValue(users);
        stopWatch.stop();

        Map<String, Object> result = new HashMap<>();
        result.put("method", "多值 INSERT");
        result.put("count", affectedRows);
        result.put("timeMs", stopWatch.getTotalTimeMillis());

        log.info("批量插入完成，方法: {}, 数量: {}, 耗时: {} ms",
                result.get("method"), result.get("count"), result.get("timeMs"));
        return ResponseEntity.ok(result);
    }

    /**
     * 查询所有用户
     */
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        StopWatch stopWatch = new StopWatch("查询所有用户");
        stopWatch.start("findAll");
        List<User> users = userService.findAll();
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        return ResponseEntity.ok(users);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> findById(@PathVariable Long userId) {
        StopWatch stopWatch = new StopWatch("根据ID查询用户");
        stopWatch.start("findById");
        User user = userService.findById(userId);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());

        if (user == null) {
            log.warn("用户不存在，userId: {}", userId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 根据姓名模糊查询
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<User>> findByUserName(@RequestParam String userName) {
        StopWatch stopWatch = new StopWatch("根据姓名查询用户");
        stopWatch.start("findByUserName");
        List<User> users = userService.findByUserName(userName);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        return ResponseEntity.ok(users);
    }

    /**
     * 根据院校查询
     */
    @GetMapping("/search/university")
    public ResponseEntity<List<User>> findByUniversity(@RequestParam String university) {
        StopWatch stopWatch = new StopWatch("根据院校查询用户");
        stopWatch.start("findByUniversity");
        List<User> users = userService.findByUniversity(university);
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        return ResponseEntity.ok(users);
    }

    /**
     * 批量插入性能对比测试
     */
    @PostMapping("/batch/benchmark")
    public ResponseEntity<Map<String, Object>> benchmark(@RequestBody List<User> users) {
        StopWatch overallStopWatch = new StopWatch("批量插入性能对比测试");
        Map<String, Object> results = new HashMap<>();
        int totalUsers = users.size();

        log.info("========== 批量插入性能对比测试开始，总用户数: {} ==========", totalUsers);

        // 测试 Batch Executor
        overallStopWatch.start("MyBatis Batch Executor");
        userService.batchInsertWithBatchExecutor(cloneList(users));
        overallStopWatch.stop();
        long batchExecutorMs = overallStopWatch.getLastTaskTimeMillis();
        results.put("batchExecutorMs", batchExecutorMs);
        log.info("MyBatis Batch Executor 耗时: {} ms", batchExecutorMs);

        // 测试 JDBC Batch
        overallStopWatch.start("JDBC Batch");
        userService.batchInsertWithJdbcBatch(cloneList(users));
        overallStopWatch.stop();
        long jdbcBatchMs = overallStopWatch.getLastTaskTimeMillis();
        results.put("jdbcBatchMs", jdbcBatchMs);
        log.info("JDBC Batch 耗时: {} ms", jdbcBatchMs);

        // 测试多值 INSERT（限制大小避免 SQL 过长）
        int limitSize = Math.min(totalUsers, 500);
        overallStopWatch.start("多值 INSERT");
        userService.batchInsertWithMultiValue(cloneList(users).subList(0, limitSize));
        overallStopWatch.stop();
        long multiValueMs = overallStopWatch.getLastTaskTimeMillis();
        results.put("multiValueMs", multiValueMs);
        log.info("多值 INSERT 耗时: {} ms (限制前 {} 条)", multiValueMs, limitSize);

        results.put("totalUsers", totalUsers);
        results.put("note", "多值 INSERT 限制为前 " + limitSize + " 条");

        log.info("========== 批量插入性能对比测试结束 ==========");
        log.info("\n{}", overallStopWatch.prettyPrint());

        return ResponseEntity.ok(results);
    }

    /**
     * 克隆列表（避免同一对象多次插入）
     */
    private List<User> cloneList(List<User> users) {
        return users.stream()
            .map(u -> {
                User clone = new User();
                clone.setUserName(u.getUserName());
                clone.setAge(u.getAge());
                clone.setSex(u.getSex());
                clone.setPlaceOfOrigin(u.getPlaceOfOrigin());
                clone.setUniversity(u.getUniversity());
                return clone;
            })
            .collect(Collectors.toList());
    }
}