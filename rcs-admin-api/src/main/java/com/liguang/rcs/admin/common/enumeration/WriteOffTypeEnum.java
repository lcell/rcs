package com.liguang.rcs.admin.common.enumeration;

public enum WriteOffTypeEnum {
    HARDWARE("0"), SEVICE("1");
    private String code;
    private WriteOffTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
