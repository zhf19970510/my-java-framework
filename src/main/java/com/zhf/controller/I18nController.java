package com.zhf.controller;

import com.zhf.localeResolver.MyLocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 16:57
 * @description：国际化语言标准测试类
 **/
@Controller
public class I18nController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MyLocaleResolver myLocaleResolver;

    @ResponseBody
    @RequestMapping("/i18n")
    public String i18n(HttpServletRequest request){
        System.out.println(messageSource);
        Locale locale = myLocaleResolver.resolveLocale(request);
        System.out.println(locale);
        String username = messageSource.getMessage("username", null, locale);
        System.out.println(username);
        return username;
    }

    @ResponseBody
    @RequestMapping("/i18n1")
    public String i18n1(Locale locale){
        System.out.println(messageSource);
        System.out.println(locale);
        String username = messageSource.getMessage("username", null, locale);
        System.out.println(username);
        return username;
    }
}
