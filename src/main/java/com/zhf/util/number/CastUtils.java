package com.zhf.util.number;

public interface CastUtils {

    @SuppressWarnings("unchecked")
    static <T> T cast(Object object) {
        return (T) object;
    }

}
