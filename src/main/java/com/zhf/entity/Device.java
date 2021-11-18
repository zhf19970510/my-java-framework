package com.zhf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;
import java.util.Objects;

@Data
@TableName("device")
@Builder
public class Device {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备名")
    private String name;

    @ApiModelProperty(value = "设备编号")
    private String deviceNumber;

    @ApiModelProperty(value = "设备型号")
    private String model;

    @ApiModelProperty(value = "所属系统")
    private String system;

    @ApiModelProperty(value = "设备类型")
    private String type;

    @ApiModelProperty(value = "设备楼宇")
    private String building;

    @ApiModelProperty(value = "设备楼层")
    private String floor;

    @ApiModelProperty(value = "设备位置")
    private String location;

    @ApiModelProperty(value = "点位号")
    private String pointNumber;

    @ApiModelProperty(value = "维保状态")
    private Integer state;

    @ApiModelProperty(value = "设备地址，目前视频地址")
    private String deviceUrl = "";

    @ApiModelProperty(value = "判断是否是设备，判断设备类型用 0 消防 不纳入设备 1设备 2视频")
    private Integer isDevice = 0;

    @ApiModelProperty(value = "点位类型（0 温度 1 湿度 2 噪声）")
    private Integer pointType;

    @ApiModelProperty(value = "显示用名")
    private String showName;

    @ApiModelProperty(value = "设备状态，判断设备状态 0正常 1异常")
    private Integer status = 0;

    @ApiModelProperty(value = "阈值类型，无阈值、数值阈值")
    private String thresholdType;

    @ApiModelProperty(value = "阈值类型为数值型时，分三种关系：小于、大于、介于")
    private String thresholdRelation;

    @ApiModelProperty(value = "阈值类型为数值型时，该值必填")
    private Double thresholdValue1;

    @ApiModelProperty(value = "阈值类型为数值型时，关系为介于时，该值必填，小于和等于不填")
    private Double thresholdValue2;

    @ApiModelProperty(value = "阈值类型为数值型时，阈值单位必填")
    private String thresholdUnit;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "从bos平台转到烟草前端面板展示状态，对应bos平台取的value为0")
    private String bosValueToStatus0;

    @ApiModelProperty(value = "从bos平台转到烟草前端面板展示状态，对应bos平台取的value为1")
    private String bosValueToStatus1;

    @ApiModelProperty(value = "点位描述")
    private String pointDesc;

    @ApiModelProperty(value = "查询条件")
    @TableField(exist = false)
    private String condition;

    @ApiModelProperty(value = "数值")
    @TableField(exist = false)
    private String value;

    @ApiModelProperty(value = "内容检索")
    @TableField(exist = false)
    private String conten;

    @TableField(exist = false)
    @ApiModelProperty(value = "过滤排除的设备")
    private List<Integer> notIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "查询的ID集合")
    private List<Integer> inIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "消防主机编号")
    private String fireNumber;

    @Tolerate
    public Device() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(deviceNumber, device.deviceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceNumber);
    }
}
