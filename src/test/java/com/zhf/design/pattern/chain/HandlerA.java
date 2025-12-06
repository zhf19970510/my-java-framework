package com.zhf.design.pattern.chain;

public class HandlerA extends Handler{
    @Override
    public void handle(RequestData requestData) {
        System.out.println("HandlerA 处理数据：" + requestData.getData());
        requestData.setData(requestData.getData().replace("A", ""));
        if(successor != null) {
            successor.handle(requestData);
        } else {
            System.out.println("执行终止");
        }
    }
}
