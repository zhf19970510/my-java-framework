package com.zhf.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CompletableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 配置参数
        final int m = 5;    // 线程数量
        final int n = 20;   // 任务数量

        ExecutorService executor = Executors.newFixedThreadPool(m);

        CompletableFuture<Integer>[] futures = IntStream.range(0, n)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    System.out.println("Task " + i + " is running on " + Thread.currentThread().getName());
                    return i * i;
                }, executor))
                .toArray(CompletableFuture[]::new);

        // 等待所有任务完成并获取结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);
        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(futures);

        // 获取所有任务的结果
        CompletableFuture<Object> combinedFuture = allFutures.thenApply(v ->
                IntStream.range(0, n)
                        .mapToObj(i -> futures[i].join())
                        .toArray()
        );

        System.out.println("Results: " + java.util.Arrays.toString((Object[]) combinedFuture.get()));

        // 关闭线程池
        executor.shutdown();
    }
}
