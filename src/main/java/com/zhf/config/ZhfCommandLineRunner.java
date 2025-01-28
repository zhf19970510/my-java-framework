package com.zhf.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 启动项目：获取controller所有接口
 */
@Component
public class ZhfCommandLineRunner implements CommandLineRunner {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void run(String... args) throws Exception {
//        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
//        List<String> list = new ArrayList<>();
//        handlerMethods.forEach((key, value) -> {
//            assert key.getPatternsCondition() != null;
//            System.out.println("接口url ：" + key.getPatternsCondition().getPatterns());
//            list.addAll(key.getPatternsCondition().getPatterns());
//        });
    }
}
