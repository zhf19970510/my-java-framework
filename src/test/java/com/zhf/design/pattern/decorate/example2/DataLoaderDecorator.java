package com.zhf.design.pattern.decorate.example2;

/**
 * 抽象装饰者类
 */
public abstract class DataLoaderDecorator extends DataLoader{


    private DataLoader dataLoader;

    public DataLoaderDecorator(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @Override
    public String read() {
        return dataLoader.read();
    }

    @Override
    public void write(String data) {
        dataLoader.write(data);
    }
}
