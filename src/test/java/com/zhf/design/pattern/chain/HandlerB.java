package com.zhf.design.pattern.chain;

public class HandlerB extends Handler{
    @Override
    public void handle(RequestData requestData) {
        System.out.println("HandlerB 处理数据：" + requestData.getData());
        requestData.setData(requestData.getData().replace("B", ""));
        if(successor != null) {
            successor.handle(requestData);
        } else {
            System.out.println("执行终止");
        }
    }
}
