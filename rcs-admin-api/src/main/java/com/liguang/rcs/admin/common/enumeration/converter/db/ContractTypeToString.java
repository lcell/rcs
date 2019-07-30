package com.liguang.rcs.admin.common.enumeration.converter.db;

import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;
import com.liguang.rcs.admin.util.EnumUtils;

import javax.persistence.AttributeConverter;

public class ContractTypeToString implements AttributeConverter<ContractTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(ContractTypeEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public ContractTypeEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EnumUtils.findByCode(ContractTypeEnum.values(), dbData);
    }
}
