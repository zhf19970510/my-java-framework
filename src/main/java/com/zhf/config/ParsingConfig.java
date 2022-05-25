package com.zhf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: 曾鸿发
 * @create: 2022-04-19 18:26
 * @description：
 **/
@Component
public class ParsingConfig {

    @Value("${group.booking.amount.tolerance:50}")
    private String groupBookingAmountTolerance;

    public BigDecimal getGroupBookingAmountTolerance() {
        return new BigDecimal(groupBookingAmountTolerance);
    }

    public void setGroupBookingAmountTolerance(String groupBookingAmountTolerance) {
        this.groupBookingAmountTolerance = groupBookingAmountTolerance;
    }
}
