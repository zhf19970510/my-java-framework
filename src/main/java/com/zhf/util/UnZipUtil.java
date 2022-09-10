package com.zhf.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 还可以参考：https://blog.csdn.net/Qq949426/article/details/126173250
 * TODO: 里面包含了各种压缩包的解压方式，到时候可以参考一下，不过我还没来得及看
 * @author: 曾鸿发
 * @create: 2022-06-05 23:10
 * @description：
 **/
public abstract class UnZipUtil {

    public void unZip(InputStream stream) throws Exception {
        ZipInputStream zis = new ZipInputStream(stream);
        ZipEntry ze;
        while (((ze = zis.getNextEntry()) != null) && !ze.isDirectory()) {
            String name = ze.getName();
            if (filter(name)) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int num;
                // 通过 read 方法来读取文件内容
                while ((num = zis.read(bytes, 0, bytes.length)) > -1) {
                    bos.write(bytes, 0, num);
                }
            }
        }
        zis.close();
    }

    /**
     * 子类可以自定义实现文件过滤规则，默认不过滤
     *
     * @param fileName  文件名
     * @return
     */
    protected boolean filter(String fileName) {
        return true;
    }

    protected abstract void afterUnzip(String fileName, byte[] fileBytes) throws Exception;

}
