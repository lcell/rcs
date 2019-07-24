package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;
import com.liguang.rcs.admin.common.enumeration.IEnum;

public class IEnumToStringConverter<T extends IEnum> implements TypeConverter<T, String> {
    @Override
    public String convert(T source, String extMsg) {
        return source.getCode();
    }
}
