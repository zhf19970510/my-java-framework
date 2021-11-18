package com.zhf.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author: 曾鸿发
 * @create: 2021-10-31 17:15
 * @description：处理map为null的方法
 **/
public class NullKeySerializer extends StdSerializer<Object> {

    public NullKeySerializer() {
        this(null);
    }

    public NullKeySerializer(Class<Object> t) {
        super(t);
    }

    @Override
    public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeFieldName("");
    }
}