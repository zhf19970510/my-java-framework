package com.zhf.util.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;

import java.io.FileInputStream;

@Slf4j
public class ReadDocFileUtil {

    public static String getDocContent(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            // 创建 HWPFDocument 对象来表示 .doc 文档
            HWPFDocument doc = new HWPFDocument(fis);
            // 创建 WordExtractor 对象
        } catch (Exception e) {
            log.error("ReadDocFileUtil getContent error: ", e);
        }
        return null;
    }
}
