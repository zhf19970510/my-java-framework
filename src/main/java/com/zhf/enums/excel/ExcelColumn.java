package com.zhf.enums.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: 曾鸿发
 * @create: 2022-03-04 11:15
 * @description：
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {

    int columnIndex();

    String columnName();
}
