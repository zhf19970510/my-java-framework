package com.zhf.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

    public static void main(String[] args) throws Exception{
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("召唤神龙，等待各位大佬都到位之后，分发护照");
        });
        new Thread(() -> {
            System.out.println("小龙女，请稍等");
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("悲剧，人没到齐");
                return;
            }
            System.out.println("小龙女，出发");
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            System.out.println("Jack，请稍等");
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("悲剧，人没到齐");
                return;
            }
            System.out.println("Jack，出发");
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            System.out.println("Tom，请稍等");
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("悲剧，人没到齐");
                return;
            }
            System.out.println("Tom，出发");
        }).start();
    }
}
