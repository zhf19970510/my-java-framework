package com.zhf.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionObjectTest {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            System.out.println("子线程获取锁资源并await挂起线程");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                condition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("子线程挂起后被唤醒！持有锁资源");
        }).start();

        Thread.sleep(100);

        lock.lock();

        System.out.println("主线程等待5秒拿到锁资源，子线程执行了await方法");
        condition.signal();
        lock.unlock();
    }

}
