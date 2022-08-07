package com.zhf.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: 曾鸿发
 * @create: 2022-06-11 09:45
 * @description：不使用DateFormat的前提下实现日期转换
 **/
public class DateRegexProcessor {

    static final String[] REGEX_ARR = {
            "(\\d{4})/(\\d{2})/(\\d{2})",
            "(\\d{2})/(\\d{2})/(\\d{4})",
            "(\\d{4})(\\d{2})(\\d{2})",
            "(\\d{4})/(\\d{1})/(\\d{1})",
            "(\\d{1})/(\\d{1})/(\\d{4})",
            "(\\d{2})/(\\d{1})/(\\d{4})",
            "(\\d{4})-(\\d{2})-(\\d{2})",
            "(\\d{2})-(\\d{2})-(\\d{4})"
    };

    static final Map<String, String> REGEX_FORMAT = new HashMap<>();

    /**
     * 日期格式转换类，由于date类型格式不固定，需要进行正则模糊匹配
     *
     * @param date              传入的日期
     * @param originalFormat    原始日期格式  ，可能的格式有 yyyy/MM/dd，MM/dd/yyyy，dd/MM/yyyy,yyyy-MM-dd,MM-dd-yyyy,dd-MM-yyyy,yyyyMMdd,yyyy/M/d,M/d/yyyy,d/M/yyyy,MM/d/yyyy,dd/M/yyyy……
     * @return  返回目标类型的日期格式
     */
    public static String processDate(String date, String originalFormat, String targetFormat) {
        String result = null;
        for(String regex : REGEX_ARR){
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(date);
            if(matcher.find()){
                try{
                    SimpleDateFormat originDateFormat = new SimpleDateFormat(originalFormat);
                    SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFormat);
                    Date parseDate = originDateFormat.parse(date);
                    result = targetDateFormat.format(parseDate);
                    break;
                }catch (Exception e){
                    return null;
                }

            }
        }
        return result;
    }

}
