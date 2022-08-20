package com.zhf.serializer;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class GsonSerializer implements ISerializer {
    private static final Logger logger = LoggerFactory.getLogger(GsonSerializer.class);
    private static GsonSerializer instance = new GsonSerializer();

    private GsonSerializer() {

    }

    public static synchronized GsonSerializer getInstance() {
        if (null == instance) {
            instance = new GsonSerializer();
        }
        return instance;
    }

    @Override
    public byte[] toByteArray(Object object) {
        try {
            return (new Gson()).toJson(object).getBytes(StandardCharsets.UTF_8);
        } catch (Exception var3) {
            throw new RuntimeException("GsonSerializer toByteArray error: ", var3);
        }
    }

    @Override
    public <T> T toObject(byte[] bytes, Class<T> clazz) {
        try {
            return (new Gson()).fromJson(new String(bytes, StandardCharsets.UTF_8), clazz);
        } catch (Exception var4) {
            throw new RuntimeException("GsonSerializer toObject error: ", var4);
        }
    }

    @Override
    public <T> T toObject(byte[] bytes, Type type) {
        try {
            return (new Gson()).fromJson(new String(bytes, StandardCharsets.UTF_8), type);
        } catch (Exception var4) {
            throw new RuntimeException("GsonSerializer toObject error: ", var4);
        }
    }

    @Override
    public SerializerType getType() {
        return SerializerType.GSON;
    }
}
