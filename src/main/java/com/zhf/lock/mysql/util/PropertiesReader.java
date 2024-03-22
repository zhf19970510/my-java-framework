package com.zhf.lock.mysql.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PropertiesReader {

    // Properties缓存文件
    private static final Map<String, Properties> propertiesCache = new HashMap<String, Properties>();

    public static Properties getProperties(String propertiesName) throws IOException {
        if (propertiesCache.containsKey(propertiesName)) {
            return propertiesCache.get(propertiesName);
        }
        loadProperties(propertiesName);
        return propertiesCache.get(propertiesName);
    }

    private synchronized static void loadProperties(String propertiesName) throws IOException {
        FileReader fileReader = null;

        try {
            // 创建Properties集合类
            Properties pro = new Properties();
            // 获取src路径下的文件--->ClassLoader类加载器
            ClassLoader classLoader = PropertiesReader.class.getClassLoader();
            URL resource = classLoader.getResource(propertiesName);
            // 获取配置路径
            String path = resource.getPath();
            // 读取文件
            fileReader = new FileReader(path);
            // 加载文件
            pro.load(fileReader);
            // 初始化
            propertiesCache.put(propertiesName, pro);
        } catch (IOException e) {
            log.error("读取Properties文件失败，Properties名为:" + propertiesName);
            throw e;
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                log.error("fileReader关闭失败！", e);
            }
        }
    }
}