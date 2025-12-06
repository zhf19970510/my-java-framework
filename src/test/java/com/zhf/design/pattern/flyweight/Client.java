package com.zhf.design.pattern.flyweight;

public class Client {

    public static void main(String[] args) {
        FlyWeightFactory factory = new FlyWeightFactory();

        // 通过工厂对象获取共享的享元对象
        FlyWeight a1 = factory.getFlyWeight("A");
        FlyWeight a2 = factory.getFlyWeight("A");
        System.out.println(a1 == a2);

    }
}
