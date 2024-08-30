package com.zhf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhf.entity.DeptResp;
import com.zhf.entity.TDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptMapper extends BaseMapper<TDept> {

    List<TDept> queryAllSubDeptByHeadDeptId(@Param("deptId") String deptId);

    List<TDept> queryAllHeadDept();

    void deleteAll();

    int insertIntoDwDept(@Param("supDeptId") String supDeptId, @Param("subDeptId") String subDeptId);

    List<DeptResp> queryEachDeptToLevel(@Param("level") Integer level);
}
