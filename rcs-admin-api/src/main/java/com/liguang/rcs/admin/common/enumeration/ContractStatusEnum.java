package com.liguang.rcs.admin.common.enumeration;

/**
 * 合同状态枚举
 */
public enum ContractStatusEnum implements IEnum {
    NORMAL_PART("0"), LEGAL_PART("1"), THIRD_PARTY("2");
    private String code;

     ContractStatusEnum(String code) {
        this.code = code;
    }
    @Override
    public String getCode() {
        return this.code;
    }
}
