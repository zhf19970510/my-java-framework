package com.zhf.enums;

public class CommonEnum {

    public enum LangMode {
        CHINESE("zh"),
        ENGLISH("en"),
        ;

        private String value;

        LangMode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
