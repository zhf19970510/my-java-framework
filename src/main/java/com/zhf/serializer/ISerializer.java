package com.zhf.serializer;

import java.lang.reflect.Type;

public interface ISerializer {

    byte[] toByteArray(Object var1);

    <T> T toObject(byte[] var1, Class<T> var2);

    <T> T toObject(byte[] var1, Type var2);

    SerializerType getType();

}
