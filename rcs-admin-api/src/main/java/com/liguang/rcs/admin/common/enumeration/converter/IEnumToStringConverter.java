package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.enumeration.IEnum;

public class IEnumToStringConverter<T extends IEnum> extends AbstractIEnmuStringConverter<T, String> {

    @Override
    public String convert(T source, String extMsg, Class extClass) {
        return ienumToString(source);
    }

    @Override
    public T reverseConvert(String target, String extMsg, Class extClass) {
        return stringToIEnum(target, extClass);
    }
}
