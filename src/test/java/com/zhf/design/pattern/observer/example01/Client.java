package com.zhf.design.pattern.observer.example01;

public class Client {

    public static void main(String[] args) {
        // 创建目标类
        Subject subject = new ConcreteSubject();

        // 注册观察者，注册多个
        subject.attach(new ConcreteObserver1());
        subject.attach(new ConcreteObserver2());

        // 具体的主题内部发生改变了，给所有注册注册的观察者发送通知
        subject.notifyObservers();
    }
}
