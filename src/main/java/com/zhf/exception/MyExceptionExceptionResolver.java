package com.zhf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyExceptionExceptionResolver extends ExceptionHandlerExceptionResolver {

    public MyExceptionExceptionResolver() {
        List<HandlerMethodArgumentResolver> list = new ArrayList<>();
        list.add(new ServletModelAttributeMethodProcessor(true));
        this.setCustomArgumentResolvers(list);

    }

    /**
     * 参考已有的异常处理器方法执行逻辑以及正常的请求执行逻辑加工的异常处理器执行逻辑，可作为一个扩展点
     */
    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
        // org.springframework.http.HttpStatus      定义了一些创建的状态码
        // 找到对应的异常处理的方法
        ServletInvocableHandlerMethod exceptionHandlerMethod = this.getExceptionHandlerMethod(handlerMethod, exception);
        // 判断找到的异常处理方法是否为空，如果为空，直接返回
        if (exceptionHandlerMethod == null) {
            return null;
        }

        // 给空值不让它进行解析
        exceptionHandlerMethod.setDataBinderFactory(new ServletRequestDataBinderFactory(null, null));
        // 设置参数处理器，返回值处理器
        exceptionHandlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolvers());
        exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(getReturnValueHandlers());


        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        ArrayList<Throwable> exceptions = new ArrayList();

        try {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Using @ExceptionHandler " + exceptionHandlerMethod);
            }

            Throwable cause;
            for(Throwable exToExpose = exception; exToExpose != null; exToExpose = cause != exToExpose ? cause : null) {
                exceptions.add(exToExpose);
                cause = ((Throwable)exToExpose).getCause();
            }

            Object[] arguments = new Object[exceptions.size() + 1];
            exceptions.toArray(arguments);
            arguments[arguments.length - 1] = handlerMethod;
            exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer, arguments);
        } catch (Throwable var13) {
            if (!exceptions.contains(var13) && this.logger.isWarnEnabled()) {
                this.logger.warn("Failure in @ExceptionHandler " + exceptionHandlerMethod, var13);
            }

            return null;
        }

        if (mavContainer.isRequestHandled()) {
            return new ModelAndView();
        } else {
            ModelMap model = mavContainer.getModel();
            HttpStatus status = mavContainer.getStatus();
            ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model, status);
            mav.setViewName(mavContainer.getViewName());
            if (!mavContainer.isViewReference()) {
                mav.setView((View)mavContainer.getView());
            }

            if (model instanceof RedirectAttributes) {
                Map<String, ?> flashAttributes = ((RedirectAttributes)model).getFlashAttributes();
                RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
            }

            return mav;
        }
    }
}
