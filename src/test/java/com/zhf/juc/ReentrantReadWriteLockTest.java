package com.zhf.juc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest {

    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void main(String[] args) throws Exception{

        new Thread(() -> {
            readLock.lock();
            System.out.println("子线程！");
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                readLock.unlock();
            }
        }).start();

        Thread.sleep(1000);

        writeLock.lock();
        try {
            System.out.println("主线程！");
        } finally {
            writeLock.unlock();
        }
    }

}
