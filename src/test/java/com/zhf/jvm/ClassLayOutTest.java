package com.zhf.jvm;

import org.openjdk.jol.info.ClassLayout;

public class ClassLayOutTest {

    public static void main(String[] args) {
        Worker worker = new Worker();
        System.out.println(worker.hashCode());
        System.out.println(ClassLayout.parseInstance(worker).toPrintable());
    }
}
