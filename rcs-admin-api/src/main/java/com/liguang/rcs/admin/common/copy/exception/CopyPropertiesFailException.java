package com.liguang.rcs.admin.common.copy.exception;

public class CopyPropertiesFailException extends Exception {
    private static final String ERROR_CODE = "Inner Err";
    public CopyPropertiesFailException() {
        super(ERROR_CODE);
    }

    public CopyPropertiesFailException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
