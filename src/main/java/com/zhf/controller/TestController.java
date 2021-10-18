package com.zhf.controller;

import com.zhf.entity.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 曾鸿发
 * @create: 2021-10-10 09:09
 * @description：Test Controller
 **/

@Api(tags = "测试接口")
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @ApiOperation("测试一")
    @GetMapping("/test1")
    public BaseResult<String> test1(@ApiParam("参数一") @RequestParam(value = "arg1", required = true) String arg1){
        // double a = 1 /  0;
        return BaseResult.success(arg1 + "zhf");
    }
}
