package com.zhf.service;

import com.zhf.job.TestScheduledJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestScheduledJobService {

    @Autowired
    TestScheduledJob testScheduledJob;

    public void schedule() {
        testScheduledJob.task1();
    }
}
