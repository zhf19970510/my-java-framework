package com.zhf.design.pattern.decorate.example1;

/**
 * 具体构建类（被装饰类）
 */
public class ConcreteComponent extends Decorator {

    public ConcreteComponent(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();  // 调用原有的业务方法

    }

}
