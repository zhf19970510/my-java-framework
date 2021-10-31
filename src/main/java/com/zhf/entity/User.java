package com.zhf.entity;

import lombok.Data;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 00:58
 * @description：用户实体类
 **/
@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private Integer age;


}
