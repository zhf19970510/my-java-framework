package com.zhf.design.pattern.flyweight;

/**
 * 非共享的享元角色
 */
public class UnSharedFlyWeight extends FlyWeight{

    private String inState;

    public UnSharedFlyWeight(String inState) {
        this.inState = inState;
    }

    @Override
    public void operation(String state) {
        System.out.println("非共享的享元角色：" + inState + "---" + state + ",外部状态：" + state);

    }
}
