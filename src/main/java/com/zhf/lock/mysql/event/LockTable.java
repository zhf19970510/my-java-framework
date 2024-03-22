package com.zhf.lock.mysql.event;

import com.zhf.lock.mysql.service.MySQLDistributedLockService;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;

/**
 * mysql分布式锁
 *      执行流程： 多进程抢占数据库某个资源，然后执行业务，执行完释放资源
 *      锁机制： 单一进程获取锁时，则其他进程提交失败
 *
 *      注意事项：
 *
 * - 该锁为非阻塞的
 * - 当某进程持有锁并且挂死时候会造成资源一直不释放的情况，造成死锁，因此需要维护一个定时清理任务去清理持有过久的锁
 * - 要注意数据库的单点问题，最好设置备库，进一步提高可靠性
 * - 该锁为非可重入锁，如果要设置成可重入锁需要添加数据库字段记录持有该锁的设备信息以及加锁次数
 *
 *
 * 关于zookeeper分布式锁实现在zookeeper-review项目中实现
 */
@Slf4j
public class LockTable extends Thread {

    @Override
    public void run() {
        super.run();

        //获取Java虚拟机的进程ID
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        try{
            while(true){
                log.info("当前进程PID：" + pid + ",尝试获取锁资源！");
                if(MySQLDistributedLockService.tryLock(1,"测试锁")){
                    log.info("当前进程PID：" + pid + ",获取锁资源成功！");

                    //sleep模拟业务处理过程
                    log.info("开始处理业务！");
                    Thread.sleep(10*1000);
                    log.info("业务处理完成！");

                    MySQLDistributedLockService.releaseLock(1);
                    log.info("当前进程PID： " + pid + ",释放了锁资源！");
                    break;
                }else{
                    log.info("当前进程PID： " + pid + "，获取锁资源失败！");
                    Thread.sleep(2000);
                }
            }
        }catch (Exception e){
            log.error("抢占锁发生错误！",e);
        }finally {
            MySQLDistributedLockService.close();
        }
    }

    // 程序入口
    public static void main(String[] args) {

        new LockTable().start();
    }
}