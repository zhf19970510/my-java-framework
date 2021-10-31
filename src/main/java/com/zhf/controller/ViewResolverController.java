package com.zhf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: 曾鸿发
 * @create: 2021-10-23 23:59
 * @description： 自定义视图解析器处理测试类
 **/

@Controller
public class ViewResolverController {

    @RequestMapping("/msb")
    public String testView(){
        System.out.println("testView");
        return "msb:/index";
    }

    @RequestMapping("/msb2")
    public String testView2(){
        System.out.println("testView");
        return "heihei:/index";
    }
}
