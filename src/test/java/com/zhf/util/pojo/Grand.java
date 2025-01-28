package com.zhf.util.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Grand {
    private String grandName;

    private String boss;

    private List<Student> studentList;
}
