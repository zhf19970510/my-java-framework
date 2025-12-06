package com.zhf.design.pattern.decorate.example2;

public class Client {

    public static void main(String[] args) {

        // 未加密，未装饰
        String info = "anme:tom, age:30";
//        BaseFileDataLoader baseFileDataLoader = new BaseFileDataLoader("demo.txt");
//        baseFileDataLoader.write(info);
//        System.out.println(baseFileDataLoader.read());


        DataLoader dataLoader = new EncryptionDataLoader(new BaseFileDataLoader("demo.txt"));
        dataLoader.write( info);
        System.out.println(dataLoader.read());
    }
}
