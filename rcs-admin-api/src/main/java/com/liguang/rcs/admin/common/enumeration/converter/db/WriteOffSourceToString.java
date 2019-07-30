package com.liguang.rcs.admin.common.enumeration.converter.db;

import com.liguang.rcs.admin.common.enumeration.WriteOffSourceEnum;
import com.liguang.rcs.admin.util.EnumUtils;

import javax.persistence.AttributeConverter;

public class WriteOffSourceToString implements AttributeConverter<WriteOffSourceEnum, String> {
    @Override
    public String convertToDatabaseColumn(WriteOffSourceEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public WriteOffSourceEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EnumUtils.findByCode(WriteOffSourceEnum.values(), dbData);
    }
}
