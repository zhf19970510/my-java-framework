package com.zhf.config;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ZhfRequestMappingHandleMapping extends RequestMappingHandlerMapping {

    private Set<String> cacheSet;

    @Override
    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
        HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ? (HandlerExecutionChain) handler : new HandlerExecutionChain(handler));

        String lookupPath = this.getUrlPathHelper().getLookupPathForRequest(request);
        for (HandlerInterceptor interceptor : this.getAdaptedInterceptors()) {
            if (interceptor instanceof MappedInterceptor) {
                MappedInterceptor mappedInterceptor = (MappedInterceptor) interceptor;
                if (mappedInterceptor.getInterceptor().getClass().isAssignableFrom(TestInterceptor.class)) {
                    if (getMatcherUrls().contains(lookupPath)) {
                        chain.addInterceptor(mappedInterceptor.getInterceptor());
                    }
                } else if (mappedInterceptor.matches(lookupPath, this.getPathMatcher())) {
                    chain.addInterceptor(mappedInterceptor.getInterceptor());
                }
            } else {
                chain.addInterceptor(interceptor);
            }
        }
        return chain;
    }

    public Set<String> getMatcherUrls() {
        if (CollectionUtils.isNotEmpty(cacheSet)) {
            return cacheSet;
        }
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.getHandlerMethods();
        this.cacheSet = handlerMethods.keySet().stream().map(t -> t.getPatternsCondition().getPatterns()).flatMap(item -> item.stream().filter(t -> Objects.nonNull(t) && !"/error".equals(t))).collect(Collectors.toSet());
        return cacheSet;
    }
}
