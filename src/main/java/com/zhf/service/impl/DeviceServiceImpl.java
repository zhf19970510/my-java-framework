package com.zhf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhf.dao.DeviceDao;
import com.zhf.dao.OperationLogDao;
import com.zhf.entity.Device;
import com.zhf.entity.OperationLog;
import com.zhf.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2021-10-31 16:40
 * @description： device 接口实现类
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private OperationLogDao operationLogDao;

    @Override
    public Map<String, Device> selectAll() {
        return deviceDao.selectAll();
    }

    public void updateById(Integer id){
        deviceDao.updateMovingRingById(id);
    }

    public void insertLog(){
        OperationLog operationLog = new OperationLog();
        operationLog.setLogInfo("hahaha1");
        operationLog.setCreatedBy(1);
        operationLogDao.insert(operationLog);
    }

    public void deleteLog(){
        LambdaQueryWrapper<OperationLog> queryWrapper = new QueryWrapper<OperationLog>().lambda();
        queryWrapper.eq(OperationLog::getCreatedBy, "1");
        operationLogDao.delete(queryWrapper);
    }

}
