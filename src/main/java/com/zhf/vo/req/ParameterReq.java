package com.zhf.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: 曾鸿发
 * @create: 2021-11-18 14:23
 * @description：参数请求对象
 **/
@Data
@ApiModel("参数请求对象")
public class ParameterReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("参数一")
    private String arg1;

    @ApiModelProperty("参数二")
    private String arg2;
}
