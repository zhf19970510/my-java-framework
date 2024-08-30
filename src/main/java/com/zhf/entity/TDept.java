package com.zhf.entity;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
* 
* @TableName t_dept
*/
public class TDept implements Serializable {

    /**
    * 部门id
    */
    @NotBlank(message="[部门id]不能为空")
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("部门id")
    @Length(max= 20,message="编码长度不能超过20")
    private String deptId;
    /**
    * 部门名称
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("部门名称")
    @Length(max= 255,message="编码长度不能超过255")
    private String deptName;
    /**
    * 部门层级
    */
    @ApiModelProperty("部门层级")
    private Integer deptLevel;
    /**
    * 直接上级机构id
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("直接上级机构id")
    @Length(max= 20,message="编码长度不能超过20")
    private String supDept;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(Integer deptLevel) {
        this.deptLevel = deptLevel;
    }

    public String getSupDept() {
        return supDept;
    }

    public void setSupDept(String supDept) {
        this.supDept = supDept;
    }
}
