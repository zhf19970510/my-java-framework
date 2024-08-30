package com.zhf.controller;

import com.zhf.entity.DeptResp;
import com.zhf.entity.base.BaseResult;
import com.zhf.entity.req.DeptToLevelReq;
import com.zhf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept")
public class DwDeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/calculateDwDept")
    public BaseResult<String> calculateDwDept() {
        deptService.calculateDwDept();
        return new BaseResult<>(0, "success");
    }

    /**
     * 查询到指定层级的所有机构
      */
    @PostMapping("/queryeachDeptToLevel")
    public BaseResult<List<DeptResp>> queryEachDeptToLevel(@RequestBody DeptToLevelReq req) {
        List<DeptResp> deptRespList = deptService.queryEachDeptToLevel(req.getLevel());
        return new BaseResult<>(0, "查询成功", deptRespList);
    }
}
