package com.zhf.design.pattern.decorate.example2;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 具体组件：抽象文件读取接口的实现类
 */
public class BaseFileDataLoader extends DataLoader{

    private String filePath;

    public BaseFileDataLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String read() {
        try {
            return FileUtils.readFileToString(new File(filePath), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(String data) {
        try {
            FileUtils.writeStringToFile(new File(filePath), data, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
