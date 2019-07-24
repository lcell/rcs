package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.enumeration.WriteOffSourceEnum;

public class WriteOffSourceConvterter extends BaseStringToIEnumConverter<WriteOffSourceEnum> {
    @Override
    protected WriteOffSourceEnum[] values() {
        return WriteOffSourceEnum.values();
    }
}
