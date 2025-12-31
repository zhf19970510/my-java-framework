package com.zhf.juc;

import java.util.concurrent.*;

public class ForkJoinPoolTest {

    static int[] nums = new int[1_000_000_00];

    static {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = (int) (Math.random() * 1000);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("单线程计算数组总和！");
        int sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }
        long end = System.nanoTime();
        System.out.println("单线程计算结果：" + sum + " 耗时：" + (end - start));

        // ===================================多线程分而治之累加10亿数据=========================================
        // 在使用ForkJoinPool时，不推荐使用Runnable和Callable，而是使用RecursiveTask和RecursiveAction
        // 可以使用提供的另外两种任务的描述方式
        // Runnable(没有返回结果) -> RecursiveAction，这个时候在任务定义中只需要fork，不需要join操作。并且调用的是execute方法
        // Callable(有返回结果) ->   RecursiveTask
        ForkJoinPool forkJoinPool = (ForkJoinPool) Executors.newWorkStealingPool();
        long forkJoinStart = System.nanoTime();
        SumRecursiveTask task = new SumRecursiveTask(0, nums.length);
        int sum2 = forkJoinPool.invoke(task);
//        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(task);
//        System.out.println(forkJoinTask.get());
        long forkJoinEnd = System.nanoTime();
//        System.out.println("多线程计算结果：" + forkJoinTask.get() + " 耗时：" + (forkJoinEnd - forkJoinStart));
        System.out.println("多线程计算结果：" + sum2 + " 耗时：" + (forkJoinEnd - forkJoinStart));

    }

    private static class SumRecursiveTask extends RecursiveTask<Integer> {

        /** 指定一个线程处理哪个位置的数据 */
        ;
        private int start;
        private int end;

        private final int MAX_STRIDE = 20_000_000;

        public SumRecursiveTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            // 在这个方法中，需要设置好任务拆分的逻辑以及聚合的逻辑
            int sum = 0;
            int stride = end - start;
            if (stride <= MAX_STRIDE) {
                for (int i = start; i < end; i++) {
                    sum += nums[i];
                }
            } else {
                // 将任务拆分，分而治之
                int middle = start + stride / 2;
                SumRecursiveTask left = new SumRecursiveTask(start, middle);
                SumRecursiveTask right = new SumRecursiveTask(middle, end);
                // 分别执行两个任务
                left.fork();
                right.fork();
                // 等待结果，并且获取sum
                sum = left.join() + right.join();
            }
            return sum;
        }
    }

}
