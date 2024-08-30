package com.zhf.entity;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "查询到指定层级的所有机构")
public class DeptResp {

    private String deptId;

    private String supDeptId;

    private String deptName;

    private String supDeptName;

    private Integer deptLeve;

    private Integer supDeptLevel;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getSupDeptId() {
        return supDeptId;
    }

    public void setSupDeptId(String supDeptId) {
        this.supDeptId = supDeptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getSupDeptName() {
        return supDeptName;
    }

    public void setSupDeptName(String supDeptName) {
        this.supDeptName = supDeptName;
    }

    public Integer getDeptLeve() {
        return deptLeve;
    }

    public void setDeptLeve(Integer deptLeve) {
        this.deptLeve = deptLeve;
    }

    public Integer getSupDeptLevel() {
        return supDeptLevel;
    }

    public void setSupDeptLevel(Integer supDeptLevel) {
        this.supDeptLevel = supDeptLevel;
    }
}
