package com.zhf.entity;

import com.zhf.constant.CommonConstant;
import com.zhf.entity.base.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: 曾鸿发
 * @create: 2021-09-22 20:52
 * @description：
 **/
@Data
public class Person implements BaseEntity {

    private Integer id;

    private String name;

    private Integer age;

    @Override
    public String getBussinessKey() {
        return id + CommonConstant.splitter + (StringUtils.isBlank(name) ? "" : name);
    }
}
