package com.liguang.rcs.admin.common.enumeration;

public enum ProductTypeEnum {
    DIRECT_SELLING("0"), //直销
    CHANNEL_SELLING("1"), //渠道
    ADELBERG("3");//爱德堡
    private String code;

    private ProductTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
