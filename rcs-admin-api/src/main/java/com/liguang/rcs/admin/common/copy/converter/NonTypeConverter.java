package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;

/**
 * 不存在类型转换
 */
public class NonTypeConverter<T> implements TypeConverter<T, T> {
    @Override
    public T convert(T source, String extMsg, Class clazz) {
        return source;
    }

    @Override
    public T reverseConvert(T target, String extMsg, Class clazz) {
        return target;
    }
}
