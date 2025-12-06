package com.zhf.design.pattern.strategy;

public class Client {

    public static void main(String[] args) {
        Context context = new Context(new ConcreteStrategyA());
        context.algorithm();
    }
}
