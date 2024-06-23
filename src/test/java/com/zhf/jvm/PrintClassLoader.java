package com.zhf.jvm;

public class PrintClassLoader {

    public static void main(String[] args) {
        // App ClassLoader
        System.out.println(Worker.class.getClassLoader());
        // Ext ClassLoader
        System.out.println(Worker.class.getClassLoader().getParent());
        // Bootstrap ClassLoader
        System.out.println(Worker.class.getClassLoader().getParent().getParent());
        System.out.println(String.class.getClassLoader());
    }
}
