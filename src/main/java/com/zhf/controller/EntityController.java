package com.zhf.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 10:55
 * @description：Entity测试类
 **/
@Controller
public class EntityController {

    @RequestMapping("/httpEntity")
    public String testHttpEntity(HttpEntity<String> httpEntity) {
        System.out.println(httpEntity);
        return "success";
    }

    @RequestMapping("tesResponseEntity")
    public ResponseEntity<String> tesResponseEntity() {
        String str = "<h1>hello,spring mvc</h1>";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Set-Cookie", "name=zhangsan");
        return new ResponseEntity<String>(str, httpHeaders, HttpStatus.OK);
    }
}
