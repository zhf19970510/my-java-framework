package com.zhf.util;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class PersonDTO {
    private String name;
    private Integer age;
    private Date birthday;
}