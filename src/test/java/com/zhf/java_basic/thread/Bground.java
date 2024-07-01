package com.zhf.java_basic.thread;

public class Bground extends Thread{
    public static void main(String argv[]){

        Bground b = new Bground();
        // 这里不会编译报错，因为run方法父类Thread也有，只是是一个空实现而已
        b.run();
    }
    public void start(){
        for(int i=0;i<10;i++){
            System.out.println("Value of i = "+i);
        }
    }
}
