package com.zhf.juc;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompletableFutureDemo1 {

    public static void main(String[] args) {
        // 任务数量
        int n = 10;
        // 线程池大小
        int m = 3;

        // 创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(m);

        // 创建任务列表
        List<CompletableFuture<String>> futures = IntStream.range(0, n)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        // 模拟任务执行耗时
                        Thread.sleep(1000);
                        return "任务[" + i + "]在线程[" + Thread.currentThread().getName() + "]中执行完成";
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }, executor))
                .collect(Collectors.toList());

        // 使用allOf等待所有任务完成
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        // 合并所有结果
        CompletableFuture<List<String>> allResults = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        try {
            // 获取最终结果
            List<String> results = allResults.get();
            results.forEach(System.out::println);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            executor.shutdown();
        }
    }
}
