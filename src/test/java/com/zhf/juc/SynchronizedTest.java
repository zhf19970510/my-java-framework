package com.zhf.juc;

public class SynchronizedTest {

    private static boolean flag = true;



    public static void main(String[] args) throws Exception{

        Object o = new Object();

        Thread t1 = new Thread(() -> {
            while (flag) {
                // 获取锁的一瞬间会从内存中重新读变量flag的值
                // 获取锁资源后，会将内部涉及到的变量从cpu缓存中移除，必须去主内存中拿数据，释放锁之后，会立即将CPU缓存中的数据同步到主内存中
                synchronized (o) {
                    System.out.println("hello world");
                }
            }
            System.out.println("t1 线程结束");
        });

        t1.start();
        Thread.sleep(100);

        flag = false;
        System.out.println("main 线程结束");
    }


}
