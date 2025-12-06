package com.zhf.design.pattern.adapter.example01;

public class Computer {

    public String read(SDCard sdCard) {
        return sdCard.readSD();
    }
}
