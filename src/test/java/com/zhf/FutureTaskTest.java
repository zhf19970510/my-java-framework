package com.zhf;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {

    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }


    static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("当前线程：" + Thread.currentThread().getName());
            return "hello world";
        }
    }
}
