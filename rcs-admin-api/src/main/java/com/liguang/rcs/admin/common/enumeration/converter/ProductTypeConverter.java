package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.enumeration.ProductTypeEnum;

public class ProductTypeConverter extends BaseStringToIEnumConverter<ProductTypeEnum> {
    @Override
    protected ProductTypeEnum[] values() {
        return ProductTypeEnum.values();
    }
}
