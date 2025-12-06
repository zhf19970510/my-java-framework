package com.zhf.design.pattern.command;

/**
 * 具体命令
 */
public class OrderCommand implements  Command{

    // 接收者对象引用
    private Chef receiver;

    private Order order;

    public OrderCommand(Chef receiver, Order order) {
        this.receiver = receiver;
        this.order = order;
    }

    @Override
    public void execute() {
        System.out.println(order.getDiningTable() + "桌的订单：");
        for (String foodName : order.getFoodMenu().keySet()) {
            receiver.makeFood(foodName, order.getFoodMenu().get(foodName));
        }
    }
}
