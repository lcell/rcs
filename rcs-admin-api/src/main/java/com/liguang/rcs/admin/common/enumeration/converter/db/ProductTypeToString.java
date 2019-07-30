package com.liguang.rcs.admin.common.enumeration.converter.db;

import com.liguang.rcs.admin.common.enumeration.ProductTypeEnum;
import com.liguang.rcs.admin.util.EnumUtils;

import javax.persistence.AttributeConverter;

public class ProductTypeToString implements AttributeConverter<ProductTypeEnum, String> {
    @Override
    public String convertToDatabaseColumn(ProductTypeEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public ProductTypeEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EnumUtils.findByCode(ProductTypeEnum.values(), dbData);
    }
}
