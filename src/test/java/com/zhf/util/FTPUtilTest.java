package com.zhf.util;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * @author: 曾鸿发
 * @create: 2022-06-05 18:21
 * @description：
 **/
public class FTPUtilTest {

    FTPProperty property = new FTPProperty();

    {
        property.setModel(FTPProperty.models.PASSIVE.value());
        property.setFtpUserName("ftp1");
        property.setFtpPort(21);
        property.setFtpPath("/");
        property.setFtpPassword("slbftp1");
        property.setFtpHost("192.168.199.128");
    }

    @Test
    public void testGetFTPClient() {
        FTPClient ftpClient = FTPUtils.getFTPClient(property);
        assert ftpClient != null;
        System.out.println(ftpClient.isConnected());
    }

    @Test
    public void testIsFtpFileReady() {
        boolean ftpFileReady = FTPUtils.isFtpFileReady(property, Arrays.asList("myzhf.txt"));
        System.out.println(ftpFileReady);
    }

    @Test
    public void testDownloadFtpFile() {
        boolean success = FTPUtils.downloadFtpFile(property, Arrays.asList("zhfMD5UploadTest.csv"), Arrays.asList("D:\\myftpdata\\"), 0);
        System.out.println(success);
    }

    @Test
    public void testListFile() {
        property.setFtpPath("test");
        List<String> list = FTPUtils.listFile(property);
        list.forEach(System.out::println);
    }

    @Test
    public void testUpload() throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File("D:\\myftpdata\\local.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        boolean success = FTPUtils.uploadFile("192.168.199.128", "ftp1", "slbftp1", 21, "test", "remoteUpload.txt", fileInputStream);
        System.out.println(success);
    }

    @Test
    public void testGenerateCSVAndUploadFileWithMd5() throws IOException, FileErrorException {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("column1", "hahaha");
        map.put("column2", "hehehe");
        Map<String, Object> map2 = new HashMap<>();
        map.put("column1", "aaaaaaaaaaaaaa");
        map.put("column2", "曾鸿发试试中文传输");
        list.add(map);
        list.add(map2);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("column1", "column1");
        linkedHashMap.put("column2", "column2");
        boolean success = FTPUtils.generateCSVAndUploadFileWithMd5("zhfMD5UploadTest", property, list, linkedHashMap, "D:\\test", "UTF-8");
        System.out.println(success);
    }

    @Test
    public void testtestGenerateCSVAndUploadFileWithMd52() throws IOException, FileErrorException {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("column1", "hahaha");
        map.put("column2", "hehehe");
        Map<String, Object> map2 = new HashMap<>();
        map.put("column1", "aaaaaaaaaaaaaa");
        map.put("column2", "曾鸿发试试中文传输");
        list.add(map);
        list.add(map2);
        boolean success = FTPUtils.generateCSVAndUploadFileWithMd5("zhfMD5UploadTest2", property, list,"D:\\test", "UTF-8");
        System.out.println(success);
    }
}
