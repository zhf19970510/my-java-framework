package com.zhf.design.pattern.adapter.example02;

import com.zhf.design.pattern.adapter.example01.SDCard;
import com.zhf.design.pattern.adapter.example01.TFCard;

/**
 * 对象适配器 - 组合形式
 */
public class SDFAdapterTF2 implements SDCard {

    private TFCard tfCard;

    public SDFAdapterTF2(TFCard tfCard) {
        this.tfCard = tfCard;
    }

    @Override
    public String readSD() {
        System.out.println("adapter read tf card");
        return tfCard.readTF();
    }

    @Override
    public void writeSD(String msg) {
        System.out.println("adapter write tf card");
        tfCard.writeTF(msg);
    }
}
