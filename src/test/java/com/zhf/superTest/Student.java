package com.zhf.superTest;

import lombok.Data;

@Data
public class Student extends People{

    private String sno;

    private String clz;

    @Override
    public String toString() {
        return "Student{" +
                "sno='" + sno + '\'' +
                ", clz='" + clz + '\'' +
                "} " + super.toString();
    }
}
