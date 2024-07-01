package com.zhf.java_basic.construct;

public class MyClass {
    long var;
    public void MyClass(long param) { var = param; }//(1)
    public static void main(String[] args) {
        MyClass a, b;
        a =new MyClass();//(2)
        // 编译报错在第3处，类名可以和方法名一致
//        b =new MyClass(5);//(3)

    }
}
