package com.liguang.rcs.admin.common.enumeration.converter.db;

import com.liguang.rcs.admin.common.enumeration.TeamTypeEnum;
import com.liguang.rcs.admin.util.EnumUtils;

import javax.persistence.AttributeConverter;

public class TeamTypeToString implements AttributeConverter<TeamTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(TeamTypeEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public TeamTypeEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EnumUtils.findByCode(TeamTypeEnum.values(), dbData);
    }
}
