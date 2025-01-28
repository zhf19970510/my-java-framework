package com.zhf.util.pinin;

import lombok.SneakyThrows;

import java.util.Comparator;

public class PinInComparator implements Comparator<String> {

    @SneakyThrows
    @Override
    public int compare(String s1, String s2) {
        byte[] s1Bytes = s1.getBytes("GBK");
        byte[] s2Bytes = s2.getBytes("GBK");

        int s1Size = s1Bytes.length;
        int s2Size = s2Bytes.length;

        int size = Math.min(s1Size, s2Size);

        for (int i = 0; i < size; i++) {
            byte b1 = s1Bytes[i];
            byte b2 = s1Bytes[i];
            if (b1 != b2) {
                return b1 - b2;
            }
        }

        return s1Size - s2Size;
    }
}
