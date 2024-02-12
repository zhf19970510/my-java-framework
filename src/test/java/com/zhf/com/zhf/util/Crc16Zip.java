package com.zhf.com.zhf.util;


import cn.hutool.core.io.checksum.crc16.CRC16Modbus;
import com.zhf.exception.ServiceException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Crc16Zip {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String input = "第一行广告";
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        System.out.println(Integer.toHexString(calculateCRC16(bytes)));
        System.out.println("===================");
        bytes = input.getBytes(StandardCharsets.UTF_8);
        System.out.println(bytes);
        System.out.println(bytesToHexString(bytes));
        System.out.println(Integer.toHexString(calculateCRC16Modbus(bytes)));

//        byte[] bytes1 = new byte[] {0x01, 0x64, 0x00,0x11, 0x00, 0x0B, 0X01, 0XB5, 0XDA, };

        String hexString = "01640011000B01B5DAD2BBD0D0B9E3B8E60000";
//        String s = hexToBinary(hexString);
//        System.out.println(s);
        String hexStr1 = "fa";
        System.out.println(hexToBinary(hexStr1));

    }

    private static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String hexToBinary(String hex) {
        int i = Integer.parseInt(hex, 16);
        String binary = Integer.toBinaryString(i);
        return binary;
    }

    public static int calculateCRC16Modbus(byte[] bytes) {
        Checksum checksum = new CRC16Modbus();
        checksum.update(bytes, 0, bytes.length);
        return (int) checksum.getValue() & 0xFFFF;
    }

    public static int calculateCRC16(byte[] bytes) {
        Checksum crc16 = new CRC32();
        crc16.update(bytes, 0, bytes.length);
        return (int) crc16.getValue() & 0xFFFF;
    }


    public static String passwordEncode(String data) {
        try {
            MessageDigest sha = MessageDigest.getInstance("CRC16");
            byte[] dataArray = data.getBytes("UTF-8");
            sha.update(dataArray);
            byte[] shaBytes = sha.digest();

            for(int j = 0; j < 5000; j++){
                sha.update(shaBytes);
                shaBytes = sha.digest();
            }

            StringBuffer hexValue = new StringBuffer();

            for(int i = 0; i < shaBytes.length; i++){
                int val = ((int) shaBytes[i]) & 0xff;
                if(val < 16){
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString();

        }catch (Exception e){
            throw new ServiceException("密码加密错误！");
        }
    }
}
