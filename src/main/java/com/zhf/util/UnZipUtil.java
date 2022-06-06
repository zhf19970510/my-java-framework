package com.zhf.util;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author: 曾鸿发
 * @create: 2022-06-05 23:10
 * @description：
 **/
public abstract class UnZipUtil {

    public void unZip(InputStream stream){
        ZipInputStream zis = new ZipInputStream(stream);
        ZipEntry ze = null;

    }

}
