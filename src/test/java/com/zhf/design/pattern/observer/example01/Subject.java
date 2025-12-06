package com.zhf.design.pattern.observer.example01;

/**
 * 抽象被观察者
 */
public interface Subject {

    void attach(Observer observer);

    void detach(Observer observer);
    void notifyObservers();

}
