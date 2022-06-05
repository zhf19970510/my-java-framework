package com.zhf.util;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2022-02-12 10:20
 * @description：操作Csv读写的工具类
 **/
public class OperateCsvUtil {

    /**
     * 将数据写入csv文件
     *
     * @param filePath 文件路径
     * @param titles   csv标题
     * @param data     要写入的数据
     */
    @SneakyThrows
    public static void writeDataToCsv(String filePath, String[] titles, List<String[]> data) {
        CsvWriter csvWriter = new CsvWriter(new FileOutputStream(filePath), ',', StandardCharsets.UTF_8);
        csvWriter.writeRecord(titles);
        for (String[] d : data) {
            csvWriter.writeRecord(d);
        }
        csvWriter.close();
    }

    /**
     * 读取包含csv头部的文件
     *
     * @param filePath
     * @return 读取的数据
     */
    @SneakyThrows
    public static List<String[]> readDataFromFile(String filePath) {
        CsvReader csvReader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
        csvReader.readHeaders();
        int headerCount = csvReader.getHeaderCount();
        List<String[]> retData = new ArrayList<>();
        while (csvReader.readRecord()) {
            String[] ret = new String[headerCount];
            for (int i = 0; i < ret.length; i++) {
                if (csvReader.get(i) == null) {
                    continue;
                }
                ret[i] = csvReader.get(i);
            }
            retData.add(ret);
        }
        return retData;
    }

    @SneakyThrows
    public static <T> List<T> readCsvDataToListEntity(String filePath, Map<String, String> fieldNameToCsvMap, Class<T> cls) {
        List<T> retList = new ArrayList<>();
        CsvReader csvReader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
        csvReader.readHeaders();
        int headerCount = csvReader.getHeaderCount();
        Map<String, Integer> headerNameIndexMap = new HashMap<>();
        String[] headers = csvReader.getHeaders();
        for (int i = 0; i < headerCount; i++) {
            headerNameIndexMap.put(headers[i], i);
        }
        Map<Integer, Field> indexFieldMap = getIndexFieldMap(fieldNameToCsvMap, headerNameIndexMap, cls);
        while (csvReader.readRecord()) {
            T t = cls.newInstance();
            for (int i = 0; i < headerCount; i++) {
                String columnData = csvReader.get(i);
                if (columnData == null) {
                    continue;
                }
                Field field = indexFieldMap.get(i);
                field.set(t, csvReader.get(i));
            }
            retList.add(t);
        }
        return retList;
    }

    public static <T> Field[] getFieldByClass(Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

    public static <T> Map<Integer, Field> getIndexFieldMap(Map<String, String> fieldNameToCsvMap, Map<String, Integer> headerNameIndexMap, Class<T> cls) {
        Map<Integer, Field> map = new HashMap<>();
        Field[] fields = getFieldByClass(cls);
        for (Field field : fields) {
            String csvHeaderName = fieldNameToCsvMap.get(field.getName());
            Integer index = headerNameIndexMap.get(csvHeaderName);
            map.put(index, field);
        }
        return map;
    }

}
