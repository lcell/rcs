package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;

public class ContractTypeConverter extends BaseStringToIEnumConverter<ContractTypeEnum> {
    @Override
    protected ContractTypeEnum[] values() {
        return ContractTypeEnum.values();
    }
}
