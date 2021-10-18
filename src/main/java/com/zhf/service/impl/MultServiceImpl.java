package com.zhf.service.impl;

import com.zhf.service.MultService;
import com.zhf.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 曾鸿发
 * @create: 2021-10-10 22:42
 * @description：
 **/
@Service
public class MultServiceImpl implements MultService {

    @Autowired
    private TestService testService;


    @Override
    public void mult() {
        testService.test1();
        testService.test2();
    }
}
