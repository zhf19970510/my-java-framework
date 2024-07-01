package com.zhf.java_basic.static1;

public class TestDemo1 {

    private int count;
    public static void main(String[] args) {
        TestDemo1 test=new TestDemo1(88);
        System.out.println(test.count);

        TestDemo2 testDemo2 = new TestDemo2(99);
        // System.out.println(testDemo2.count);        // 这样会报错，其实还考察了访问修饰符的访问范围

    }
    TestDemo1(int a) {
        count=a;
    }

    public static int getMyCount() {
        //return count;         // 会报错，普通的static直接访问会报错
        return 0;
    }
}
