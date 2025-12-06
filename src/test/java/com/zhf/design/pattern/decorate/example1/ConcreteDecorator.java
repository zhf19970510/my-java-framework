package com.zhf.design.pattern.decorate.example1;

public class ConcreteDecorator extends Decorator {

    public ConcreteDecorator(Component component) {
        super(component);
    }


    @Override
    public void operation() {
        super.operation();
        add();
    }


    // 新增业务方法
    public void add() {
        System.out.println("新增业务方法");
    }
}
