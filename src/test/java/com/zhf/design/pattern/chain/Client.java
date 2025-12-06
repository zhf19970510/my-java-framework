package com.zhf.design.pattern.chain;

public class Client {

    public static void main(String[] args) {
        RequestData requestData = new RequestData("feafeaZFEJAOIZAFEJAOBDJIOCOFIE");

        // 创建处理链
        Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        Handler handlerC = new HandlerC();
        handlerA.setSuccessor(handlerB);
        handlerB.setSuccessor(handlerC);
        handlerA.handle(requestData);
    }
}
