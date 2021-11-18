package com.zhf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 00:58
 * @description：用户实体类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    private String username;

    private String password;

    private Integer age;


}
