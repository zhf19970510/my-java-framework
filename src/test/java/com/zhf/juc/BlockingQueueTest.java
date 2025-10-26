package com.zhf.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueTest {

    public static void main(String[] args) throws Exception{
        ArrayBlockingQueue queue = new ArrayBlockingQueue(2);
        queue.add("1");
        queue.add("2");

        System.out.println(queue.remove());
        System.out.println(queue.remove());

        queue.offer("3");
        queue.offer("4");

        System.out.println(queue.poll());
        System.out.println(queue.poll());

        queue.offer("5", 2, TimeUnit.SECONDS);
        queue.offer("6", 2, TimeUnit.SECONDS);
        System.out.println(queue.poll(2, TimeUnit.SECONDS));
        queue.put("6");
        System.out.println(queue.take());
    }
}
