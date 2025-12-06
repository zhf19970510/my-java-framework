package com.zhf.design.pattern.templateMethod;

public class Test01 {

    public static void main(String[] args) {
        AbstractClassTemplate template = new ConcreteClassA();
        template.run("1");
    }
}
