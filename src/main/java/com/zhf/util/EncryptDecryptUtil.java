package com.zhf.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class EncryptDecryptUtil {

    public static SimpleStringPBEConfig config(String password) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        config.setPoolSize(1);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    // 加密明文通过一个盐进行加密

    /**
     * 加密明文
     *
     * @param password 加解密的密码
     * @param value    需要加密的值，明文
     * @return
     */
    public static String encrypt(String password, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(config(password));
        return encryptor.encrypt(value);
    }

    /**
     * 解密
     *
     * @param password 加解密的密码
     * @param value    需要解密的值，密文
     * @return
     */
    public static String decrypt(String password, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(config(password));
        return encryptor.decrypt(value);
    }

    public static void main(String[] args) {
        String password = "zhf123456";
        String value = "123456";
        String encrypt = encrypt(password, value);
        System.out.println(encrypt);
        System.out.println(decrypt(password, encrypt));
    }
}
