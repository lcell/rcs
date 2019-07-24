package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;

public class NumberToStringConverter extends AbstractNumberStringConverter implements TypeConverter<Number, String> {
    @Override
    public String convert(Number source, String extMsg, Class extClass) {
        return numericToString(source);
    }

    @Override
    public Number reverseConvert(String target, String extMsg, Class extClass) {
        return stringToNumeric(target, extClass);
    }
}
