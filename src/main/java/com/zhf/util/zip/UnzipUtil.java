package com.zhf.util.zip;

import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipUtil {

    public static void zipDecompress(String sourcePath, String desPath) {
        try{
            ZipFile zipFile = new ZipFile(sourcePath, Charset.forName("utf-8"));
            System.out.println("上传文件路径：" + sourcePath);
            ZipEntry zipEntry;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
