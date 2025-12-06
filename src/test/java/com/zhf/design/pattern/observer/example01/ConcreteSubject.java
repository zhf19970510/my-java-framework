package com.zhf.design.pattern.observer.example01;

import java.util.ArrayList;

/**
 * 具体目标类
 */
public class ConcreteSubject implements Subject{

    // 定义集合，存储所有观察者对象
    private ArrayList<Observer> observers = new ArrayList<>();

    // 注册方法，向观察者集合增加一个观察者
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    // 注销方法，从观察者集合中删除一个观察者
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    // 通知方法
    @Override
    public void notifyObservers() {
        // 遍历集合，调用观察者的响应方法
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
