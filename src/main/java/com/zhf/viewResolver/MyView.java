package com.zhf.viewResolver;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 00:06
 * @description：自定义视图类
 **/

public class MyView implements View {

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("model:" + model);
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.write("<h1>曾鸿发zhf.123</h1>");
        writer.write("123456");
    }


}
