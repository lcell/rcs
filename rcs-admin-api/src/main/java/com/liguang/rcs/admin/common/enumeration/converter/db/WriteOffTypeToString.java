package com.liguang.rcs.admin.common.enumeration.converter.db;

import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.util.EnumUtils;

import javax.persistence.AttributeConverter;

public class WriteOffTypeToString implements AttributeConverter<WriteOffTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(WriteOffTypeEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public WriteOffTypeEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EnumUtils.findByCode(WriteOffTypeEnum.values(), dbData);
    }
}
