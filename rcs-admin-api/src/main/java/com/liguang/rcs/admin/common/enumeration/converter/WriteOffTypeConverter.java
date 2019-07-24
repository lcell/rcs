package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;

public class WriteOffTypeConverter extends BaseStringToIEnumConverter<WriteOffTypeEnum> {
    @Override
    protected WriteOffTypeEnum[] values() {
        return WriteOffTypeEnum.values();
    }
}
