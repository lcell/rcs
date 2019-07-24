package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;
import com.liguang.rcs.admin.util.NumericUtils;

public class StringToIntegerConverter implements TypeConverter<String, Integer> {
    @Override
    public Integer convert(String source, String extMsg) {
        return NumericUtils.toInteger(source);
    }
}
