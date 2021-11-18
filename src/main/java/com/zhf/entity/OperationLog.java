package com.zhf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: 曾鸿发
 * @create: 2021-11-12 11:13
 * @description：日志操作记录实体类
 **/
@Data
@TableName("operation_log")
public class OperationLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作日志内容")
    private String logInfo;

    @ApiModelProperty(value = "创建人id")
    private Integer createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;
}
