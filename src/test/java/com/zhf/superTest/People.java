package com.zhf.superTest;

import lombok.Data;

@Data
public class People {
    private String name;

    private String sex;

    private Integer age;

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
