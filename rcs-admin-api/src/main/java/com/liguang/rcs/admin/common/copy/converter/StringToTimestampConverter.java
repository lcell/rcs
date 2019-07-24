package com.liguang.rcs.admin.common.copy.converter;

import java.sql.Timestamp;

public class StringToTimestampConverter extends AbstractTimestampStringConverter<String, Timestamp> {
    @Override
    public Timestamp convert(String source, String extMsg, Class extClass) {
        return stringToTimestamp(source, extMsg);
    }

    @Override
    public String reverseConvert(Timestamp target, String extMsg, Class extClass) {
        return timestampToString(target, extMsg);
    }
}
