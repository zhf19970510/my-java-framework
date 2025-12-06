package com.zhf.design.pattern.adapter.example01;

/**
 * 适配器类（SD兼容TF)
 */
public class SDAdapterTF extends TFCardImpl implements SDCard{


    @Override
    public String readSD() {
        System.out.println("adapter read tf card");
        return readTF();
    }

    @Override
    public void writeSD(String msg) {
        System.out.println("adapter write tf card");
        writeTF(msg);
    }
}
