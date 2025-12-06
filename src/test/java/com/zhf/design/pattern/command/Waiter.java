package com.zhf.design.pattern.command;

import java.util.ArrayList;

public class Waiter {

    // 可以持有多个命令对象
    private ArrayList<Command> commands;

    public Waiter() {
        commands = new ArrayList<>();
    }

    public Waiter(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public void setCommands(Command command) {
        this.commands.add(command);
    }

    public void orderUp() {
        System.out.println("叮咚！服务员：有新的订单，请师傅开始制作....");
        for (Command command : commands) {
            if (command != null) {
                command.execute();
            }
        }
    }

}
