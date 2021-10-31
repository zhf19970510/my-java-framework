package com.zhf.localeResolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author: 曾鸿发
 * @create: 2021-10-24 18:00
 * @description：自定义国际化语言设置
 **/
@Component("localeResolver")
public class MyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale;
        String localeStr = request.getParameter("locale");
        if (StringUtils.isNotBlank(localeStr)) {
            locale = new Locale(localeStr.split("_")[0], localeStr.split("_")[1]);
        } else locale = request.getLocale();
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        throw new UnsupportedOperationException("Cannot change HTTP accept header - use a different locale resolution strategy");
    }
}
