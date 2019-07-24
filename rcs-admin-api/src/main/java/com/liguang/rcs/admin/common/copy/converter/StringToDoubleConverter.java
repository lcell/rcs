package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;
import com.liguang.rcs.admin.util.NumericUtils;

public class StringToDoubleConverter implements TypeConverter<String, Double> {
    @Override
    public Double convert(String source, String extMsg){
        return NumericUtils.toDouble(source);
    }
}
