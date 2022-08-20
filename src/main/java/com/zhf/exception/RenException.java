package com.zhf.exception;

import com.zhf.enums.ErrorEnum;
import org.quartz.SchedulerException;

public class RenException extends SchedulerException {

    public RenException(String msg) {
        super(msg);
    }

    public RenException(ErrorEnum errorEnum) {
        super(errorEnum.getErrorMessage());
    }

}
