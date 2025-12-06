package com.zhf.design.pattern.adapter.example01;

public class Client {

    public static void main(String[] args) {
        Computer computer = new Computer();
        SDCard sdCard = new SDCardImpl();
        String msg = computer.read(sdCard);
        System.out.println(msg);

        System.out.println("-------------------------");

        SDAdapterTF adapterTF = new SDAdapterTF();
        System.out.println(computer.read(adapterTF));
    }
}
