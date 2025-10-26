package com.zhf.juc;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueTest {

    public static void main(String[] args) {
        // 因为当前队列不存在数据，没有长度的概念
        SynchronousQueue queue = new SynchronousQueue();

        String msg = "消息！";
        new Thread(() -> {
            boolean b = false;
            try {
                b = queue.offer(msg, 1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(b);
        }).start();

        new Thread(() -> {
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
