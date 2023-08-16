package com.zhf.constraints;

import com.zhf.controller.VehicleTypeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeRangeValidator implements ConstraintValidator<DateTimeRange, Object> {

    private DateTimeRange dateTimeRange;

    private static Logger logger = LoggerFactory.getLogger(VehicleTypeController.class);

    @Override
    public void initialize(DateTimeRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object paramDate, ConstraintValidatorContext context) {
        if(paramDate == null) {
            return true;
        }

        // 用户传进来的是日期参数
        LocalDateTime dateValue = null;
        if(paramDate instanceof LocalDateTime) {
            dateValue = (LocalDateTime) paramDate;
        }

        if(paramDate instanceof String) {
            dateValue = LocalDateTime.parse((String) paramDate, DateTimeFormatter.ofPattern(dateTimeRange.pattern()));
        }

        LocalDateTime now = LocalDateTime.now();

        if (dateValue.isAfter(now)) {
            return true;
        }

        return false;
    }

}
