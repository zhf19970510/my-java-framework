package com.zhf.util;

/**
 * @author: 曾鸿发
 * @create: 2022-04-18 17:00
 * @description：
 **/
@FunctionalInterface
public interface BeanUtilCopyCallBack <S, T> {
    /**
     * 定义默认回调方法，如果有特殊的转换方法，则可以在此处用
     * @param t
     * @param s
     */
    void callBack(S t, T s);
}

