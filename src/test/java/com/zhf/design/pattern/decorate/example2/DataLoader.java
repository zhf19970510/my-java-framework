package com.zhf.design.pattern.decorate.example2;

/**
 * 抽象的文件读取接口
 */
public abstract class DataLoader {

    public abstract String read();

    public abstract void write(String data);

}
