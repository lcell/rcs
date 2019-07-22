package com.liguang.rcs.admin.common.enumeration;

/**
 * 合同状态枚举
 */
public enum ContractStatusEnum {
    NORMAL("0"), LEGAL("1"), THIRD_PARTY("3");
    private String code;

    private ContractStatusEnum(String code) {
        this.code = code;
    }
    public String getCode() {
        return this.code;
    }
}
