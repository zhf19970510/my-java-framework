package com.zhf.java_basic.extends1;

/**
 *   此题的父类方法有private修饰，所以对子类不可见，子类不能覆盖。所以子类方法和父类是两个方法。
 *
 *   扩展：如果父类方法将private改为public 会怎样？
 *
 *           会报错，因为父类方法有final修饰，不能被覆盖。
 *
 */
public class Car extends Vehicle{

    public static void main(String[] args) {
        new Car().run();
    }

    private final void run() {
        System.out.println("Car");
    }
}
