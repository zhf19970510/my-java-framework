package com.zhf.controller;


import com.zhf.dao.DecimalTestDao;
import com.zhf.entity.DecimalTest;
import com.zhf.entity.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;


@RestController
@RequestMapping("/decimalTest")
public class DecimalTestController {

    @Autowired
    private DecimalTestDao decimalTestDao;

    @RequestMapping("/queryDecimalTestInfoById")
    public BaseResult<DecimalTest> queryDecimalTestInfoById(int id){
        DecimalTest decimalTest = decimalTestDao.selectByPrimaryKey(id);
        return new BaseResult<DecimalTest>().success(decimalTest);
    }

    @RequestMapping("/insertDecimalTestInfo")
    public BaseResult<String> insertDecimalTestInfo(){
        DecimalTest decimalTest = new DecimalTest();
        decimalTest.setAge(new BigDecimal("9.8888").setScale(8, RoundingMode.HALF_UP));
        decimalTestDao.insertSelective(decimalTest);
        return new BaseResult<>().success("插入数据成功！");
    }

}
