package com.zhf.design.pattern.observer.example01;

/**
 * 抽象观察者角色
 */
public interface Observer {

    //  update方法：为不同观察者更新行为定义一个相同的接口，不同观察者对该接口有不同的实现
    public void update();

}
