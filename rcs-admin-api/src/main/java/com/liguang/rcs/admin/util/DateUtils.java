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

    public static Date toDate(String dataStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dataStr);
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
}
