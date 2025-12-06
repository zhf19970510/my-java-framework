package com.zhf.design.pattern.spring02;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RequestListener implements ApplicationListener<RequestEvent> {


    @Override
    public void onApplicationEvent(RequestEvent requestEvent) {
        System.out.println("监听到事件，开始处理......");
    }
}
