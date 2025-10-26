package com.zhf.juc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolTest {

    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

        pool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + ": 1");
        });

        // 延迟执行，执行当前任务延迟5s后再执行
        pool.schedule(() -> {
            System.out.println(Thread.currentThread().getName() + ": 2");
        }, 5, TimeUnit.SECONDS);

        // 延迟执行，当前任务第一次延迟5s执行，每3s执行一次
        // 这个方法在计算下次执行时间时，是从任务刚刚开始执行时开始计算，而不是从任务上次执行完毕时开始计算
        pool.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + ": 3");
        }, 5, 3, TimeUnit.SECONDS);

        // 周期执行，当前任务第一次延迟5s执行，每3s执行一次
        // 这个方法在计算下次执行时间时，是从任务上次执行完毕时开始计算，而不是从任务刚刚开始执行时开始计算
        pool.scheduleWithFixedDelay(() -> {
            System.out.println(Thread.currentThread().getName() + ": 4");
        }, 5, 3, TimeUnit.SECONDS);
    }
}
