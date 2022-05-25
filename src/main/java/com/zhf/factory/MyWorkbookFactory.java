package com.zhf.factory;

import com.zhf.exception.ServiceException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * @author: 曾鸿发
 * @create: 2022-05-24 16:01
 * @description：generate workbook to parse Excel
 **/
public class MyWorkbookFactory {



    /**
     * 网上现有的
     */
    public static Workbook generateWorkbook(String filePath, String password) throws IOException, GeneralSecurityException {
        File excelFile = new File(filePath);
        return generateWorkbook(excelFile, password);
    }

    public static Workbook generateWorkbook(File excelFile, String password) throws IOException, GeneralSecurityException {
        InputStream is = new FileInputStream(excelFile);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(is);
        is.close();
        EncryptionInfo encryptionInfo = new EncryptionInfo(poifsFileSystem);
        Decryptor decryptor = Decryptor.getInstance(encryptionInfo);
        boolean verifyPassword = decryptor.verifyPassword(password);
        if(!verifyPassword){
            throw new ServiceException("excel 密码错误！");
        }
        String fileSuffix = excelFile.getName().substring(excelFile.getName().lastIndexOf(".") + 1).toLowerCase();
        if("xlsx".equals(fileSuffix)){
            return new XSSFWorkbook(decryptor.getDataStream(poifsFileSystem));
        }else if("xls".equals(fileSuffix)){
            return new HSSFWorkbook(decryptor.getDataStream(poifsFileSystem));
        }else{
            throw new ServiceException("excel cannot recognize file suffix！");
        }

    }

    public static Workbook generateWorkbook2(File excelFile, String password) throws IOException {
        return WorkbookFactory.create(excelFile, password);
    }

}
