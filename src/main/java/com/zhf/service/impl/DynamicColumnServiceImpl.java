package com.zhf.service.impl;

import com.zhf.dao.DynamicColumnMapper;
import com.zhf.entity.DynamicColumn;
import com.zhf.service.DynamicColumnService;
import com.zhf.util.batchinsert.BatchInsertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * t_dynamic_column 表的 Service 实现类
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
@Slf4j
@Service
public class DynamicColumnServiceImpl implements DynamicColumnService {

    @Autowired
    private DynamicColumnMapper dynamicColumnMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 单条插入
     */
    @Override
    @Transactional
    public int insert(DynamicColumn entity) {
        log.info("单条插入, pkId: {}", entity.getPkId());
        return dynamicColumnMapper.insert(entity);
    }

    /**
     * 批量插入 - 使用 MyBatis foreach 标签(多值INSERT)
     */
    @Override
    @Transactional
    public int insertBatch(List<DynamicColumn> list) {
        log.info("foreach批量插入, 数据量: {}", list.size());
        int rows = dynamicColumnMapper.insertBatch(list);
        log.info("foreach批量插入完成, 影响行数: {}", rows);
        return rows;
    }

    /**
     * 批量插入 - 使用 SqlSession BATCH 模式
     * <p>
     * 通过 sqlSessionFactory.openSession(ExecutorType.BATCH, false) 打开会话，
     * 循环调用单值 insert，BATCH 模式下 SQL 只积攒到 addBatch 不实际执行，
     * 最后统一 flushStatements 触发 executeBatch，再手动 commit。
     * 包含完整的提交、回滚、关闭逻辑。
     */
    @Override
    public int batchInsertWithBatchExecutor(List<DynamicColumn> list) {
        log.info("Batch Executor批量插入开始, 数据量: {}", list.size());

        // 打开 BATCH 模式的 SqlSession
        // 注意：openSession 的 autoCommit 参数会被 SpringManagedTransactionFactory 忽略，
        // 因此不能依赖 sqlSession.commit() 来提交事务
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        Connection conn = null;
        try {
            DynamicColumnMapper batchMapper = sqlSession.getMapper(DynamicColumnMapper.class);

            conn = sqlSession.getConnection();
            // 关闭自动提交，开启事务
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
            }

            // 循环调用单值 insert，BATCH 模式下只积攒到 addBatch，不实际执行
            for (DynamicColumn entity : list) {
                batchMapper.insert(entity);
            }

            // 触发 executeBatch 执行所有积攒的 SQL
            sqlSession.flushStatements();

            // 手动提交事务
            // 注意：必须直接调用 conn.commit()，不能依赖 sqlSession.commit()。
            // 与 BatchInsertUtil.batchInsertWithSqlRewrite 同理：
            // SpringManagedTransaction 缓存了 autoCommit=true，
            // sqlSession.commit() 会跳过 connection.commit()，导致事务未提交。
            conn.commit();
            log.info("Batch Executor批量插入完成, 数据量: {}", list.size());
            return list.size();

        } catch (Exception e) {
            // 异常时回滚
            log.error("Batch Executor批量插入失败，执行回滚", e);
            if (conn != null) {
                try {
                    conn.rollback();
                    log.info("事务已回滚");
                } catch (SQLException ex) {
                    log.error("回滚失败", ex);
                }
            }
            throw new RuntimeException("Batch Executor批量插入失败", e);
        } finally {
            // 关闭 session 连接
            if (sqlSession != null) {
                sqlSession.close();
                log.debug("Batch Executor SqlSession已关闭");
            }
        }
    }

    /**
     * 批量插入 - 使用 SQL改写工具类
     * <p>
     * 委托 BatchInsertUtil，通过拦截器捕获多条单值INSERT，
     * 改写为一条多值INSERT执行。
     */
    @Override
    public int batchInsertWithSqlRewrite(List<DynamicColumn> list) {
        log.info("SQL改写批量插入开始, 数据量: {}", list.size());
        // 静态调用工具类，传入单值 insert 方法引用，由工具类完成 SQL 捕获和改写
        int rows = BatchInsertUtil.batchInsertWithSqlRewrite(list, dynamicColumnMapper::insert);
        log.info("SQL改写批量插入完成, 影响行数: {}", rows);
        return rows;
    }

    /**
     * 根据主键更新
     */
    @Override
    @Transactional
    public int update(DynamicColumn entity) {
        log.info("更新记录, pkId: {}", entity.getPkId());
        return dynamicColumnMapper.update(entity);
    }

    /**
     * 根据主键删除
     */
    @Override
    @Transactional
    public int deleteById(String pkId) {
        log.info("删除记录, pkId: {}", pkId);
        return dynamicColumnMapper.deleteById(pkId);
    }

    /**
     * 根据主键查询
     */
    @Override
    public DynamicColumn findById(String pkId) {
        log.info("根据主键查询, pkId: {}", pkId);
        return dynamicColumnMapper.findById(pkId);
    }

    /**
     * 查询所有记录
     */
    @Override
    public List<DynamicColumn> findAll() {
        log.info("查询所有记录");
        return dynamicColumnMapper.findAll();
    }

    /**
     * 清空表数据
     */
    @Override
    public int deleteAll() {
        log.info("清空表数据");
        return dynamicColumnMapper.deleteAll();
    }
}
