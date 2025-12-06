package com.zhf.design.pattern.chain;

public class HandlerC extends Handler{
    @Override
    public void handle(RequestData requestData) {
        System.out.println("HandlerC 处理数据：" + requestData.getData());
        requestData.setData(requestData.getData().replace("C", ""));
        if(successor != null) {
            successor.handle(requestData);
        } else {
            System.out.println("执行终止");
        }
    }
}
