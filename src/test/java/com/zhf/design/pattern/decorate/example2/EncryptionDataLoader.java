package com.zhf.design.pattern.decorate.example2;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * 具体装饰者类 - 对文件进行加密和解密
 */
public class EncryptionDataLoader extends DataLoaderDecorator{



    public EncryptionDataLoader(DataLoader dataLoader) {
        super(dataLoader);
    }

    @Override
    public String read() {
        return decrypt(super.read());
    }

    @Override
    public void write(String data) {
        super.write(encrypt(data));
    }

    // 加密操作
    public String encrypt(String data) {
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] bytes = data.getBytes("UTF-8");
            return encoder.encodeToString(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密操作
    public String decrypt(String data) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(data);
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
