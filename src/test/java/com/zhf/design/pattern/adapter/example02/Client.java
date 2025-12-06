package com.zhf.design.pattern.adapter.example02;

import com.zhf.design.pattern.adapter.example01.Computer;
import com.zhf.design.pattern.adapter.example01.SDCard;
import com.zhf.design.pattern.adapter.example01.SDCardImpl;
import com.zhf.design.pattern.adapter.example01.TFCard;
import com.zhf.design.pattern.adapter.example01.TFCardImpl;

public class Client {

    public static void main(String[] args) {
        Computer computer = new Computer();
        SDCard sdCard = new SDCardImpl();
        String msg = computer.read(sdCard);
        System.out.println(msg);
        System.out.println("-------------------------");
        TFCard tfCard = new TFCardImpl();
        SDCard adapterTF = new SDFAdapterTF2(tfCard);
        System.out.println(computer.read(adapterTF));

    }
}
