package com.zhf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: 曾鸿发
 * @create: 2021-09-22 21:10
 * @description：启动类
 **/
@SpringBootApplication
@EnableSwagger2
public class MyJavaFrameworkApplication {

    public static void main(String[] args) {
        // SpringApplication application = new SpringApplication(MyJavaFrameworkApplication.class);
        // application.run(args);
        SpringApplication.run(MyJavaFrameworkApplication.class,args);
    }
}
