package com.zhf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author: 曾鸿发
 * @create: 2021-09-14 15:10
 * @description：日期工具类
 **/
public class DateUtil {

    /**
     * 根据日期-拼接字符串 转换为年月日中文类型的日期字符串
     * @param dateStr
     * @return
     */
    public static String toChinseDate(String dateStr){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            return simpleDateFormat.format(sdfDate.parse(dateStr));
        } catch (ParseException e) {
            return "1997-01-01";
        }
    }

    /**
     * start
     * 本周开始时间戳 - 以星期一为本周的第一天
     * @return
     */
    public static String getWeekStartTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd", Locale. getDefault());
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar. DAY_OF_WEEK) - 1;
        if (day_of_week == 0 ) {
            day_of_week = 7 ;
        }
        cal.add(Calendar.DATE , -day_of_week + 1 );
        return simpleDateFormat.format(cal.getTime()) + "000000000";
    }


    /**
     * end
     * 本周结束时间戳 - 以星期一为本周的第一天
     */
    public static String getWeekEndTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd", Locale. getDefault());
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar. DAY_OF_WEEK) - 1;
        if (day_of_week == 0 ) {
            day_of_week = 7 ;
        }
        cal.add(Calendar.DATE , -day_of_week + 7 );
        return simpleDateFormat.format(cal.getTime()) + "235959999";
    }

    public static boolean checkDateInSection(String checkDate, String startTime, String endTime){
        return checkDate != null && checkDate.compareTo(startTime) > 0 && checkDate.compareTo(endTime) < 0;
    }

}
