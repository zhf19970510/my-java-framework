package com.zhf.design.pattern.templateMethod;

public class ConcreteClassA extends AbstractClassTemplate{
    @Override
    protected void step4() {
        System.out.println("在具体类A中 -> 执行步骤4");
    }

    @Override
    protected void step3() {
        System.out.println("在具体类A中 -> 执行步骤3");
    }
}
