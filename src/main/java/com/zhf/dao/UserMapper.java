package com.zhf.dao;

import com.zhf.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 单条插入
     */
    int insert(User user);

    /**
     * 批量插入 - 使用 MyBatis Batch Executor
     * 注意：此方法配合 Service 层的 Batch Executor 使用
     */
    int insertBatch(@Param("list") List<User> users);

    /**
     * 查询所有用户
     */
    List<User> findAll();

    /**
     * 根据ID查询
     */
    User findById(@Param("userId") Long userId);

    /**
     * 根据姓名模糊查询
     */
    List<User> findByUserName(@Param("userName") String userName);

    /**
     * 根据院校查询
     */
    List<User> findByUniversity(@Param("university") String university);
}