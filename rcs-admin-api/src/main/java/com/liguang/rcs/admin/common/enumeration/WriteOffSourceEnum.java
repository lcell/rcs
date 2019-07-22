package com.liguang.rcs.admin.common.enumeration;

public enum WriteOffSourceEnum {
    //从系统同步过来
    ADVANCE_SYS("0"),
    //手工校对
    MANUALLY_CHECK("1");

    private String code;
    private WriteOffSourceEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
