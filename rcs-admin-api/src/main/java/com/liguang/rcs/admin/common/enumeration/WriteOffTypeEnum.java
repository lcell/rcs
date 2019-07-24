package com.liguang.rcs.admin.common.enumeration;

public enum WriteOffTypeEnum  implements IEnum {
    HARDWARE("0"), SERVICE("1");
    private String code;
    WriteOffTypeEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

}
