package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;

public class ToStringConverter<T> implements TypeConverter<T, String> {
    @Override
    public String convert(T source, String extMsg) {
        return source == null ? null : source.toString();
    }
}
