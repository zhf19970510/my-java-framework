package com.zhf.entity.dto;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhf.constraints.DateTimeRange;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class DateTimeTestDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @DateTimeRange
    private LocalDateTime nowTime;


    public LocalDateTime getNowTime() {
        return nowTime;
    }

    public void setNowTime(LocalDateTime nowTime) {
        this.nowTime = nowTime;
    }
}
