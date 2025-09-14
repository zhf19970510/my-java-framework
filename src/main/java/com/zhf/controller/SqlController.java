package com.zhf.controller;

import com.zhf.entity.Device;
import com.zhf.entity.base.BaseResult;
import com.zhf.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sql")
public class SqlController {

    @Autowired
    SqlService sqlService;


    @PostMapping("/getSqlQueryResult")
    public BaseResult<List<Map<String, Object>>> getSqlQueryResult(@RequestBody String sql) {
        return  new BaseResult<Map<String, Device>>().success(sqlService.getSqlQueryResult(sql));
    }

    @PostMapping("/updateRecordBySql")
    public BaseResult<Integer> updateRecordBySql(@RequestBody String sql) {
        return  new BaseResult<Integer>().success(sqlService.updateRecordBySql(sql));

    }

    @PostMapping("/deleteRecordBySql")
    public BaseResult<Integer> deleteRecordBySql(@RequestBody String sql) {
        return  new BaseResult<Integer>().success(sqlService.deleteRecordBySql(sql));

    }

    @PostMapping("/insertRecordBySql")
    public BaseResult<Integer> insertRecordBySql(@RequestBody String sql) {
        return  new BaseResult<Integer>().success(sqlService.insertRecordBySql(sql));

    }
}
