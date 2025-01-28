package com.zhf.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

/**
 * 使用此种方式实现自定义的RequestMappingHandlerMapping在springBoot 2.1.7版本实现没有问题，但是在2.2.4版本报错了，但是也不想继续研究了，只是说提供了一个引子，可以提供此种方式实现自定义RequestMappingHandlerMapping，实现一些特殊逻辑的处理。
 * 比如可以在自定义RequestMappingHandlerMapping中针对·特殊的拦截器进行拦截处理，可以参考 ZhfRequestMappingHandleMapping
 */
//@Configuration
public class ZhfWebMvcConfigurationSupport extends DelegatingWebMvcConfiguration {

    //    @Bean
    public ZhfRequestMappingHandleMapping zhfRequestMappingHandleMapping(
            @Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager
            ,
            @Qualifier("mvcConversionService") FormattingConversionService conversionService,
            @Qualifier("mvcResourceUrlProvider") ResourceUrlProvider resourceUrlProvider
    ) {
        ZhfRequestMappingHandleMapping mapping = new ZhfRequestMappingHandleMapping();
        mapping.setOrder(-1);
        mapping.setInterceptors(getInterceptors(conversionService, resourceUrlProvider));
        mapping.setContentNegotiationManager(contentNegotiationManager);
        mapping.setCorsConfigurations(getCorsConfigurations());
        return mapping;
    }
}
