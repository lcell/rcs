package com.liguang.rcs.admin.util;

import com.liguang.rcs.admin.common.enumeration.IEnum;

public class EnumUtils {

    /**
     * 通过code查找对应的枚举值
     */
    public static <T extends IEnum > T findByCode(T[] values, String code) {
        for(T value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
