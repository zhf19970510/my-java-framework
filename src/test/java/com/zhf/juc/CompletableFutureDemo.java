package com.zhf.juc;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class CompletableFutureDemo {
    public static void main(String[] args) {
        // 配置参数
        final int m = 5;    // 线程数量
        final int n = 20;   // 任务数量
        
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(m);
        
        // 创建CompletableFuture数组
        CompletableFuture<?>[] futures = IntStream.range(0, n)
            .mapToObj(taskId -> CompletableFuture.runAsync(() -> {
                System.out.printf("任务 %d 正在由线程 %s 执行%n",
                    taskId, Thread.currentThread().getName());
                try {
                    // 模拟任务执行耗时
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.printf("任务 %d 完成%n", taskId);
            }, executor))
            .toArray(CompletableFuture[]::new);

        // 使用allOf等待所有任务完成
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(futures);
        
        try {
            // 阻塞等待所有任务完成（带超时）
            allTasks.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            executor.shutdown();
        }
    }
}
