package com.liguang.rcs.admin.common.copy.converter;

import java.sql.Timestamp;

public class TimestampToStringConverter extends AbstractTimestampStringConverter<Timestamp, String> {
    @Override
    public String convert(Timestamp source, String extMsg, Class extClass) {
        return timestampToString(source, extMsg);
    }

    @Override
    public Timestamp reverseConvert(String target, String extMsg, Class extClass) {
        return stringToTimestamp(target, extMsg);
    }
}
