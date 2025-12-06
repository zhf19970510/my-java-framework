package com.zhf.design.pattern.chain;

public abstract class Handler {

    // 后继处理者的引用
    public Handler successor;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public abstract void handle(RequestData requestData);

}
