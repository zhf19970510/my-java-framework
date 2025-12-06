package com.zhf.design.pattern.state;

public class Client {

    public static void main(String[] args) {
        Context context = new Context();
        State state1 = new ConcreteStateA();
        state1.handle(context);
        System.out.println(context.getCurrentState().toString());

        System.out.println("==================");

        State state2 = new ConcreteStateA();
        state2.handle(context);
        System.out.println(context.getCurrentState().toString());
    }
}
