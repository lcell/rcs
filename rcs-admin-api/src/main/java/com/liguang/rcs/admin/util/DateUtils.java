package com.liguang.rcs.admin.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期转换工具
 */
public class DateUtils {
    public static Timestamp toTimestamp(String dataStr, String format) throws ParseException {
        Date date = toDate(dataStr, format);
        return new Timestamp(date.getTime());
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
