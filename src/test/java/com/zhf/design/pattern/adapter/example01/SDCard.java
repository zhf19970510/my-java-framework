package com.zhf.design.pattern.adapter.example01;

public interface SDCard {

    // 读取SD卡
    String readSD();

    void writeSD(String msg);
}
