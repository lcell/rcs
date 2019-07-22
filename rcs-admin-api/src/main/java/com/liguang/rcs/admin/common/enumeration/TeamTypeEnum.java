package com.liguang.rcs.admin.common.enumeration;

public enum TeamTypeEnum {

    COMPANY("0"), DEPARTMENT("1"), TEAM("2");
    private String code;

    private TeamTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
