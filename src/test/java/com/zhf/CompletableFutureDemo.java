package com.zhf;

import java.util.concurrent.*;

public class CompletableFutureDemo {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
            50,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws ExecutionException, InterruptedException, Exception {
        // 获取CompletableFuture对象
        // 不推荐这种方式，它会用默认的线程池
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": 线程启动");
        });

        CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": 线程启动");
        }, executor);

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ": 线程启动");
            return "hello";
        }, executor);
        System.out.println(stringCompletableFuture.get());

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程开始了...");
            int i = 100 / 2;
            System.out.println("线程结束了...");
            return i;
        }, executor).whenCompleteAsync((v, t) -> {
            System.out.println("结果是：" + v);
            System.out.println("异常是：" + t);
            System.out.println("线程结束了...");
        });


        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程开始了...");
            int i = 100 / 2;
            System.out.println("线程结束了...");
            return i;
        }, executor).exceptionally(t -> {
            System.out.println("异常是：" + t);
            return 10;
        });


        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程开始了...");
            int i = 100 / 2;
            System.out.println("线程结束了...");
            return i;
        }, executor).handle((v, t) -> {
            System.out.println("结果是：" + v);
            System.out.println("异常是：" + t);
            return 10;
        });


        CompletableFuture.supplyAsync(() -> {
            System.out.println("线程开始了..." + Thread.currentThread().getName());
            int i = 100 / 2;
            System.out.println("线程结束了..." + Thread.currentThread().getName());
            return i;
        }, executor).thenRunAsync(() -> {
            System.out.println("线程开始了..." + Thread.currentThread().getName());
            int i = 100 / 2;
            System.out.println("线程结束了..." + Thread.currentThread().getName());
        }, executor);


        CompletableFuture.supplyAsync(() -> {
            System.out.println("线程开始了..." + Thread.currentThread().getName());
            int i = 100 / 2;
            System.out.println("线程结束了..." + Thread.currentThread().getName());
            return i;
        }, executor).thenAcceptAsync(v -> {
            System.out.println("线程开始了..." + Thread.currentThread().getName());
            System.out.println("结果是：" + v);
            System.out.println("线程结束了..." + Thread.currentThread().getName());
        }, executor);

        CompletableFuture.supplyAsync(() -> {
            System.out.println("线程开始了..." + Thread.currentThread().getName());
            int i = 100 / 2;
            System.out.println("线程结束了..." + Thread.currentThread().getName());
            return i;
        }, executor).thenApplyAsync(v -> {
            System.out.println("线程开始了..." + Thread.currentThread().getName());
            System.out.println("结果是：" + v);
            System.out.println("线程结束了..." + Thread.currentThread().getName());
            return v;
        }, executor);

        {
            CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
                System.out.println("线程开始了1..." + Thread.currentThread().getName());
                int i = 100 / 2;
                System.out.println("线程结束了1..." + Thread.currentThread().getName());
                return i;
            }, executor);

            CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> {
                System.out.println("线程开始了2..." + Thread.currentThread().getName());
                int i = 100 / 2;
                System.out.println("线程结束了2..." + Thread.currentThread().getName());
                return i;
            }, executor);

            // runAfterBothAsync 不能获取前面两个线程的返回结果，本身也没有返回结果
            future3.runAfterBothAsync(future4, () -> {
                System.out.println("线程开始了3..." + Thread.currentThread().getName());
            }, executor);

            // thenAcceptBothAsync 可以获取前面两个线程的返回结果，本身也没有返回结果
            future3.thenAcceptBothAsync(future4, (v1, v2) -> {
                System.out.println("结果是：" + v1);
                System.out.println("结果是：" + v2);
                System.out.println("线程开始了4..." + Thread.currentThread().getName());
            }, executor);

            // thenCombineAsync 可以获取前面两个线程的返回结果，本身有返回结果
            CompletableFuture<Integer> future5 = future3.thenCombineAsync(future4, (v1, v2) -> {
                System.out.println("结果是：" + v1);
                System.out.println("结果是：" + v2);
                System.out.println("线程开始了5..." + Thread.currentThread().getName());
                return v1 + v2;
            }, executor);

            // runAfterEitherAsync 不能获取前面两个线程的返回结果，本身也没有返回结果
            future3.runAfterEitherAsync(future4, () -> {
                System.out.println("线程开始了6..." + Thread.currentThread().getName());
            }, executor);

            future3.acceptEitherAsync(future4, v -> {
                System.out.println("结果是：" + v);
            }, executor);
            future3.applyToEitherAsync(future4, v -> {
                System.out.println("结果是：" + v);
                return v;
            }, executor);
        }

        {
            CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
                System.out.println("线程开始了1..." + Thread.currentThread().getName());
                int i = 100 / 2;
                System.out.println("线程结束了1..." + Thread.currentThread().getName());
                return i;
            }, executor);

            CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> {
                System.out.println("线程开始了2..." + Thread.currentThread().getName());
                int i = 100 / 2;
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("线程结束了2..." + Thread.currentThread().getName());
                return i;
            }, executor);

            CompletableFuture<Integer> future5 = CompletableFuture.supplyAsync(() -> {
                System.out.println("线程开始了3..." + Thread.currentThread().getName());
                int i = 100 / 2;
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("线程结束了3..." + Thread.currentThread().getName());
                return i;
            }, executor);

            CompletableFuture<Void> allOf = CompletableFuture.allOf(future3, future4, future5);

            allOf.get();    // 阻塞等待所有任务执行完成

            System.out.println("主任务执行完成");

            CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future3, future4, future5);
            anyOf.get();
            System.out.println("任意任务执行完成");
        }
    }
}
