package com.zhf;

import com.zhf.entity.DynamicColumn;
import com.zhf.service.DynamicColumnService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * t_dynamic_column 批量插入性能对比测试
 * <p>
 * 测试三种批量插入方式的性能差异：
 * <ol>
 *   <li>For循环调用单值insert：每条数据一个事务，性能最差</li>
 *   <li>SqlSession BATCH模式：sqlSessionFactory.openSession(ExecutorType.BATCH, false)，
 *       批量执行单值insert，最后统一commit</li>
 *   <li>SQL改写批量插入：通过拦截器捕获多条INSERT，改写为一条多值INSERT执行</li>
 * </ol>
 * 测试数据量：100、1000、3000条，使用StopWatch监控打印。
 * 主键使用UUID生成，方便重复测试。
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJavaFrameworkApplication.class)
public class DynamicColumnBatchInsertTest {

    @Autowired
    private DynamicColumnService dynamicColumnService;

    /**
     * 每个测试方法执行前清理表数据，确保测试环境干净
     */
    @Before
    public void setUp() {
        dynamicColumnService.deleteAll();
    }

    // ================================================================
    // 辅助方法：生成测试数据
    // ================================================================

    /**
     * 创建单个测试实体(覆盖所有数据类型)
     *
     * @param index 序号，用于生成有区分度的数据
     * @return 填充所有字段的 DynamicColumn
     */
    private DynamicColumn createTestEntity(int index) {
        DynamicColumn entity = new DynamicColumn();
        // 主键使用UUID，方便重复测试
        entity.setPkId(UUID.randomUUID().toString());

        // 整数类型
        entity.setColTinyint(index % 127);
        entity.setColSmallint(index % 32767);
        entity.setColMediumint(index);
        entity.setColInt(index);
        entity.setColBigint((long) index * 1000L);
        entity.setColUnsignedInt((long) index);

        // 定点与浮点类型
        entity.setColDecimal(new BigDecimal(index + ".1234"));
        entity.setColFloat(index * 1.5f);
        entity.setColDouble(index * 2.5);

        // 位类型
        entity.setColBit(new byte[]{(byte) (index % 128)});

        // 字符串类型
        entity.setColChar(String.format("c%08d", index));
        entity.setColVarchar("varchar_test_" + index);
        entity.setColTinytext("tinytext_" + index);
        entity.setColText("text_content_" + index);
        entity.setColMediumtext("mediumtext_content_" + index);
        entity.setColLongtext("longtext_content_" + index);

        // 二进制类型
        byte[] binaryData = new byte[16];
        binaryData[0] = (byte) (index % 128);
        entity.setColBinary(binaryData);
        entity.setColVarbinary(("varbinary_" + index).getBytes());
        entity.setColBlob(("blob_data_" + index).getBytes());

        // 日期时间类型
        entity.setColDate(LocalDate.now());
        entity.setColTime(LocalTime.now());
        entity.setColDatetime(LocalDateTime.now());
        entity.setColTimestamp(LocalDateTime.now());
        entity.setColYear(LocalDate.now().getYear());

        // 枚举与集合类型
        entity.setColEnum("ENABLED");
        entity.setColSet("READ");

        // JSON类型
        entity.setColJson("{\"key\":\"value_" + index + "\"}");

        // 审计字段
        entity.setCreateBy("test");
        entity.setCreateTime(LocalDateTime.now());

        return entity;
    }

