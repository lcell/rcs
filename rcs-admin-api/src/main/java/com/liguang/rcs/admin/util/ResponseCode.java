package com.liguang.rcs.admin.util;

public enum  ResponseCode {
    USER_LOCKED_ACCOUNT("601","用户账户被锁定"),
    USER_INVALID_ACCOUNT("605","用户帐号或密码不正确"),

    USER_INVALID_PASSWORD("606","旧密码不正确"),

    BAD_ARGUMENT("401", "参数不对"),
    BAD_ARGUMENT_VALUE("402", "参数值不对"),
    NOT_EXIST("404", "数据不存在"),
    UN_LOGIN("501", "请登录"),
    SYS_INNER_ERR("502", "系统内部错误"),
    NOT_SUPPORT("503", "业务不支持"),
    UPDATED_DATA_EXPIRED("504", "更新数据已经失效"),
    UPDATED_DATA_FAILED("505", "更新数据失败"),
    NO_PERMISSION("506", "无操作权限");


    private final String code;
    private final String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
