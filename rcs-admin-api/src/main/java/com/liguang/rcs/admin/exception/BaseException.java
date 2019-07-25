package com.liguang.rcs.admin.exception;

import com.liguang.rcs.admin.util.ResponseCode;

/**
 * 基础异常类
 */
public class BaseException extends Exception {
    private String code;

    public BaseException(String code) {
        this.code = code;
    }

    public BaseException(ResponseCode code) {
        this(code.getCode(), code.getMsg());
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException( String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
