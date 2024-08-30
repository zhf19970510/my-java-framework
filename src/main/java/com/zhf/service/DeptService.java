package com.zhf.service;

import com.zhf.entity.DeptResp;

import java.util.List;

public interface DeptService {

    void calculateDwDept();

    List<DeptResp> queryEachDeptToLevel(Integer level);
}
