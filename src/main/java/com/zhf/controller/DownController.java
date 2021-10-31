package com.zhf.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 11:27
 * @description：文件下载接口
 **/

@Controller
public class DownController {

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws Exception {
        // 获取要下载的文件的路径
        ServletContext servletContext = request.getServletContext();
        // String realPath = servletContext.getRealPath("./application.yml");
        // 通过IO流对文件进行读写
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/application.yml");
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Disposition", "attachment; filename=" + "application.yml");
        return new ResponseEntity<byte[]>(bytes, httpHeaders, HttpStatus.OK);
    }
}
