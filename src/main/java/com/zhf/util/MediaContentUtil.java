package com.zhf.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class MediaContentUtil {

    public static String filePath() {
        String osName = System.getProperty("os.name");
        String filePath = "/data/files/";
        if (osName.startsWith("Windows")) {
            filePath = "D:\\" + filePath;
        } else if (osName.startsWith("Mac") || osName.startsWith("Linux")) {
            filePath = "/home/admin" + filePath;
        }
        return filePath;
    }

    public static String encode(String fileName) throws Exception {
        return URLEncoder.encode(fileName, "utf-8");
    }

    public static String decode(String fileName) throws Exception {
        return URLDecoder.decode(fileName, "utf-8");
    }
}
