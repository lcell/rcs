package com.liguang.rcs.admin.common.enumeration;

public enum ContractTypeEnum  implements IEnum {
    HARDWARE("0"), SERVICE("1");

    private final String code;
    ContractTypeEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
