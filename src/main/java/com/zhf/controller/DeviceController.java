package com.zhf.controller;

import com.zhf.entity.Device;
import com.zhf.entity.base.BaseResult;
import com.zhf.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2021-10-31 16:53
 * @description：设备相关接口
 **/
@Api(tags = "设备")
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @ApiOperation("查询所有设备信息")
    @RequestMapping("/selectAll")
    public  BaseResult<Map<String, Device>> selectAll() {
        Map<String, Device> stringDeviceMap = deviceService.selectAll();
        return new BaseResult<Map<String, Device>>().success(stringDeviceMap);
    }

    @ApiOperation("根据id修改设备信息")
    @RequestMapping("/updateById")
    public BaseResult<String> updateById(){
        deviceService.updateById(1);
        return new BaseResult<String>();
    }

    @ApiOperation("插入日志信息")
    @RequestMapping("/insertLog")
    public BaseResult<String> insertLog(){
        deviceService.insertLog();
        return new BaseResult<String>();
    }

    @ApiOperation("删除日志信息")
    @RequestMapping("/deleteLog")
    public BaseResult<String> deleteLog(){
        deviceService.deleteLog();
        return new BaseResult<>();
    }
}
