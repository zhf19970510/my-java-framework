package com.zhf.service;

import com.zhf.entity.Device;

import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2021-10-31 16:38
 * @description：device 接口
 **/
public interface DeviceService {

    /**
     * 查询所有设备信息
     * @return  键值对，键是点位号，值是设备对象
     */
    Map<String, Device> selectAll();

    /**
     * 根据id修改设备信息
     * @param id
     */
    void updateById(Integer id);

    void insertLog();

    void deleteLog();

}
