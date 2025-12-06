package com.zhf.design.pattern.observer.example01;

/**
 * 具体观察者角色1
 */
public class ConcreteObserver1 implements Observer{
    @Override
    public void update() {
        System.out.println("ConcreteObserver1 得到通知，更新状态！！ ");
    }
}
