package com.zhf.util.batchinsert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 批量插入SQL捕获上下文
 * <p>
 * 基于 ThreadLocal 实现的线程级捕获上下文，配合 {@link com.zhf.interceptor.BatchRewriteInterceptor} 使用。
 * <p>
 * 工作原理：
 * <ol>
 *   <li>调用 {@link #startCapture()} 开启捕获模式</li>
 *   <li>拦截器拦截到 INSERT 语句时，调用 {@link #addCapturedSql} 将SQL和参数缓存到 ThreadLocal</li>
 *   <li>调用 {@link #getCapturedSqls()} 获取所有捕获的SQL</li>
 *   <li>调用 {@link #clear()} 清理 ThreadLocal 资源</li>
 * </ol>
 * 注意：务必在 finally 块中调用 {@link #clear()} 以防止 ThreadLocal 内存泄漏。
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
public class BatchInsertCaptureContext {

    /** 捕获模式标记 */
    private static final ThreadLocal<Boolean> CAPTURING = new ThreadLocal<>();

    /** 捕获的SQL列表 */
    private static final ThreadLocal<List<CapturedSql>> CAPTURED_SQLS = new ThreadLocal<>();

    /**
     * 开启捕获模式，并清空之前捕获的数据
     */
    public static void startCapture() {
        CAPTURING.set(Boolean.TRUE);
        CAPTURED_SQLS.set(new ArrayList<>());
    }

    /**
     * 判断当前线程是否处于捕获模式
     *
     * @return true表示正在捕获，false表示正常执行
     */
    public static boolean isCapturing() {
        Boolean flag = CAPTURING.get();
        return flag != null && flag;
    }

    /**
     * 添加一条捕获的SQL及其参数
     *
     * @param sql    原始SQL(含?占位符)
     * @param params SQL对应的参数值数组
     */
    public static void addCapturedSql(String sql, Object[] params) {
        List<CapturedSql> list = CAPTURED_SQLS.get();
        if (list == null) {
            list = new ArrayList<>();
            CAPTURED_SQLS.set(list);
        }
        list.add(new CapturedSql(sql, params));
    }

    /**
     * 获取当前线程已捕获的所有SQL
     *
     * @return 捕获的SQL列表
     */
    public static List<CapturedSql> getCapturedSqls() {
        List<CapturedSql> list = CAPTURED_SQLS.get();
        return list != null ? list : new ArrayList<>();
    }

    /**
     * 清理 ThreadLocal 资源，防止内存泄漏
     */
    public static void clear() {
        CAPTURING.remove();
        CAPTURED_SQLS.remove();
    }

    // ================================================================
    // 内部类: 捕获的SQL
    // ================================================================

    /**
     * 捕获的SQL数据结构
     */
    public static class CapturedSql {

        /** 原始SQL(含?占位符) */
        private final String sql;

        /** SQL对应的参数值数组 */
        private final Object[] params;

        public CapturedSql(String sql, Object[] params) {
            this.sql = sql;
            this.params = params;
        }

        public String getSql() {
            return sql;
        }

        public Object[] getParams() {
            return params;
        }

        @Override
        public String toString() {
            return "CapturedSql{" +
                    "sql='" + sql + '\'' +
                    ", params=" + Arrays.toString(params) +
                    '}';
        }
    }
}
