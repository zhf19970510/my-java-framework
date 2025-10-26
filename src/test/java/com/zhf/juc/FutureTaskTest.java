package com.zhf.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {

    public static void main(String[] args) throws Exception{
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("任务开始执行....");
            Thread.sleep(2000);
            System.out.println("任务执行完成....");
            return "hello world";
        });

        // 构建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 线程池执行任务
        executorService.execute(futureTask);

        // 对返回结果的获取，类似阻塞队列的take方法，死等结果
        String s = futureTask.get();
        System.out.println(s);

        // 对任务状态的控制
        System.out.println("任务结束了么？" + futureTask.isDone());
        Thread.sleep(1000);
        System.out.println("任务结束了么？" + futureTask.isDone());
        Thread.sleep(1000);
        System.out.println("任务结束了么？" + futureTask.isDone());
        Thread.sleep(1000);
        System.out.println("任务结束了么？" + futureTask.isDone());

    }
}
