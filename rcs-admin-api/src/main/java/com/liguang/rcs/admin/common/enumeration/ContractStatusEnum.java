package com.liguang.rcs.admin.common.enumeration;

/**
 * 合同状态枚举
 */
public enum ContractStatusEnum implements IEnum {
    NORMAL("0"), LEGAL("1"), THIRD_PARTY("3");
    private String code;

    private ContractStatusEnum(String code) {
        this.code = code;
    }
    @Override
    public String getCode() {
        return this.code;
    }
}
