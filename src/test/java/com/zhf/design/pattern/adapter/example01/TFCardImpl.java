package com.zhf.design.pattern.adapter.example01;

public class TFCardImpl implements TFCard{
    @Override
    public String readTF() {
        String msg = "tf card reading data";
        return msg;
    }

    @Override
    public void writeTF(String msg) {
        System.out.println("tf card writing data:"+msg);

    }
}
