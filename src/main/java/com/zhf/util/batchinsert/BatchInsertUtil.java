package com.zhf.util.batchinsert;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.zhf.util.spring.SpringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * 批量插入SQL改写工具类
 * <p>
 * 该工具类实现类似 MyBatis {@code <foreach>} 批量插入的功能，
 * 核心原理是通过 MyBatis 拦截器({@link com.zhf.interceptor.BatchRewriteInterceptor})捕获
 * Function 调用时产生的每条单值 INSERT 语句，然后将多条 INSERT 改写为一条多值 INSERT。
 * <p>
 * SQL改写示例：
 * <pre>
 * 改写前(多条单值INSERT):
 *   INSERT INTO t (col) VALUES (?);
 *   INSERT INTO t (col) VALUES (?);
 *   INSERT INTO t (col) VALUES (?);
 *
 * 改写后(一条多值INSERT):
 *   INSERT INTO t (col) VALUES (?, ?, ?), (?, ?, ?), (?, ?, ?);
 * </pre>
 * <p>
 * 该方法关闭自动提交，执行完所有 SQL 后才进行手动提交，
 * 包含完整的提交、回滚、关闭 session 连接逻辑。
 * <p>
 * 纯静态工具类，不注册到 Spring 容器，
 * SqlSessionFactory 通过 {@link SpringUtil#getBean(Class)} 获取。
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
@Slf4j
public class BatchInsertUtil {

    /**
     * 通过SQL改写实现批量插入(静态方法)
     * <p>
     * 使用步骤：
     * <ol>
     *   <li>调用 {@link BatchInsertCaptureContext#startCapture()} 开启捕获模式</li>
     *   <li>循环调用传入的 Function(即 mapper 的单值 insert 方法)，拦截器自动捕获每条 INSERT SQL</li>
     *   <li>从 {@link BatchInsertCaptureContext} 获取所有捕获的 SQL</li>
     *   <li>将多条 INSERT INTO ... VALUES (?) 改写为 INSERT INTO ... VALUES (?), (?), (?), ...</li>
     *   <li>打开 SqlSession(autoCommit=false)，执行改写后的 SQL</li>
     *   <li>手动提交事务；异常时回滚；最终关闭 session</li>
     * </ol>
     *
     * @param list           要批量插入的数据列表
     * @param insertFunction 单值插入的 mapper 方法引用(如 mapper::insert)
     * @param <T>            实体类型
     * @return 实际插入的行数
     */
    public static <T> int batchInsertWithSqlRewrite(List<T> list, Function<T, Integer> insertFunction) {
        if (list == null || list.isEmpty()) {
            log.warn("批量插入列表为空，跳过执行");
            return 0;
        }

        log.info("========== SQL改写批量插入开始，数据量: {} ==========", list.size());

        // 1. 开启捕获模式
        BatchInsertCaptureContext.startCapture();
        SqlSession sqlSession = null;
        try {
            // 2. 循环调用 Function，拦截器自动捕获每条 INSERT SQL(不实际执行)
            for (T item : list) {
                insertFunction.apply(item);
            }

            // 3. 获取所有捕获的 SQL
            List<BatchInsertCaptureContext.CapturedSql> capturedSqls = BatchInsertCaptureContext.getCapturedSqls();
            if (capturedSqls.isEmpty()) {
                log.warn("未捕获到任何INSERT SQL，请检查Function是否调用了mapper的insert方法");
                return 0;
            }

            // 4. SQL改写：多条单值INSERT -> 一条多值INSERT
            RewrittenSql rewrittenSql = rewriteInsertSql(capturedSqls);
            log.info("SQL改写完成 - 原始SQL数量: {}, 改写后SQL长度: {} 字符, 参数总数: {}",
                    capturedSqls.size(), rewrittenSql.sql.length(), rewrittenSql.params.size());

            // 5. 从 Spring 容器获取 SqlSessionFactory，打开 SqlSession(autoCommit=false)
            SqlSessionFactory sqlSessionFactory = SpringUtil.getBean(SqlSessionFactory.class);
            sqlSession = sqlSessionFactory.openSession(false);
            Connection conn = null;
            try {
                conn = sqlSession.getConnection();
                // 确保关闭自动提交
                if (conn.getAutoCommit()) {
                    conn.setAutoCommit(false);
                }

                // 6. 执行改写后的 SQL
                PreparedStatement ps = conn.prepareStatement(rewrittenSql.getSql());
                int paramIndex = 1;
                for (Object param : rewrittenSql.getParams()) {
                    setParameter(ps, paramIndex++, param);
                }

                log.debug("准备执行改写后的SQL，参数数量: {}", rewrittenSql.getParams().size());
                int affectedRows = ps.executeUpdate();
                ps.close();

                // 7. 手动提交事务
                // 注意：必须直接调用 conn.commit()，不能依赖 sqlSession.commit()。
                // MyBatis-Spring 的 SpringManagedTransaction 在打开连接时缓存了
                // autoCommit 值(连接池默认为 true)，后续 setAutoCommit(false)
                // 不会更新该缓存值，导致 sqlSession.commit() 误判 autoCommit=true
                // 而跳过 connection.commit()，数据无法落库。
                conn.commit();
                log.info("SQL改写批量插入成功，影响行数: {}", affectedRows);
                return affectedRows;

            } catch (Exception e) {
                // 8. 异常时回滚
                log.error("SQL改写批量插入失败，执行回滚", e);
                if (conn != null) {
                    try {
                        conn.rollback();
                        log.info("事务已回滚");
                    } catch (SQLException ex) {
                        log.error("回滚失败", ex);
                    }
                }
                throw new RuntimeException("SQL改写批量插入失败", e);
            } finally {
                // 9. 关闭 session 连接
                if (sqlSession != null) {
                    sqlSession.close();
                    log.debug("SqlSession已关闭");
                }
            }
        } finally {
            // 10. 清理捕获上下文，防止 ThreadLocal 内存泄漏
            BatchInsertCaptureContext.clear();
        }
    }

    /**
     * SQL改写核心逻辑
     * <p>
     * 将多条 INSERT INTO table (cols) VALUES (?, ?, ...)
     * 改写为一条 INSERT INTO table (cols) VALUES (?, ?, ...), (?, ?, ...), ...
     * <p>
     * 改写步骤：
     * 1. 从第一条 SQL 中提取 INSERT 前缀和 VALUES 后的值模板
     * 2. 将值模板重复 N 次(逗号分隔)
     * 3. 收集所有参数值(按顺序)
     *
     * @param capturedSqls 捕获的SQL列表
     * @return 改写后的SQL和参数
     */
    private static RewrittenSql rewriteInsertSql(List<BatchInsertCaptureContext.CapturedSql> capturedSqls) {
        BatchInsertCaptureContext.CapturedSql first = capturedSqls.get(0);
        String firstSql = first.getSql();

        // 定位 VALUES 关键字(不区分大小写)
        int valuesIdx = indexOfIgnoreCase(firstSql, "VALUES");
        if (valuesIdx == -1) {
            throw new RuntimeException("无法解析SQL，未找到VALUES关键字: " + firstSql);
        }

        // 提取前缀: "INSERT INTO table (col1, col2, ...) VALUES "
        String prefix = firstSql.substring(0, valuesIdx + 6).trim() + " ";
        // 提取值模板: "(?, ?, ...)"
        String valueTemplate = firstSql.substring(valuesIdx + 6).trim();

        // 构建改写后的 SQL
        StringBuilder sb = new StringBuilder(prefix.length() + valueTemplate.length() * capturedSqls.size() + capturedSqls.size());
        sb.append(prefix);

        List<Object> allParams = new ArrayList<>(capturedSqls.size() * first.getParams().length);

        for (int i = 0; i < capturedSqls.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(valueTemplate);

            BatchInsertCaptureContext.CapturedSql captured = capturedSqls.get(i);
            for (Object param : captured.getParams()) {
                allParams.add(param);
            }
        }

        return new RewrittenSql(sb.toString(), allParams);
    }

    /**
     * 不区分大小写地查找关键字位置
     *
     * @param sql     SQL字符串
     * @param keyword 关键字(如 "VALUES")
     * @return 关键字起始索引，未找到返回 -1
     */
    private static int indexOfIgnoreCase(String sql, String keyword) {
        String upperSql = sql.toUpperCase();
        String upperKeyword = keyword.toUpperCase();
        return upperSql.indexOf(upperKeyword);
    }

    /**
     * 设置 PreparedStatement 参数
     * <p>
     * 针对 Java 8 日期时间类型做特殊处理，转换为 java.sql 包下的类型：
     * <ul>
     *   <li>LocalDateTime -> Timestamp</li>
     *   <li>LocalDate -> java.sql.Date</li>
     *   <li>LocalTime -> java.sql.Time</li>
     *   <li>byte[] -> setBytes</li>
     *   <li>其他类型 -> setObject</li>
     * </ul>
     *
     * @param ps    PreparedStatement
     * @param index 参数索引(从1开始)
     * @param value 参数值
     * @throws SQLException SQL异常
     */
    private static void setParameter(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setNull(index, java.sql.Types.NULL);
        } else if (value instanceof byte[]) {
            ps.setBytes(index, (byte[]) value);
        } else if (value instanceof java.time.LocalDateTime) {
            ps.setTimestamp(index, Timestamp.valueOf((java.time.LocalDateTime) value));
        } else if (value instanceof java.time.LocalDate) {
            ps.setDate(index, java.sql.Date.valueOf((java.time.LocalDate) value));
        } else if (value instanceof java.time.LocalTime) {
            ps.setTime(index, Time.valueOf((java.time.LocalTime) value));
        } else if (value instanceof Date) {
            ps.setTimestamp(index, new Timestamp(((Date) value).getTime()));
        } else {
            ps.setObject(index, value);
        }
    }

    // ================================================================
    // 内部类: 改写后的SQL
    // ================================================================

    /**
     * 改写后的SQL数据结构
     */
    private static class RewrittenSql {
        private final String sql;
        private final List<Object> params;

        RewrittenSql(String sql, List<Object> params) {
            this.sql = sql;
            this.params = params;
        }

        String getSql() {
            return sql;
        }

        List<Object> getParams() {
            return params;
        }
    }
}
