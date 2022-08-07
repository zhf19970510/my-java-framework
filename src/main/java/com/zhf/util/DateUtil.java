package com.zhf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author: 曾鸿发
 * @create: 2021-09-14 15:10
 * @description：日期工具类
 **/
public class DateUtil {

    /**
     * 根据日期-拼接字符串 转换为年月日中文类型的日期字符串
     *
     * @param dateStr
     * @return
     */
    public static String toChinseDate(String dateStr) {
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
     *
     * @return
     */
    public static String getWeekStartTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 1);
        return simpleDateFormat.format(cal.getTime()) + "000000000";
    }


    /**
     * end
     * 本周结束时间戳 - 以星期一为本周的第一天
     */
    public static String getWeekEndTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 7);
        return simpleDateFormat.format(cal.getTime()) + "235959999";
    }

    public static boolean checkDateInSection(String checkDate, String startTime, String endTime) {
        return checkDate != null && checkDate.compareTo(startTime) > 0 && checkDate.compareTo(endTime) < 0;
    }

    /**
     * 根据选择的是本日、本月还是本年获取对应的开始时间和结束时间
     *
     * @param flag 本日：d ， 本月：m ， 本年： y
     */
    public static Map<String, String> getBetweenTimeByFlag(String flag) {
        Map<String, String> map = new HashMap<>();
        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        if ("d".equals(flag)) {
            // 获取本日开始时间和结束时间
            map.put("startTime", getDayStart());
            map.put("endTime", getDayEnd());
        } else if ("m".equals(flag)) {
            map.put("startTime", getMonthStart());
            map.put("endTime", getMonthEnd());
        } else {
            map.put("startTime", getYearStart());
            map.put("endTime", getYearEnd());
        }

        return map;
    }

    /**
     * 获取当天0点时间
     */
    public static String getDayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当天23:59:59的时间
     */
    public static String getDayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当月开始时间
     */
    public static String getMonthStart() {
        Calendar cal = Calendar.getInstance();
        return getDayStartTime(cal);
    }

    /**
     * 获取当月结束时间
     */
    public static String getMonthEnd() {
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
     */
    public static String getYearStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);
        return getDayStartTime(cal);
    }

    /**
     * 获取当年结束时间
     */
    public static String getYearEnd() {
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
     */
    private static String getDayStartTime(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }

    /**
     * 得到 X sec后时间
     *
     * @param d
     * @param sec
     * @return
     */
    public static Date getDateBeforeSec(Date d, int sec) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) - sec);
        return now.getTime();
    }

    /**
     * 通过修改时间和密码有效期，获取对应的密码过期时间
     *
     * @param updateTime     修改时间
     * @param expireDuration 密码有效期长
     * @return 密码过期时间
     */
    public static Date getPasswordExpireTime(Date updateTime, Long expireDuration) {
        updateTime = new Date();
        Long endTime = updateTime.getTime() + expireDuration;
        Date dateEnd = new Date(endTime);
        return dateEnd;
    }

    /**
     * @param date1 日期1
     * @param date2 日期2
     * @return 日期1在日期2后面返回true
     */
    public static boolean compareDay(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date1Str = sdf.format(date1);
        String date2Str = sdf.format(date2);
        return date1Str.compareTo(date2Str) >= 0;
    }

    /**
     * 获取开始日期到结束日期之间的天数
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 天数
     */
    public static long getDays(LocalDate start, LocalDate end) {
        return Math.abs(start.toEpochDay() - end.toEpochDay());
    }

    /**
     * date 转换为 LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        LocalDate localDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        return localDate;
    }

    public static LocalDate dateToLocalDate2(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * LocalDate to Date
     *
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * date 转换为String
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        if (null == date) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * String to date
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String date, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(date);
    }

    public static LocalDate objectToLocalDate(Object objDate) {
        if (objDate == null) {
            return null;
        }
        LocalDate date = LocalDate.now();
        if (objDate instanceof String) {
            String dateA = (String) objDate;
            if (dateA.contains("/")) {
                date = LocalDate.parse(dateA, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            } else if (dateA.contains("-")) {
                date = LocalDate.parse(dateA, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else {
                date = LocalDate.parse(dateA, DateTimeFormatter.ofPattern("yyyyMMdd"));
            }
        } else if (objDate instanceof Date) {
            Date dateC = (Date) objDate;
            date = dateToLocalDate(dateC);
        } else if (objDate instanceof LocalDate) {
            date = (LocalDate) objDate;
        } else if (objDate instanceof Long) {
            date = (LocalDate) objDate;
        } else if (objDate instanceof Long) {
            date = dateToLocalDate(new Date((Long) objDate));
        } else if (objDate instanceof Integer) {
            // 这个看具体业务场景而进行修改
            date = LocalDate.parse(String.valueOf(objDate), DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        return date;
    }

    public static Date objectToDate(Object objDate) {
        return localDateToDate(objectToLocalDate(objDate));
    }

    public static String formDate(Object objDate, String formatter) throws ParseException {
        if (objDate == null) {
            return "";
        }
        Date date = null;
        if (objDate instanceof String) {
            String dateA = (String) objDate;
            if (dateA.contains("/")) {
                date = stringToDate(dateA, "yyyy/MM/dd");
            } else if (dateA.contains("-")) {
                date = stringToDate(dateA, "yyyy-MM-dd");
            } else if ("".equals(dateA)) {
                return "";
            } else {
                date = stringToDate(dateA, "yyyyMMdd");
            }
        } else if (objDate instanceof Date) {
            date = (Date) objDate;
        } else if (objDate instanceof LocalDate) {
            date = localDateToDate((LocalDate) objDate);
        } else if (objDate instanceof Long) {
            date = new Date((Long) objDate);
        }

        String dateRet = dateToString(date, formatter);
        return dateRet;
    }

    public static void getAllAvailableZoneId() {
        String[] availableIDs = TimeZone.getAvailableIDs();
        for (int i = 0; i < availableIDs.length; i++) {
            System.out.println("zoneId " + i + " : " + availableIDs[i]);
        }
    }

    public static String parseDateTimeGmt(String time, String zoneId){
        // 例如：20220316 06:17:39.00.000000 +0000
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSSSSS Z");
        try{
            // 1. 先转换为中国时区时间
            Date date = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
            TimeZone timeZone = TimeZone.getTimeZone(zoneId);
            sdf2.setTimeZone(timeZone);
            return sdf2.format(date);
        }catch (Exception e){
            return "error";
        }
    }
}
