package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.exception.NotSupportConverterException;
import com.liguang.rcs.admin.util.NumericUtils;

public abstract class AbstractNumberStringConverter {

    protected String numericToString(Object numeric) {
        return numeric.toString();
    }

    protected Number stringToNumeric(String source, Class type) {
        if (type.isAssignableFrom(Integer.class)) {
            return NumericUtils.toInteger(source);
        }
        if (type.isAssignableFrom(Long.class)) {
            return NumericUtils.toLong(source);
        }
        if (type.isAssignableFrom(Double.class)) {
            return NumericUtils.toDouble(source);
        }
        throw new NotSupportConverterException();
    }
}
