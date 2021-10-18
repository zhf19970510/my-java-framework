package com.zhf.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 曾鸿发
 * @create: 2021-10-10 09:21
 * @description：Swagger 配置类
 **/


@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
public class Swagger2Config {

    private static final List<String> list = new ArrayList<>();

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .enable(true).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build().pathMapping("/")
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = new ArrayList();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("zhf 框架")
                .description("zhf 框架")
                .build();
    }

    private String getControllerPackage() {
        String path = new File(this.getClass().getResource("/").getPath()).toString()
                .replace("target\\classes", "") + "src\\main\\java\\";
        String packageName = "";
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                getPackagePath(packageName, files);
            }
            StringBuilder sb = new StringBuilder();
            for (String var1 : list) {
                sb.append(var1).append(";");
            }
            String str = sb.toString();
            return str.length() == 0 ? str : str.substring(0, str.length() - 1);
        } else {
            return "";
        }
    }

    private void getPackagePath(String name, File[] files) {
        for (File file : files) {
            StringBuilder sb = new StringBuilder(name);
            if (file.isDirectory()) {
                String fileName = file.getName();
                if ("controller".equals(fileName)) {
                    sb.append(fileName);
                    list.add(sb.toString());
                } else {
                    sb.append(fileName).append(".");
                }
                File[] files1 = file.listFiles();
                if (files1 != null) {
                    getPackagePath(sb.toString(), files1);
                }
            }
        }
    }

}
