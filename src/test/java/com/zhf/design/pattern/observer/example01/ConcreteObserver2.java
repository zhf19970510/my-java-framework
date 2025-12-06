package com.zhf.design.pattern.observer.example01;

/**
 * 具体观察者角色2
 */
public class ConcreteObserver2 implements Observer{
    @Override
    public void update() {
        System.out.println("ConcreteObserver2 得到通知，更新状态！！ ");
    }
}
