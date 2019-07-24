package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;
import com.liguang.rcs.admin.util.NumericUtils;

public class StringToLongConverter implements TypeConverter<String, Long> {
    @Override
    public Long convert(String source, String extMsg) {
        return NumericUtils.toLong(source);
    }
}
