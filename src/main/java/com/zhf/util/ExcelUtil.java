package com.zhf.util;

import com.zhf.enums.excel.ExcelColumn;
import com.zhf.exception.ServiceException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.omg.CORBA.INTERNAL;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import sun.nio.ch.Net;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 曾鸿发
 * @create: 2022-03-04 10:45
 * @description：
 **/
public class ExcelUtil {

    public static <T> List<T> decodeInputStream(InputStream inputStream, int columnNum, Class<T> cls) throws IOException, InvalidFormatException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Workbook wb = WorkbookFactory.create(inputStream);

        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null) {
            throw new ServiceException("uploaded excel does not have any excel sheet!");
        }

        int lastRow = Math.max(sheet.getLastRowNum(), sheet.getPhysicalNumberOfRows());
        if (lastRow == 0) {
            throw new ServiceException("uploaded excel does not have any rows!");
        }

        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(t -> !Modifier.isFinal(t.getModifiers()) && !Modifier.isStatic(t.getModifiers())).collect(Collectors.toList());

        Map<String, Method> fieldNameMethodMap = new HashMap<>();
        for (Field field : fieldList) {
            String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            fieldNameMethodMap.put(field.getName(), cls.getMethod(methodName));
        }

        List<T> contents = new ArrayList<>();
        for (int i = 0; i <= lastRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                // skip empty rows
                continue;
            }
            if (row.getLastCellNum() < 3) {
                throw new ServiceException(String.format("uploaded excel does not have proper format in row %d", i));
            }

            if (i == 0) {
                // skip header
                continue;
            }

            T t = cls.newInstance();

            for (Field field : fieldList) {
                ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                if (excelColumn == null) {
                    continue;
                }
                int columnIndex = excelColumn.columnIndex();
                Cell curCell = row.getCell(columnIndex);
                Method method = fieldNameMethodMap.get(field.getName());
                String cellValue = curCell.getStringCellValue() == null ? null : curCell.getStringCellValue().trim();

                // TODO 数据类型校验
                method.invoke(t, cellValue);
            }
            contents.add(t);
        }
        return contents;
    }

    public static List<Map<String, String>> getExcelData(Sheet sheet) {
        int lastRow = Math.max(sheet.getLastRowNum(), sheet.getPhysicalNumberOfRows());
        Row row = sheet.getRow(0);
        Map<Integer, String> headerMap = new HashMap<>();
        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            if (cell != null && cell.getCellType() == CellType.STRING) {
                headerMap.put(j, cell.getStringCellValue());
            }
        }

        List<Map<String, String>> excelData = new ArrayList<>();

        for (int k = 1; k < lastRow; k++) {
            Row row1 = sheet.getRow(k);
            Map<String, String> rowData = new HashMap<>();
            for (int m = row1.getFirstCellNum(); m < row1.getLastCellNum(); m++) {
                Cell cell = row1.getCell(m);
                if (cell != null) {
                    if(cell.getCellType() != CellType.STRING){
                        cell.setCellType(CellType.STRING);
                    }
                    rowData.put(headerMap.get(m), cell.getStringCellValue());
                }
            }
            excelData.add(rowData);
        }
        return excelData;
    }
}
