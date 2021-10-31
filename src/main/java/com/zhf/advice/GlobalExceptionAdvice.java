package com.zhf.advice;

import com.zhf.entity.base.BaseResult;
import com.zhf.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 08:56
 * @description：异常处理类
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 业务异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public BaseResult<?> bizException(ServiceException ex) {
        return BaseResult.error(ex.getCode(), ex.getMessage(), null);
    }

    /**
     * 请求参数校验异常处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BaseResult<?> result;
        BindingResult bindingResult = ex.getBindingResult();
        // 获取当前所有的错误
        List<FieldError> errors = bindingResult.getFieldErrors();
        Map map = new HashMap();
        for (FieldError fieldError : errors){
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        if (bindingResult.hasErrors()) {
            result = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).findFirst()
                    .map(BaseResult::error).orElseGet(() -> BaseResult.error("参数错误"));
        } else {
            result = BaseResult.error("参数错误");
        }
        return result;
    }

    /**
     * 请求入参必填验证
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResult<?> missingRequestParameterException(MissingServletRequestParameterException ex) {
        return BaseResult.error(ex.getMessage());
    }

    /**
     * 主键冲突
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public BaseResult<?> duplicateKeyException(DataIntegrityViolationException ex) {

        log.error(ex.toString());
        return BaseResult.error("违反数据约束,重复或缺失!");
    }

    /**
     * 请求方式出错
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResult<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return BaseResult.error("请求方式出错:" + ex.getMessage());
    }

    /**
     * 参数错误
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResult<?> httpRequestMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return BaseResult.error("参数无效");
    }

    /**
     * 服务器内部处理失败异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    public BaseResult<?> badRequestException(Exception e) {
        log.error("服务器处理错误", e);
        return BaseResult.error("服务器异常，请稍后再试!");
    }
}
