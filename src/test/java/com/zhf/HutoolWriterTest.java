package com.zhf;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class HutoolWriterTest {

    public static void main(String[] args) {
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("姓名", "张三");
        row1.put("年龄", 23);
        row1.put("成绩", 88.32);
        row1.put("是否合格", true);
        row1.put("考试日期", DateUtil.date());
        row1.put("我的成绩", 0);

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("姓名", "李四");
        row2.put("年龄", 33.33);
        row2.put("成绩", 59.50);
        row2.put("是否合格", false);
        row2.put("考试日期", DateUtil.date());
        row2.put("我的成绩", 1);

        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);

        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("d:/writeMapTest190.xlsx");

//        StyleSet style = writer.getStyleSet();// 获取整个Excel的格式
//        CellStyle numberCellStyle = style.getCellStyleForNumber();
//        DataFormat dataFormat = writer.getWorkbook().createDataFormat();
//        numberCellStyle.setDataFormat(dataFormat.getFormat("0.00000"));     // 设置5位小数格式
//        writer.getSheet().setDefaultColumnStyle(2, numberCellStyle);

//        StyleSet style = writer.getStyleSet();
//        CellStyle numberCellStyle = style.getCellStyleForNumber();
//        DataFormat dataFormat = writer.getWorkbook().createDataFormat();
//        numberCellStyle.setDataFormat(dataFormat.getFormat("0.00000"));
//        writer.setStyle(numberCellStyle,  1, 0); // 设置[0,2]单元格的样式
//
//        CellStyle numberCellStyle1 = style.getCellStyleForNumber();
//        DataFormat dataFormat1 = writer.getWorkbook().createDataFormat();
//        numberCellStyle1.setDataFormat(dataFormat1.getFormat("0.000"));
//        writer.setStyle(numberCellStyle1,  2, 2); // 设置[0,2]单元格的样式

//        ArrayList<String> headers = new ArrayList<>(row2.keySet());
//        writer.writeRow(headers);

        // 合并单元格后的标题行，使用默认标题样式
//        writer.merge(row1.size() - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
//        writer.write(rows, true);
        int rowNum = 0;
        for (Map<String, Object> row : rows) {
            int columnNum = 0;
            for (Map.Entry<String, Object> stringObjectEntry : row.entrySet()) {
                writeCell(writer, rowNum, columnNum, stringObjectEntry.getValue());
                columnNum++;
            }
            rowNum++;
        }
        // 关闭writer，释放内存
        writer.close();
    }

    private static void writeCell(ExcelWriter writer, int row, int column, Object cellValue) {
        // 根据x,y轴设置单元格内容
        writer.writeCellValue(column, row, cellValue);
//        if(column == 4){
//            return;
//        }
//        Font font = writer.createFont();
//        font.setColor(Font.COLOR_RED);
        // 根据x,y轴获取当前单元格样式
        CellStyle cellStyle = writer.createCellStyle(column, row);
//        // 内容水平居中
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//        // 内容垂直居中
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        // 设置边框
//        cellStyle.setBorderBottom(BorderStyle.THIN);
//        cellStyle.setBorderLeft(BorderStyle.THIN);
//        cellStyle.setBorderRight(BorderStyle.THIN);
//        // 字体颜色标红
//        cellStyle.setFont(font);

        if (column == 1) {
            DataFormat dataFormat1 = writer.getWorkbook().createDataFormat();
            cellStyle.setDataFormat(dataFormat1.getFormat("0.000"));
        }

        if(column == 2){
            DataFormat dataFormat1 = writer.getWorkbook().createDataFormat();
            cellStyle.setDataFormat(dataFormat1.getFormat("0.00000"));
        }

    }

}
