package com.zhf.util.pojo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class Student {

    private String name;

    private String age;

    private String no;

}