    /**
     * 批量创建测试实体列表
     *
     * @param count 数量
     * @return 实体列表
     */
    private List<DynamicColumn> createTestEntities(int count) {
        List<DynamicColumn> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(createTestEntity(i));
        }
        return list;
    }

    // ================================================================
    // 方式一：For循环单值插入测试
    // ================================================================

    /**
     * For循环单值插入 - 100条
     */
    @Test
    public void testForLoopInsert_100() {
        testForLoopInsert(100);
    }

    /**
     * For循环单值插入 - 1000条
     */
    @Test
    public void testForLoopInsert_1000() {
        testForLoopInsert(1000);
    }

    /**
     * For循环单值插入 - 3000条
     */
    @Test
    public void testForLoopInsert_3000() {
        testForLoopInsert(3000);
    }

    /**
     * For循环单值插入通用方法
     * <p>
     * 每条数据调用一次 service.insert()，每条数据一个独立事务，性能最差。
     *
     * @param count 数据量
     */
    private void testForLoopInsert(int count) {
        List<DynamicColumn> list = createTestEntities(count);
        log.info("========== For循环单值插入开始，数据量: {} ==========", count);

        StopWatch stopWatch = new StopWatch("For循环单值插入-" + count + "条");
        stopWatch.start("for-loop-insert");
        for (DynamicColumn entity : list) {
            dynamicColumnService.insert(entity);
        }
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
        log.info("For循环单值插入完成，数据量: {}，总耗时: {} ms", count, stopWatch.getTotalTimeMillis());
    }

    // ================================================================
    // 方式二：SqlSession BATCH模式批量插入测试
    // ================================================================

    /**
     * Batch Executor批量插入 - 100条
     */
    @Test
    public void testBatchExecutorInsert_100() {
        testBatchExecutorInsert(100);
    }

    /**
     * Batch Executor批量插入 - 1000条
     */
    @Test
    public void testBatchExecutorInsert_1000() {
        testBatchExecutorInsert(1000);
    }

    /**
     * Batch Executor批量插入 - 3000条
     */
    @Test
    public void testBatchExecutorInsert_3000() {
        testBatchExecutorInsert(3000);
    }

    /**
     * Batch Executor批量插入通用方法
     * <p>
     * 通过 sqlSessionFactory.openSession(ExecutorType.BATCH, false) 打开会话，
     * 循环调用单值 insert，最后统一 commit。
     *
     * @param count 数据量
     */
    private void testBatchExecutorInsert(int count) {
        List<DynamicColumn> list = createTestEntities(count);
        log.info("========== Batch Executor批量插入开始，数据量: {} ==========", count);

        StopWatch stopWatch = new StopWatch("Batch Executor批量插入-" + count + "条");
        stopWatch.start("batch-executor-insert");
        dynamicColumnService.batchInsertWithBatchExecutor(list);
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
        log.info("Batch Executor批量插入完成，数据量: {}，总耗时: {} ms", count, stopWatch.getTotalTimeMillis());
    }

    // ================================================================
    // 方式三：SQL改写批量插入测试
    // ================================================================

    /**
     * SQL改写批量插入 - 100条
     */
    @Test
    public void testSqlRewriteInsert_100() {
        testSqlRewriteInsert(100);
    }

    /**
     * SQL改写批量插入 - 1000条
     */
    @Test
    public void testSqlRewriteInsert_1000() {
        testSqlRewriteInsert(1000);
    }

    /**
     * SQL改写批量插入 - 3000条
     */
    @Test
    public void testSqlRewriteInsert_3000() {
        testSqlRewriteInsert(3000);
    }

    /**
     * SQL改写批量插入通用方法
     * <p>
     * 通过拦截器捕获多条单值INSERT，改写为一条多值INSERT执行。
     *
     * @param count 数据量
     */
    private void testSqlRewriteInsert(int count) {
        List<DynamicColumn> list = createTestEntities(count);
        log.info("========== SQL改写批量插入开始，数据量: {} ==========", count);

        StopWatch stopWatch = new StopWatch("SQL改写批量插入-" + count + "条");
        stopWatch.start("sql-rewrite-insert");
        dynamicColumnService.batchInsertWithSqlRewrite(list);
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
        log.info("SQL改写批量插入完成，数据量: {}，总耗时: {} ms", count, stopWatch.getTotalTimeMillis());
    }

    // ================================================================
    // 综合性能对比测试(三种方式同台对比)
    // ================================================================

    /**
     * 综合性能对比 - 100条
     */
    @Test
    public void testBenchmark_100() {
        testBenchmark(100);
    }

    /**
     * 综合性能对比 - 1000条
     */
    @Test
    public void testBenchmark_1000() {
        testBenchmark(1000);
    }

    /**
     * 综合性能对比 - 3000条
     */
    @Test
    public void testBenchmark_3000() {
        testBenchmark(3000);
    }


    /**
     * 综合性能对比 - 10000条
     */
    @Test
    public void testBenchmark_10000() {
        testBenchmark(10000);
    }

    /**
     * 综合性能对比测试通用方法
     * <p>
     * 在同一个 StopWatch 中分别测试三种插入方式，打印对比报告。
     * 每种方式测试前清理表数据，确保公平对比。
     *
     * @param count 数据量
     */
    private void testBenchmark(int count) {
        log.info("################## 批量插入性能对比测试开始，数据量: {} ##################", count);

        StopWatch stopWatch = new StopWatch("批量插入性能对比-" + count + "条");

        // ========== 方式一：For循环单值插入 ==========
        List<DynamicColumn> list1 = createTestEntities(count);
        stopWatch.start("For循环单值插入");
        for (DynamicColumn entity : list1) {
            dynamicColumnService.insert(entity);
        }
        stopWatch.stop();
        log.info("[{}] For循环单值插入耗时: {} ms", count, stopWatch.getLastTaskTimeMillis());

        // 清理表，确保下一种方式从空表开始
        dynamicColumnService.deleteAll();

        // ========== 方式二：Batch Executor批量插入 ==========
        List<DynamicColumn> list2 = createTestEntities(count);
        stopWatch.start("Batch Executor批量插入");
        dynamicColumnService.batchInsertWithBatchExecutor(list2);
        stopWatch.stop();
        log.info("[{}] Batch Executor批量插入耗时: {} ms", count, stopWatch.getLastTaskTimeMillis());

        // 清理表
        dynamicColumnService.deleteAll();

        // ========== 方式三：SQL改写批量插入 ==========
        List<DynamicColumn> list3 = createTestEntities(count);
        stopWatch.start("SQL改写批量插入");
        dynamicColumnService.batchInsertWithSqlRewrite(list3);
        stopWatch.stop();
        log.info("[{}] SQL改写批量插入耗时: {} ms", count, stopWatch.getLastTaskTimeMillis());

        // 打印综合对比报告
        log.info("################## 批量插入性能对比报告 (数据量: {}) ##################", count);
        log.info(stopWatch.prettyPrint());

        long forLoopMs = stopWatch.getTaskInfo()[0].getTimeMillis();
        long batchExecutorMs = stopWatch.getTaskInfo()[1].getTimeMillis();
        long sqlRewriteMs = stopWatch.getTaskInfo()[2].getTimeMillis();

        log.info("--------------------------------------------------------");
        log.info("| 方式               | 耗时(ms) |  相对最慢  |");
        log.info("|--------------------|----------|------------|");
        log.info("| For循环单值插入     | {} |   100.0%   |", padLeft(forLoopMs, 8));
        log.info("| Batch Executor      | {} |   {}%   |", padLeft(batchExecutorMs, 8), String.format("%5.1f", batchExecutorMs * 100.0 / forLoopMs));
        log.info("| SQL改写批量插入     | {} |   {}%   |", padLeft(sqlRewriteMs, 8), String.format("%5.1f", sqlRewriteMs * 100.0 / forLoopMs));
        log.info("--------------------------------------------------------");
    }

    /**
     * 将数字左补空格到指定宽度(用于日志对齐)
     *
     * @param value  数值
     * @param width  目标宽度
     * @return 补空格后的字符串
     */
    private String padLeft(long value, int width) {
        String s = String.valueOf(value);
        while (s.length() < width) {
            s = " " + s;
        }
        return s;
    }
}
