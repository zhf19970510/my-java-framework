package com.zhf.controller;

import com.zhf.entity.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义类型转换器的时候，一定要注意对应的属性值跟方法跟方法中参数的值要对应起来，
 *
 * @author: 曾鸿发
 * @create: 2021-10-24 00:53
 * @description： 自定义类型转换接口测试类
 **/

@RestController
public class MyConverterController {

    @RequestMapping("/converter")
    public String testConverter(User user, Model model){
        System.out.println(user);
        model.addAttribute("user", user);
        return "success";
    }
}
