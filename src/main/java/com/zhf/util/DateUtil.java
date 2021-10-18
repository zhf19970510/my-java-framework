package com.zhf.util;

import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    /**
     * 根据选择的是本日、本月还是本年获取对应的开始时间和结束时间
     * @param flag 本日：d ， 本月：m ， 本年： y
     *
     */
    public static Map<String, String> getBetweenTimeByFlag(String flag){
        Map<String, String> map = new HashMap<>();
        // 获取当前时间
        Calendar calendar =  Calendar.getInstance();
        if("d".equals(flag)){
            // 获取本日开始时间和结束时间
            map.put("startTime", getDayStart());
            map.put("endTime", getDayEnd());
        }else if("m".equals(flag)){
            map.put("startTime", getMonthStart());
            map.put("endTime", getMonthEnd());
        }else{
            map.put("startTime", getYearStart());
            map.put("endTime", getYearEnd());
        }

        return map;
    }

    /**
     * 获取当天0点时间
     *
     */
    public static String getDayStart(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当天23:59:59的时间
     *
     */
    public static String getDayEnd(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当月开始时间
     *
     */
    public static String getMonthStart(){
        Calendar cal = Calendar.getInstance();
        return getDayStartTime(cal);
    }

    /**
     * 获取当月结束时间
     *
     */
    public static String getMonthEnd(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当年开始时间
     *
     */
    public static String getYearStart(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);
        return getDayStartTime(cal);
    }

    /**
     * 获取当年结束时间
     *
     */
    public static String getYearEnd(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取对应月份第一天0点0分0秒的时间
     *
     */
    private static String getDayStartTime(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
