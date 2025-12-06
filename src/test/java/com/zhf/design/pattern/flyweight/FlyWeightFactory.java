package com.zhf.design.pattern.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂类
 * 作用：作为存储享元对象的享元池存在的，用户获取享元对象时先从享元池中获取，如果享元池中没有，则创建一个，并放入享元池中。有则返回
 */
public class FlyWeightFactory {

    // 定义一个Map集合用于存储享元对象，实现享受元池
    private Map<String, FlyWeight> pool = new HashMap<>();


    // 实现享元对象之间状态的传递
    public FlyWeightFactory() {

        // 添加对应的内部状态
        pool.put("A", new ConcreteFlyWeight("A"));
        pool.put("B", new ConcreteFlyWeight("B"));
        pool.put("C", new ConcreteFlyWeight("C"));


    }

    // 根据内部状态进行查找
    public FlyWeight getFlyWeight(String key) {
        // 对象是否存在
        if (pool.containsKey(key)) {
            System.out.println("=== 享元池中存在，直接复用，key：" + key);
            return pool.get(key);
        } else {
            // 如果对象不存在，就创建一个添加到享元池，然后返回
            System.out.println("=== 享元池中不存在，创建对象，key：" + key);
            FlyWeight concreteFlyWeight = new ConcreteFlyWeight(key);
            pool.put(key, concreteFlyWeight);
            return concreteFlyWeight;
        }
    }

}
