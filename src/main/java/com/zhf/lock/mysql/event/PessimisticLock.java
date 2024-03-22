package com.zhf.lock.mysql.event;

import com.zhf.lock.mysql.service.MySQLDistributedLockService;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mysql 分布式锁-悲观锁
 *     执行流程：多个进程抢占同一个商品，执行业务完毕则通过connection.commit() 释放锁
 *     锁机制：单一进程获取锁时，则其他进程将阻塞等待
 *
 * 注意事项：
 *      1. 阻塞锁
 *      2. 存在加锁开销
 *      3. 性能比较低
 */
@Slf4j
public class PessimisticLock extends Thread {

    @Override
    public void run() {
        super.run();
        ResultSet resultSet = null;
        String goodName = null;
        int goodCount = 0;
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        //关闭自动提交
        MySQLDistributedLockService.closeAutoCommit();
        try{
            while(true){
                log.info("当前线程：" + pid + "");
                //获取库存
                resultSet = MySQLDistributedLockService.getGoodCount2(1);
                while (resultSet.next()) {
                    goodName = resultSet.getString("good_name");
                    goodCount = resultSet.getInt("good_count");
                }
                log.info("获取库存成功，当前商品名称为:" + goodName + ",当前库存剩余量为:" + goodCount);
                // 模拟执行业务事件
                Thread.sleep(2 * 1000);
                if (0 == goodCount) {
                    log.info("抢购失败，当前库存为0！");
                    break;
                }
                // 抢购商品
                if (MySQLDistributedLockService.setGoodCount2(1)) {
                    // 模拟延时，防止锁每次被同一进程获取
                    MySQLDistributedLockService.commit(pid, goodName, goodCount);
                    Thread.sleep(2 * 1000);
                } else {
                    log.error("抢购商品:" + goodName + "失败!");
                }
            }
        }catch (Exception e){
            //抢购失败
            log.error("抢购商品发生错误！",e);
            try {
                MySQLDistributedLockService.rollBack();
            } catch (SQLException ex) {
                log.error("回滚失败！ ",e);
            }
        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error("Result关闭失败！",e);
                }
            }
            MySQLDistributedLockService.close();
        }
    }

    public static void main(String[] args) {
        new PessimisticLock().start();
    }
}