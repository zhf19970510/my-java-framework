package com.zhf.service;


import com.zhf.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 单条插入
     */
    User insert(User user);

    /**
     * 批量插入 - 使用 MyBatis Batch Executor
     */
    List<User> batchInsertWithBatchExecutor(List<User> users);

    /**
     * 批量插入 - 使用 JDBC Batch
     */
    int[] batchInsertWithJdbcBatch(List<User> users);

    /**
     * 批量插入 - 使用多值 INSERT
     */
    int batchInsertWithMultiValue(List<User> users);

    /**
     * 查询所有用户
     */
    List<User> findAll();

    /**
     * 根据ID查询
     */
    User findById(Long userId);

    /**
     * 根据姓名模糊查询
     */
    List<User> findByUserName(String userName);

    /**
     * 根据院校查询
     */
    List<User> findByUniversity(String university);
}