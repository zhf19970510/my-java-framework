package com.zhf.java_basic.tryCatch;

import java.util.Set;
import java.util.TreeSet;

/**
 *  首先执行try，遇到算术异常，抛出，执行catch，打印1，然后抛出RuntimeException，缓存异常，执行finally，打印2，然后抛出RuntimeException。
 *
 *   如果catch中没有抛出RuntimeException，则执行结果为123。
 *
 */
public class ZeroTest {
    public static void main(String[] args) {
        try {
            int i = 100 / 0;
            System.out.println(i);
        } catch (Exception e) {
            System.out.println(1);
            throw new RuntimeException();
        } finally {
            System.out.println(2);
        }
        System.out.println(3);
    }
}
