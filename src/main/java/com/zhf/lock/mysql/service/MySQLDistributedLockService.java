package com.zhf.lock.mysql.service;

import com.zhf.lock.mysql.util.JDBCUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * MySQL 锁操作类（加锁+释放锁）
 */
@Slf4j
public class MySQLDistributedLockService {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    static{
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            resultSet = null;
        } catch (SQLException e) {
            log.error("数据库连接失败！");
        }
    }

    /**
     * 锁表 - 获取锁
     * @param resource      资源
     * @param description   锁描述
     * @return  是否操作成功
     */
    public static boolean tryLock(int resource,String description){

        String sql = "insert into database_lock (resource,description) values (" + resource + ", '" + description + "');";

        //获取数据库连接
        try {
            int stat = statement.executeUpdate(sql);
            return stat == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 锁表-释放锁
     * @return
     */
    public static boolean releaseLock(int resource) throws SQLException {
        String sql = "delete from database_lock where resource = " + resource;
        //获取数据库连接
        int stat = statement.executeUpdate(sql);
        return stat == 1;
    }

    /**
     * 关闭连接
     */
    public static void close(){
        log.info("当前线程： " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0] +
                ",关闭了数据库连接！");
        JDBCUtils.close(resultSet,statement,connection);
    }

    /**
     * 乐观锁-获取资源
     * @param id 资源ID
     * @return result
     */
    public static ResultSet getGoodCount(int id) throws SQLException {

        String sql = "select  * from  database_lock_2 where id = " + id;

        //查询数据
        resultSet = statement.executeQuery(sql);
        return resultSet;
    }


    /**
     * 乐观锁-修改资源
     * @param id        资源ID
     * @param goodCount 资源
     * @return  修改状态
     */
    public static boolean setGoodCount(int id, int goodCount) throws SQLException {

        String sql = "update database_lock_2 set good_count = good_count - 1 where id =" + id +"  and good_count = " + goodCount;

        int stat = statement.executeUpdate(sql);
        return stat == 1;
    }

    /**
     * 乐观锁-开启事务自动提交
     */
    public static void AutoCommit(){
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            log.error("开启自动提交！",e);
        }
    }

    /**
     * 悲观锁-获取资源
     * @param id 资源ID
     * @return result
     */
    public static ResultSet getGoodCount2(int id) throws SQLException {

        String sql = "select  * from  database_lock_2 where id = " + id + "for update";

        //查询数据
        resultSet = statement.executeQuery(sql);
        return resultSet;
    }

    /**
     * 悲观锁-修改资源
     * @param id        资源ID
     * @return  修改状态
     */
    public static boolean setGoodCount2(int id) throws SQLException {

        String sql = "update database_lock_2 set good_count = good_count - 1 where id =" + id;

        int stat = statement.executeUpdate(sql);
        return stat == 1;
    }

    /**
     * 悲观锁-关闭事务自动提交
     */
    public static void closeAutoCommit(){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("关闭自动提交失败！",e);
        }
    }

    /**
     * 悲观锁-提交事务
     */
    public static void commit(String pid,String goodName,int goodCount) throws SQLException {
        connection.commit();
        log.info("当前线程：" + pid + "抢购商品： " + goodName + "成功，剩余库存为：" + (goodCount-1));
    }

    /**
     * 悲观锁-回滚
     */
    public static void rollBack() throws SQLException {
        connection.rollback();
    }
}