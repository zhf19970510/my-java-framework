package com.zhf.service;

import com.zhf.entity.DynamicColumn;

import java.util.List;

/**
 * t_dynamic_column 表的 Service 接口
 * <p>
 * 提供单值插入、foreach批量插入、Batch Executor批量插入、
 * SQL改写批量插入、CRUD 等方法。
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
public interface DynamicColumnService {

    /**
     * 单条插入
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int insert(DynamicColumn entity);

    /**
     * 批量插入 - 使用 MyBatis foreach 标签(多值INSERT)
     *
     * @param list 实体列表
     * @return 影响行数
     */
    int insertBatch(List<DynamicColumn> list);

    /**
     * 批量插入 - 使用 SqlSession BATCH 模式
     * <p>
     * 通过 sqlSessionFactory.openSession(ExecutorType.BATCH, false) 打开会话，
     * 循环调用单值 insert，最后统一 commit。
     *
     * @param list 实体列表
     * @return 插入数量
     */
    int batchInsertWithBatchExecutor(List<DynamicColumn> list);

    /**
     * 批量插入 - 使用 SQL改写工具类
     * <p>
     * 通过拦截器捕获多条单值INSERT，改写为一条多值INSERT执行。
     *
     * @param list 实体列表
     * @return 影响行数
     */
    int batchInsertWithSqlRewrite(List<DynamicColumn> list);

    /**
     * 根据主键更新
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int update(DynamicColumn entity);

    /**
     * 根据主键删除
     *
     * @param pkId 主键ID
     * @return 影响行数
     */
    int deleteById(String pkId);

    /**
     * 根据主键查询
     *
     * @param pkId 主键ID
     * @return 实体对象
     */
    DynamicColumn findById(String pkId);

    /**
     * 查询所有记录
     *
     * @return 实体列表
     */
    List<DynamicColumn> findAll();

    /**
     * 清空表数据
     *
     * @return 影响行数
     */
    int deleteAll();
}
