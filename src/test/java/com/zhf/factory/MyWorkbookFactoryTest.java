package com.zhf.factory;

import com.zhf.MyJavaFrameworkApplication;
import com.zhf.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.List;
import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2022-05-24 17:45
 * @description：
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyJavaFrameworkApplication.class)
public class MyWorkbookFactoryTest {

    @Test
    public void test1(){

    }

    @Test
    public void testGenerateWorkbook() throws IOException, GeneralSecurityException {
        String filePath = "C:\\Users\\zhf\\Desktop\\pay_order_1.xlsx";
        File file = new File(filePath);
        String password = "ohyyds";
        Workbook workbook = MyWorkbookFactory.generateWorkbook(file, password);
        Sheet sheet = workbook.getSheetAt(0);
        List<Map<String, String>> excelData = ExcelUtil.getExcelData(sheet);
        for (Map<String, String> excelDatum : excelData) {
            excelDatum.forEach((k , v) ->{
                System.out.println(k + "\t" + v);
            });
            System.out.println("===========================");
        }
    }

    @Test
    public void testGenerateWorkbook2() throws IOException {
        String filePath = "C:\\Users\\zhf\\Desktop\\pay_order_1.xlsx";
        File file = new File(filePath);
        String password = "ohyyds";
        Workbook workbook = MyWorkbookFactory.generateWorkbook2(file, password);
        Sheet sheet = workbook.getSheetAt(0);
        List<Map<String, String>> excelData = ExcelUtil.getExcelData(sheet);
        for (Map<String, String> excelDatum : excelData) {
            excelDatum.forEach((k , v) ->{
                System.out.println(k + "\t" + v);
            });
            System.out.println("===========================");
        }
    }

}
