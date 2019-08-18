package com.liguang.rcs.admin.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
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
            log.warn("convert to Long Exception, value:{}", value);
            return defaultVal;
        }
    }
/******** Double 相关的处理**********/
    public static Double toDouble(String source) {
        return toDouble(source, null);
    }

    public static Double toDouble(String source, Double defaultVal) {
        try {
            return Double.parseDouble(source);
        } catch (Exception ex) {
            log.warn("convert to Double Exception, value:{}", source);
            return defaultVal;
        }
    }

    public static Double minus(Double double1, Double double2, Double... doubles) {
        Double result = minus(double1, double2);
        if (doubles == null || doubles.length == 0) {
            return  result;
        }
        for(Double double3 : doubles) {
            result = minus(result, double3);
        }
        return result;
    }

    private static Double minus(Double double1, Double double2) {
        if (isNullOrZero(double2)) {
            return double1;
        }
        if (isNullOrZero(double1)) {
            return -double2;
        }
        return double1 - double2;
    }

    public static Double plus(Double double1, Double double2, Double... doubles) {
        Double result = plus(double1, double2);
        if (doubles == null || doubles.length == 0) {
            return  result;
        }
        for(Double double3 : doubles) {
            result = plus(result, double3);
        }
        return result;
    }
    private static Double plus(Double double1, Double double2) {
        if (isNullOrZero(double1)) {
            return double2;
        }
        if (isNullOrZero(double2)) {
            return double1;
        }
        return double1 + double2;
    }

    public static Double div(Double double1, Double double2) {
        if (isNullOrZero(double1)) {
            return 0D;
        }
        if (isNullOrZero(double2)) {
            return 1D;
        }
        return formatDouble(double1 / double2, 2);
    }

    public static boolean isLtZero(Double minus) {
        return isNullOrZero(minus) || minus < 0;
    }

    public static Double formatDouble(Double double1, int scale) {
        if (isNullOrZero(double1)) {
            return 0d;
        }
        return new BigDecimal(double1).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
/******* Double 相关的处理结束  ************/

    public static Integer toInteger(String source) {
        return toInteger(source, null);
    }

    public static Integer toInteger(String value, Integer defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            log.warn("convert to Integer Exception, value:{}", value);
            return defaultVal;
        }
    }

    public static Float toFloat(String source) {
        return toFloat(source, null);
    }

    private static Float toFloat(String source, Float defaultVal) {
        try {
            return Float.parseFloat(source);
        } catch (Exception ex) {
            log.warn("convert to Float Exception, value:{}", source);
            return defaultVal;
        }
    }


    public static boolean isNullOrZero(Number num) {
        return num == null || num.doubleValue() == 0;
    }

}
