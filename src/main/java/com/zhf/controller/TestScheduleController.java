package com.zhf.controller;

import com.zhf.entity.base.BaseResult;
import com.zhf.service.TestScheduledJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class TestScheduleController {

    @Autowired
    TestScheduledJobService testScheduledJobService;

    @PostMapping("/task")
    public BaseResult<String> task() {
        testScheduledJobService.schedule();
        return new BaseResult<>(0, "success");
    }
}
