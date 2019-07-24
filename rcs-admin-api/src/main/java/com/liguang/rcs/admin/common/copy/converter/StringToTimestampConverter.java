package com.liguang.rcs.admin.common.copy.converter;

import com.liguang.rcs.admin.common.copy.TypeConverter;
import com.liguang.rcs.admin.util.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;

/**
 *
 */
@Slf4j
public class StringToTimestampConverter implements TypeConverter<String, Timestamp> {
    @Override
    public Timestamp convert(String source, String format) {
        try {
            return DateUtils.toTimestamp(source, format);
        } catch (ParseException e) {
            log.warn("[Converter] source:{}, format:{}", source, format);
            log.error("[Converter] converter to Timestamp Exception:", e);
            return null;
        }
    }
}
