package com.zhf.design.pattern.command;

/**
 * 厨师类 -> Receiver  接收者角色
 */
public class Chef {

    public void makeFood(String foodName, int num) {
        System.out.println("开始做：" + foodName + "，数量为：" + num);
    }

}
