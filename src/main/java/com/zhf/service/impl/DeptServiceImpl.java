package com.zhf.service.impl;

import com.zhf.dao.DeptMapper;
import com.zhf.entity.DeptResp;
import com.zhf.entity.TDept;
import com.zhf.service.DeptService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public void calculateDwDept() {
        deptMapper.deleteAll();

        Set<String> hasCircledDeptIdSet = new HashSet<>();
        Stack<TDept> stack = new Stack<>();
        List<TDept> headDeptList = Optional.ofNullable(deptMapper.queryAllHeadDept()).orElse(new ArrayList<>());
        pushToStack(headDeptList, stack, hasCircledDeptIdSet);
        while (!stack.isEmpty()) {
            TDept pop = stack.pop();
            String supDeptId = pop.getDeptId();
            deptMapper.insertIntoDwDept(supDeptId, supDeptId);
            List<TDept> deptList = Optional.ofNullable(deptMapper.queryAllSubDeptByHeadDeptId(supDeptId)).orElse(new ArrayList<>());
            for (TDept tDept : deptList) {
                deptMapper.insertIntoDwDept(supDeptId, tDept.getDeptId());
            }
            pushToStack(deptList, stack, hasCircledDeptIdSet);

        }
    }

    @Override
    public List<DeptResp> queryEachDeptToLevel(Integer level) {
        return deptMapper.queryEachDeptToLevel(level);
    }

    public void pushToStack(List<TDept> deptList, Stack<TDept> stack, Set<String> hasCircledDeptIdSet) {
        if (CollectionUtils.isEmpty(deptList)) {
            return;
        }
        for (TDept tDept : deptList) {
            if (!hasCircledDeptIdSet.contains(tDept.getDeptId())) {
                stack.push(tDept);
                hasCircledDeptIdSet.add(tDept.getDeptId());
            }
        }
    }
}
