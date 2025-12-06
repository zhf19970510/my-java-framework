package com.zhf.design.pattern.state;

/**
 * 上下文类：状态模式中的上下文类
 */
public class Context {

    // 维持一个对状态对象的引用
    private State currentState;

    public Context() {
        this.currentState = null;
    }

    public Context(State state) {
        this.currentState = state;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getCurrentState() {
        return currentState;
    }
}
