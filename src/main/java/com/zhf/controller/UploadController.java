package com.zhf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 13:01
 * @description：上传测试类
 **/
@Controller
public class UploadController {

    @ResponseBody
    @RequestMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam(value = "desc", required = false) String desc) throws IOException {
        System.out.println(desc);
        // 获取文件的名称
        System.out.println(multipartFile.getOriginalFilename());
        multipartFile.transferTo(new File("d:\\file\\" + multipartFile.getOriginalFilename()));
        return "success";
    }
}
