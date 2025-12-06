package com.zhf.design.pattern.templateMethod;

public abstract class AbstractClassTemplate {

    void step1(String key) {
        System.out.println("在模板类中 -> 执行步骤1");
        if(step2(key)) {
            step3();
        } else {
            step4();
        }
        step5();
    }


    protected abstract void step4();

    protected abstract void step3();

    boolean step2(String key) {
        System.out.println("在模板类中 -> 执行步骤2");
        if("x".equals(key)) {
            return true;
        }
        return false;
    }

    void step5() {
        System.out.println("在模板类中 -> 执行步骤5");
    }

    void run(String key) {
        step1(key);
    }
}
