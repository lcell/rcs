package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.enumeration.IEnum;

public class StringToIEnumConverter<T extends IEnum> extends AbstractIEnmuStringConverter<String, T> {
    @Override
    public T convert(String source, String extMsg, Class clazz) {
        return stringToIEnum(source, clazz);
    }

    @Override
    public String reverseConvert(T target, String extMsg, Class clazz) {
        return ienumToString(target);
    }
}
