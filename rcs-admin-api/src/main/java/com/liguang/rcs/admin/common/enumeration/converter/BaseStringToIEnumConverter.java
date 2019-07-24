package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;
import com.liguang.rcs.admin.common.enumeration.IEnum;

public abstract class BaseStringToIEnumConverter<T extends IEnum> implements TypeConverter<String, T> {
    @Override
    public T convert(String source, String extMsg) {
        T [] values = values();
        for(T value : values) {
            if (value.getCode().equals(source)) {
                return value;
            }
        }
        return null;
    }

    protected abstract T[] values();
}
