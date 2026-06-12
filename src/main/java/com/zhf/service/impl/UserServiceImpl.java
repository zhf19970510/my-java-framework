package com.zhf.service.impl;

import com.zhf.dao.UserMapper;
import com.zhf.entity.User;
import com.zhf.service.UserService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public User insert(User user) {
        user.setCreateBy("system");
        userMapper.insert(user);
        return user;
    }

    /**
     * 方案一：MyBatis Batch Executor（推荐）
     * 真正的 JDBC 批处理，性能优秀
     */
    @Override
    @Transactional
    public List<User> batchInsertWithBatchExecutor(List<User> users) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            UserMapper batchMapper = sqlSession.getMapper(UserMapper.class);
            
            for (User user : users) {
                user.setCreateBy("batch-executor");
                batchMapper.insert(user);
            }
            
            sqlSession.commit();
            
            // 刷新后获取自增主键
            List<User> resultUsers = new ArrayList<>();
            for (User user : users) {
                if (user.getUserId() != null) {
                    resultUsers.add(user);
                }
            }
            return resultUsers.isEmpty() ? users : resultUsers;
        } catch (Exception e) {
            sqlSession.rollback();
            throw new RuntimeException("Batch insert failed", e);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 方案二：JDBC Batch（最高性能）
     * 绕过 MyBatis，直接使用 JdbcTemplate
     */
    @Override
    @Transactional
    public int[] batchInsertWithJdbcBatch(List<User> users) {
        String sql = "INSERT INTO user (user_name, age, sex, place_of_origin, university, create_by, create_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                User user = users.get(i);
                ps.setString(1, user.getUserName());
                ps.setObject(2, user.getAge());
                ps.setObject(3, user.getSex());
                ps.setString(4, user.getPlaceOfOrigin());
                ps.setString(5, user.getUniversity());
                ps.setString(6, "jdbc-batch");
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }

    /**
     * 方案三：多值 INSERT（单条 SQL）
     * 注意：适合中小批量，避免 SQL 过长
     */
    @Override
    @Transactional
    public int batchInsertWithMultiValue(List<User> users) {
        if (users == null || users.isEmpty()) {
            return 0;
        }

        StringBuilder sql = new StringBuilder(
            "INSERT INTO user (user_name, age, sex, place_of_origin, university, create_by, create_time) VALUES "
        );

        List<Object> params = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append("(?, ?, ?, ?, ?, ?, NOW())");
            
            User user = users.get(i);
            params.add(user.getUserName());
            params.add(user.getAge());
            params.add(user.getSex());
            params.add(user.getPlaceOfOrigin());
            params.add(user.getUniversity());
            params.add("multi-value");
        }

        return jdbcTemplate.update(sql.toString(), params.toArray());
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public List<User> findByUserName(String userName) {
        return userMapper.findByUserName(userName);
    }

    @Override
    public List<User> findByUniversity(String university) {
        return userMapper.findByUniversity(university);
    }
}