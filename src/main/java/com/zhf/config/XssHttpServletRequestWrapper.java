package com.zhf.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * xss漏洞问题解决，针对特殊字符，进行转移处理
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static Logger logger = LoggerFactory.getLogger(XssHttpServletRequestWrapper.class);

    // 白名单数组
    private static final String[] WHITE_LIST = {"content", "userInfo"};

    // 定义SQL注入
    private static final String reg = "(\\b(select|update|union|and|or|delete|insert|truncate|into|substr|ascii|declare|exec|count|drop|execute)\\b)";

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        int count = values.length;

        String[] encodeValues = new String[count];

        for (int i = 0; i < count; i++) {
            // 先给目标赋值，避免最终没有给目标数组存储数值
            encodeValues[i] = values[i];
            // 白名单放行的只有HTML标签，SQL标签还是要验证
            if (isWhiteList(parameter)) {
                if (sqlValidate(values[i])) {
                    encodeValues[i] = values[i];
                } else {
                    if (StringUtils.isNotBlank(values[i])) {
                        encodeValues[i] = StringEscapeUtils.escapeSql(values[i]);
                    }
                }
            } else {
                if (!isJSON(values[i])) {
                    encodeValues[i] = removeHtml(values[i]);
                }
            }
        }
        return encodeValues;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!isJSON(value)) {
            value = removeHtml(value);
        }
        return value;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        if (isWhiteList(name)) {
            if (sqlValidate(value)) {
                return value;
            } else {
                return StringEscapeUtils.escapeSql(value);
            }
        } else {
            return removeHtml(value);
        }
    }

    // \b 表示限定便捷，比如 select 不通过， lselect 则是可以的
    private static final Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

    private static boolean sqlValidate(String str) {
        if (sqlPattern.matcher(str).find()) {
            logger.info("未通过过滤器：str={}", str);
            return false;
        }
        return true;
    }

    private static boolean isWhiteList(String paramName) {

        String lowerParam = paramName.toLowerCase();
        String name = Arrays.stream(WHITE_LIST).filter(x -> x.toLowerCase().equals(lowerParam)).findAny().orElse(null);
        return name != null;
    }

    public static String removeHtml(String htmlStr) {
        if (StringUtils.isBlank(htmlStr)) {
            return htmlStr;
        }
        return HtmlUtils.htmlEscape(htmlStr);
    }

    public static boolean isJSONObject(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isJSON(String str) {
        return isJSONObject(str) || isJSONArray(str);
    }

    public static boolean isJSONArray(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            JSONArray jsonArr = JSONArray.parseArray(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
