package com.zhf.util;

/**
 * @author: 曾鸿发
 * @create: 2022-03-02 13:54
 * @description：计数器
 **/
public class Counter {

    public int count = 0;

    public Counter(){}

    public Counter(int count){
        this.count = count;
    }

    public int get(){
        return count;
    }

    public int increment(){
        count++;
        return count;
    }

    public int decrement(){
        count--;
        return count;
    }

    public int increment(int n){
        count += n;
        return count;
    }

    public int decrement(int n){
        count -= n;
        return count;
    }
}
