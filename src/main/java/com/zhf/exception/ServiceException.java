package com.zhf.exception;

import com.zhf.enums.BaseEnum;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 09:01
 * @description：业务异常类
 **/
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -6873174195938670725L;
    private int code;

    public int getCode() {
        return code;
    }

    public ServiceException(String message) {
        this(-1, message);
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 自定义错误响应的状态码和信息
     *
     * @param err 将状态码枚举继承{@link BaseEnum}即可
     */
    public ServiceException(BaseEnum err) {
        super(err.getName());
        this.code = err.getCode();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}
