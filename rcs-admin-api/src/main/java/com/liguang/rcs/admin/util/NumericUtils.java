package com.liguang.rcs.admin.util;

public class NumericUtils {

    /**
     * 将字符串转换成Long，如果转换失败则返回null，不对外抛异常
     */
    public static final Long toLong(String value) {
        return toLong(value, null);
    }
    /**
     * 将字符串转换成Long，如果转换失败则返回defaultVal，不对外抛异常
     */
    public static final Long toLong(String value, Long defaultVal) {
        try {
            return Long.parseLong(value);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static Double toDouble(String source) {
        return toDouble(source, null);
    }

    public static Double toDouble(String source, Double defaultVal) {
        try {
            return Double.parseDouble(source);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static Integer toInteger(String source) {
        return toInteger(source, null);
    }

    public static Integer toInteger(String value, Integer defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return defaultVal;
        }
    }
}
