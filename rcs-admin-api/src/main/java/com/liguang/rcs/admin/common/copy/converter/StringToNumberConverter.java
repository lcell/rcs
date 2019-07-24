package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;

public class StringToNumberConverter extends AbstractNumberStringConverter implements TypeConverter<String, Number> {
    @Override
    public Number convert(String source, String extMsg, Class extClass) {
        return stringToNumeric(source, extClass);
    }

    @Override
    public String reverseConvert(Number target, String extMsg, Class extClass) {
        return numericToString(target);
    }
}
