package com.zhf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhf.entity.Device;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2021-10-31 16:33
 * @description：device 表数据访问层
 **/

@Mapper
public interface DeviceDao extends BaseMapper<Device> {

    @MapKey("point_number")
    Map<String, Device> selectAll();

    @Update("update device set model = 'hahaha111' where id = #{id}")
    void updateMovingRingById(Integer id);
}
