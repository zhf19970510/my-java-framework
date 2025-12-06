package com.zhf.design.pattern.adapter.example01;

/**
 * SD卡实现类
 */
public class SDCardImpl implements SDCard{



    @Override
    public String readSD() {
        String msg = "sd card reading data";
        return msg;
    }

    @Override
    public void writeSD(String msg) {
        System.out.println("sd card writing data:"+msg);

    }
}
