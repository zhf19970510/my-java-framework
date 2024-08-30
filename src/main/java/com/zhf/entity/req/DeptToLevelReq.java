package com.zhf.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "查询到指定层级的所有机构-请求对象")
public class DeptToLevelReq {

    @ApiModelProperty(value = "机构层级")
    @NotNull
    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
