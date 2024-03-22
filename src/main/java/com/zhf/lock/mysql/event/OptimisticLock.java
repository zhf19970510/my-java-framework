package com.zhf.lock.mysql.event;

import com.zhf.lock.mysql.service.MySQLDistributedLockService;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mysql分布式锁-乐观锁
 *  执行流程： 多进程抢购同一商品，每次抢购成功商品数量-1，商品数据量为0时退出
 *  锁机制： 单一进程获取锁时，则其他进程提交失败
 *
 * 注意事项：
 *  非阻塞
 *  增加数据库冗余
 *  高并发场景下，写压力比较大
 *  并发量不高
 */
@Slf4j
public class OptimisticLock extends Thread{

    @Override
    public void run() {
        super.run();

        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        ResultSet resultSet = null;
        String goodName = null;
        int goodCount = 0;

        try {
            while(true){
                log.info("当前线程：" + pid + "，开始抢购商品！");
                //获取当前商品信息
                resultSet = MySQLDistributedLockService.getGoodCount(1);
                while (resultSet.next()){
                    goodName = resultSet.getString("good_name");
                    goodCount = resultSet.getInt("good_count");
                }
                log.info("获取库存成功，当前商品名为：" + goodName + "，当前库存剩余量为：" + goodCount);

                //模拟执行业务操作
                Thread.sleep(2*3000);
                if(0 == goodCount){
                    log.info("抢购失败，当前库存为0！ ");
                    break;
                }
                //修改库存信息，库存量-1
                if(MySQLDistributedLockService.setGoodCount(1,goodCount)){
                    log.info("当前线程：" + pid + " 抢购商品：" + goodName + "成功，剩余库存为：" + (goodCount -1));
                    //模拟延迟，防止锁每次被同一进程获取
                    Thread.sleep(2 * 1000);
                }else{
                    log.error("抢购商品：" + goodName +"失败，商品数量已被修改");
                }
            }
        }catch (Exception e){
            log.error("抢购商品发生错误！",e);
        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    log.error("关闭Result失败！" , e);
                }
            }

            MySQLDistributedLockService.close();
        }
    }

    public static void main(String[] args) {
        new OptimisticLock().start();
    }
}