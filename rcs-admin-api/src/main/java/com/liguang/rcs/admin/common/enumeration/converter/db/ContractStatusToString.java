package com.liguang.rcs.admin.common.enumeration.converter.db;

import com.liguang.rcs.admin.common.enumeration.ContractStatusEnum;
import com.liguang.rcs.admin.util.EnumUtils;

import javax.persistence.AttributeConverter;

public class ContractStatusToString implements AttributeConverter<ContractStatusEnum, String> {
    @Override
    public String convertToDatabaseColumn(ContractStatusEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public ContractStatusEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EnumUtils.findByCode(ContractStatusEnum.values(), dbData);
    }
}
