package com.liguang.rcs.admin.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期转换工具
 */
@Slf4j
public class DateUtils {
    public static Timestamp toTimestamp(String dataStr, String format) throws ParseException {
        Date date = toDate(dataStr, format);
        return new Timestamp(date.getTime());
    }

    public static Timestamp softToTimestamp(String dataStr, String format) {
        try {
            Date date = toDate(dataStr, format);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            log.warn("[Utils] convert to Timestamp fail, return null. dataStr:{}, format:{}", dataStr, format);
            return null;
        }
    }

    public static long dateMinus(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
    }
    public static long dateMinus(String date1, String date2, String format) throws ParseException {
        return dateMinus(toDate(date1, format), toDate(date2, format));
    }
    //当前时间 - 输入日期
    public static long dateMinusToNow(String date, String format) throws ParseException {
        String dateStr = toString(Calendar.getInstance().getTime(), format);
        return dateMinus(dateStr, date, format);
    }

    public static Date toDate(String dataStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dataStr);
    }

    public static Date softToDate(String monthStr, String format) {
        try {
            return toDate(monthStr, format);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String toString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String toString(Timestamp timestamp, String format) {
        return toString(toDate(timestamp), format);
    }

    public static Date toDate(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar.getTime();
    }
    public static int dateMinusForMonth(Date date1, String date2Str, String format) throws ParseException {
        return dateMinusForMonth(date1, toDate(date2Str, format));
    }

    public static int dateMinusForMonth(String date1Str, String date2Str, String format) throws ParseException {
        return dateMinusForMonth(toDate(date1Str, format), toDate(date2Str, format));
    }

    public static int dateMinusForMonth(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int deltaYear = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
        int deltaMonth = calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH);
        return deltaMonth + (deltaYear * 12);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(dateMinusForMonth("201107", "201208", "yyyyMM"));
    }

}
