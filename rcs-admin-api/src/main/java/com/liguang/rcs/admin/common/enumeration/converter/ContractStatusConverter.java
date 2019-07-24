package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.enumeration.ContractStatusEnum;

public class ContractStatusConverter extends BaseStringToIEnumConverter<ContractStatusEnum> {
    @Override
    protected ContractStatusEnum[] values() {
        return ContractStatusEnum.values();
    }
}
