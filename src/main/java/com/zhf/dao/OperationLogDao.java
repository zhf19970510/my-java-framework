package com.zhf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhf.entity.Device;
import com.zhf.entity.OperationLog;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2021-10-31 16:33
 * @description：operation_log 表数据访问层
 **/

@Mapper
public interface OperationLogDao extends BaseMapper<OperationLog> {

}
