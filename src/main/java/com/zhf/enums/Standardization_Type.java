package com.zhf.enums;

public enum Standardization_Type {

    REG_EXP("1"),
    FILTER("2"),
    SPLIT("3"),
    DICTIONARY("4")
    ;
    private String value;

    Standardization_Type(String value){
        this.value = value;
    }
}
