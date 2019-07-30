package com.liguang.rcs.admin.common.enumeration;

public enum ProductTypeEnum  implements IEnum  {
    DIRECT_SELLING("0"), //直销
    CHANNEL_SELLING("1"), //渠道
    ADELBERG("2");//爱德堡
    private String code;

    ProductTypeEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
