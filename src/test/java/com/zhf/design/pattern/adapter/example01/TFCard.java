package com.zhf.design.pattern.adapter.example01;

public interface TFCard {



    // 读取SD卡
    String readTF();

    void writeTF(String msg);
}
