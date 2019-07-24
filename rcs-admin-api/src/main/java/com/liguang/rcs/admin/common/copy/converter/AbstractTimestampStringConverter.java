package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;
import com.liguang.rcs.admin.common.copy.exception.ConvertFailException;
import com.liguang.rcs.admin.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;

@Slf4j
public abstract class AbstractTimestampStringConverter<S, T> implements TypeConverter<S, T> {
    protected Timestamp stringToTimestamp(String source, String format) {
        try {
            return DateUtils.toTimestamp(source, format);
        } catch (ParseException e) {
            log.warn("timestamp convert fail, source:{}, format:{}", source, format);
            throw new ConvertFailException();
        }
    }

    protected String timestampToString(Timestamp source, String format) {
        return DateUtils.toString(source, format);
    }
}
