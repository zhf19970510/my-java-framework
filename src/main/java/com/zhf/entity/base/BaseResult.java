package com.zhf.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author: 曾鸿发
 * @create: 2021-10-10 09:51
 * @description：
 **/
@Data
@AllArgsConstructor
public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 4762264923215132025L;

    protected Integer code = 200;
    protected String msg = "success";
    protected T data;

    public BaseResult() {
        super();
    }

    public BaseResult(Integer code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public static <T> BaseResult<T> error(String msg) {
        return new BaseResult<T>(-1, msg);
    }

    public static <T> BaseResult<T> error(String msg, T data) {
        BaseResult<T> baseResult = new BaseResult<T>(-1, msg);
        baseResult.setData(data);
        return baseResult;
    }

    public static <T> BaseResult<T> error(Integer code, String msg, T data) {
        BaseResult<T> baseResult = new BaseResult<T>(code, msg);
        baseResult.setData(data);
        return baseResult;
    }

    public static <T> BaseResult<T> success() {
        return success(null, null);
    }

    public static <T> BaseResult<T> success(T data) {
        return success(null, data);
    }

    public static <T> BaseResult<T> success(String msg, T data) {
        BaseResult<T> baseResult = new BaseResult<T>();
        if (StringUtils.isNotBlank(msg)) {
            baseResult.setMsg(msg);
            baseResult.setCode(200);
        }
        baseResult.setData(data);
        return baseResult;
    }

    /**
     * 判断结果集是否返回成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return this.code == 200;
    }

}
