package com.zhf.jvm;

public class StaticTest {

    static {
        i = 3;
    }

    public static int i;

    public static void main(String[] args) {
        System.out.println(i);
    }
}
