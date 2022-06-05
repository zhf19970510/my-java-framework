package com.zhf.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

/**
 * @author: 曾鸿发
 * @create: 2022-06-05 01:19
 * @description：
 **/
public class OutExcelUtil {

    public static final Logger logger = LoggerFactory.getLogger(OutExcelUtil.class);

    public OutExcelUtil() {
    }

    public void exportToExcel(ResultSet rs, List<String> columnNames, String outputFilePath, String fileName, boolean isOverwriting) throws Exception {
        List<List<String>> columentValues = new ArrayList<>();
        List<String> columnNameStrs = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        boolean isFirstRow = false;

        while (rs.next()) {
            List<String> columnValueList = new ArrayList<>();
            for (int i = 1; i <= colCount; i++) {
                String columnValue;
                if (isFirstRow) {
                    columnValue = rsmd.getColumnLabel(i);
                    columnNameStrs.add(columnValue);
                }

                columnValue = rs.getString(i);
                columnValueList.add(columnValue);
            }

            isFirstRow = false;
            columentValues.add(columnValueList);
        }

        if (columnNames == null) {
            columnNames = columnNameStrs;
        }
        exportExcelTool(columnNames, columentValues, outputFilePath, fileName, isOverwriting);
    }

    public void exportToExcel(List<Map<String, String>> resultMap, List<String> columnNames, String outputFilePath, String fileName, boolean isOverwriting) throws IOException, FileErrorException {
        List<List<String>> columnValues = new ArrayList<>();
        List<String> columnNameStrs = new ArrayList<>();
        boolean isFirstRow = false;
        Iterator<Map<String, String>> var9 = resultMap.iterator();
        while (var9.hasNext()) {
            Map<String, String> map = var9.next();
            List<String> columnValueList = new ArrayList<>();
            Iterator<Map.Entry<String, String>> var12 = map.entrySet().iterator();
            while (var12.hasNext()) {
                Map.Entry<String, String> entry = var12.next();
                String columnValue;
                if (isFirstRow) {
                    columnValue = entry.getKey();
                    columnNameStrs.add(columnValue);
                }

                columnValue = entry.getValue();
                columnValueList.add(columnValue);
            }

            isFirstRow = false;
            columnValues.add(columnValueList);
        }

        if (columnNames == null) {
            columnNames = columnNameStrs;
        }

        exportExcelTool(columnNames, columnValues, outputFilePath, fileName, isOverwriting);
    }

    public void exportExcelTool(List<String> columnNames, List<List<String>> columnValues, String outputFilePath, String fileName, boolean isOverWriting) throws FileErrorException, IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        XSSFRow row = sheet.createRow(0);
        int i = 0;

        String fileNameFlag;
        for (Iterator var10 = columnNames.iterator(); var10.hasNext(); ++i) {
            fileNameFlag = (String) var10.next();
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(fileNameFlag);
        }

        int j = 1;

        for (Iterator var22 = columnValues.iterator(); var22.hasNext(); ++j) {
            List<String> columnValueList = (List<String>) var22.next();
            XSSFRow rowV = sheet.createRow(j);

            int k = 0;
            for (Iterator var15 = columnValueList.iterator(); var15.hasNext(); ++k) {
                String columnValue = (String) var15.next();
                XSSFCell cellV = rowV.createCell(k);
                cellV.setCellValue(columnValue);
            }
        }

