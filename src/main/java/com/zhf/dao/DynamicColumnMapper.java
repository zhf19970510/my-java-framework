package com.zhf.dao;

import com.zhf.entity.DynamicColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * t_dynamic_column 表的 Mapper 接口
 * <p>
 * 提供单值插入、foreach批量插入、更新、删除、查询等 CRUD 方法。
 *
 * @author: 曾鸿发
 * @create: 2026-08-15
 */
@Mapper
public interface DynamicColumnMapper {

    /**
     * 单条插入
     * <p>
     * 注意：主键 pk_id 由业务代码塞值(UUID)，不使用自增主键。
     *
     * @param entity 实体对象
     * @return 影响行数
     */
    int insert(DynamicColumn entity);

    /**
     * 批量插入 - 使用 MyBatis foreach 标签拼装多值 INSERT
     * <p>
     * 将 List 拼装为 INSERT INTO ... VALUES (...), (...), (...) 形式，
     * 一次性发送给 MySQL 执行。
     *
     * @param list 实体列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<DynamicColumn> list);

    /**
     * 根据主键更新
     *
     * @param entity 实体对象(pk_id 不能为空)
     * @return 影响行数
     */
    int update(DynamicColumn entity);

    /**
     * 根据主键删除
     *
     * @param pkId 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("pkId") String pkId);

    /**
     * 根据主键查询
     *
     * @param pkId 主键ID
     * @return 实体对象
     */
    DynamicColumn findById(@Param("pkId") String pkId);

    /**
     * 查询所有记录
     *
     * @return 实体列表
     */
    List<DynamicColumn> findAll();

    /**
     * 清空表数据(用于测试前清理)
     *
     * @return 影响行数
     */
    int deleteAll();
}
