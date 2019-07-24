package com.liguang.rcs.admin.util;

public class ResponseCode {
    //用户登录错误code
    public static final Integer USER_INVALID_NAME = 601;
    public static final Integer USER_INVALID_PASSWORD = 602;
    public static final Integer USER_NAME_EXIST = 602;
    public static final Integer USER_ALTER_NOT_ALLOWED = 603;
    public static final Integer USER_DELETE_NOT_ALLOWED = 604;
    public static final Integer USER_INVALID_ACCOUNT = 605;

    //用户角色错误code
    public static final Integer ROLE_NAME_EXIST = 610;
    public static final Integer ROLE_SUPER_SUPERMISSION = 611;
    public static final Integer ROLE_USER_EXIST = 612;


    //系统错误
    public static final Integer INNER_ERROR_CODE = 500;
}