        fileNameFlag = ".xlsx";
        if (!fileNameFlag.equals(fileName.substring(fileName.length() - 5))) {
            throw new FileErrorException("报表文件名后缀应为.xlsx");
        } else {
            File filePath = new File(outputFilePath);
            File file = new File(outputFilePath + File.separator + fileName);
            if (!filePath.exists()) {
                filePath.mkdirs();
                if (!file.createNewFile()) {
                    throw new FileErrorException("新建文件失败");
                }
            }

            if (file.exists() && isOverWriting) {
                if (!file.delete()) {
                    throw new FileErrorException("文件删除失败");
                }

                filePath.mkdirs();

                if (!file.createNewFile()) {
                    throw new FileErrorException("新建文件失败！");
                }
            }

            if (file.exists() && !isOverWriting) {
                throw new FileErrorException("该报表已存在");
            } else {
                FileOutputStream os = new FileOutputStream(file);

                try {
                    wb.write(os);
                } finally {
                    os.close();
                }
            }
        }

    }

    public static boolean generateCSVAndUploadFTP(List<Map<String, Object>> columnValues, FTPProperty property, String tmpPath, String fileName, FileUtil.GenerateType generateType, String separator, String encode) throws IOException, FileErrorException {
        Map<String, String> columnNames = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(columnValues)) {
            return false;
        }
        Iterator<Map.Entry<String, Object>> iterator = columnValues.get(0).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            columnNames.put(entry.getKey(), entry.getKey());
        }
        return generateCSVAndUploadFTP(columnValues, columnNames, property, tmpPath, fileName, generateType, separator, encode);
    }

    public static boolean generateCSVAndUploadFTP(List<Map<String, Object>> columnValues, Map<String, String> columnNames, FTPProperty property, String tempPath, String fileName, FileUtil.GenerateType generateType, String separator, String encode) throws IOException, FileErrorException {
        if ("\"".equals(separator)) {
            logger.warn("separator could not be set as \" ! ");
            return false;
        } else {
            List<Map<String, Object>> dataList = new ArrayList<>();

            for (int i = 0; i < columnValues.size(); i++) {
                Map<String, Object> tempList = new LinkedHashMap<>();
                Iterator iterator = columnNames.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) iterator.next();
                    tempList.put(entry.getKey(), columnValues.get(i).get(entry.getKey()));
                }

                dataList.add(tempList);
            }

            StringBuffer headerBuffer = new StringBuffer();
            Iterator nameIt = columnNames.entrySet().iterator();

            while (nameIt.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) nameIt.next();
                headerBuffer.append(generateCellValue(entry.getValue(), separator));
                if(nameIt.hasNext()){
                    headerBuffer = headerBuffer.append(separator);
                }else{
                    headerBuffer = headerBuffer.append("\r\n");
                }
            }

            StringBuffer valueBuffer = new StringBuffer();
            Iterator<Map<String, Object>> var21 = dataList.iterator();

            while (var21.hasNext()){
                Map<String, Object> columnValue = var21.next();
                Iterator<Map.Entry<String, Object>> valueIt = columnValue.entrySet().iterator();

                while (valueIt.hasNext()){
                    Map.Entry<String, Object> entry = valueIt.next();
                    Object data = entry.getValue() == null ? "" : entry.getValue();
                    valueBuffer = valueBuffer.append(generateCellValue(data, separator));
                    if(valueIt.hasNext()){
                        valueBuffer = valueBuffer.append(separator);
                    }else{
                        valueBuffer = valueBuffer.append("\r\n");
                    }
                }
            }

            StringBuffer content = new StringBuffer();
            content = content.append(headerBuffer).append(valueBuffer);
            FileUtil.generateFileAndUploadToFTP(content.toString(), encode, property, tempPath, fileName, generateType);
            return true;
        }
    }


    public static boolean generateCSV(List<Map<String, Object>> columnValues, String tmpPath, String fileName, FileUtil.GenerateType generateType, String separator, String encode) throws IOException, FileErrorException {
        Map<String, String> columnNames = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(columnValues)) {
            return false;
        }
        Iterator<Map.Entry<String, Object>> iterator = columnValues.get(0).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            columnNames.put(entry.getKey(), entry.getKey());
        }
        return generateCSV(columnValues, columnNames, tmpPath, fileName, generateType, separator, encode);
    }

    public static boolean generateCSV(List<Map<String, Object>> columnValues, Map<String, String> columnNames, String tempPath, String fileName, FileUtil.GenerateType generateType, String separator, String encode) throws IOException, FileErrorException {
        if ("\"".equals(separator)) {
            logger.warn("separator could not be set as \" ! ");
            return false;
        } else {
            List<Map<String, Object>> dataList = new ArrayList<>();

            for (int i = 0; i < columnValues.size(); i++) {
                Map<String, Object> tempList = new LinkedHashMap<>();
                Iterator iterator = columnNames.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) iterator.next();
                    tempList.put(entry.getKey(), columnValues.get(i).get(entry.getKey()));
                }

                dataList.add(tempList);
            }

            StringBuffer headerBuffer = new StringBuffer();
            Iterator nameIt = columnNames.entrySet().iterator();

            while (nameIt.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) nameIt.next();
                headerBuffer.append(generateCellValue(entry.getValue(), separator));
                if(nameIt.hasNext()){
                    headerBuffer = headerBuffer.append(separator);
                }else{
                    headerBuffer = headerBuffer.append("\r\n");
                }
            }

            StringBuffer valueBuffer = new StringBuffer();
            Iterator<Map<String, Object>> var21 = dataList.iterator();

            while (var21.hasNext()){
                Map<String, Object> columnValue = var21.next();
                Iterator<Map.Entry<String, Object>> valueIt = columnValue.entrySet().iterator();

                while (valueIt.hasNext()){
                    Map.Entry<String, Object> entry = valueIt.next();
                    Object data = entry.getValue() == null ? "" : entry.getValue();
                    valueBuffer = valueBuffer.append(generateCellValue(data, separator));
                    if(valueIt.hasNext()){
                        valueBuffer = valueBuffer.append(separator);
                    }else{
                        valueBuffer = valueBuffer.append("\r\n");
                    }
                }
            }

            StringBuffer content = new StringBuffer();
            content = content.append(headerBuffer).append(valueBuffer);
            FileUtil.generateFile(content.toString(), encode, tempPath, fileName, generateType);
            return true;
        }
    }

    public static String generateCellValue(Object value, String splitor) {
        if(value == null){
            return null;
        }
        String valueStr = value.toString();
        String returnStr = valueStr;
        if(valueStr.contains("\"")){
            returnStr = valueStr.replace("\"", "\"\"");
        }

        if(valueStr.contains(splitor)){
            returnStr = "\"" + returnStr + "\"";
        }
        return returnStr;
    }

}
