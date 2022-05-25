package com.zhf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.ToString;

/**
 * @author: 曾鸿发
 * @create: 2021-11-04 21:22
 * @description：账号 model 对象
 **/

@Data
@ToString
@TableName("account")
public class Account {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String card;

    private Double balance;

    @Version
    private Integer version;
}
