package com.zhf.design.pattern.strategy;

/**
 * 具体策略类
 */
public class ConcreteStrategyA implements Strategy{
    @Override
    public void algorithm() {
        System.out.println("执行策略A");
    }
}
