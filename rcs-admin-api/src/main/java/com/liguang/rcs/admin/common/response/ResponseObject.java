package com.liguang.rcs.admin.common.response;

import com.liguang.rcs.admin.util.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static com.liguang.rcs.admin.util.ResponseCode.*;

@Data
@ApiModel(value = "请求响应报文体")
public class ResponseObject<T> {
    @ApiModelProperty(value = "响应码", dataType = "String")
    private String code;
    @ApiModelProperty(value = "错误消息，存在错误是有值", dataType = "String")
    private String errMsg;
    @ApiModelProperty("响应报文")
    private T body;

    public static <T> ResponseObject<T> success(T body) {
        ResponseObject<T> response = new ResponseObject<>();
        response.setBody(body);
        response.setCode("0");
        response.setErrMsg("success");
        return response;
    }
    public static <T> ResponseObject<T> success() {
        ResponseObject<T> response = new ResponseObject<>();
        response.setCode("0");
        response.setErrMsg("success");
        return response;
    }

    public static <T> ResponseObject<T> fail(String code, String errMsg) {
        ResponseObject<T> response = new ResponseObject<>();
        response.setCode(code);
        response.setErrMsg(errMsg);
        return response;
    }


    public static <T> ResponseObject<T> fail(ResponseCode code) {
        ResponseObject<T> response = new ResponseObject<>();
        response.setCode(code.getCode());
        response.setErrMsg(code.getMsg());
        return response;
    }

    public static <T> ResponseObject<T> fail(ResponseCode code, String message) {
        ResponseObject<T> response = new ResponseObject<>();
        response.setCode(code.getCode());
        response.setErrMsg(message);
        return response;
    }

    public static <T> ResponseObject<T> badArgument() {
        return fail(BAD_ARGUMENT);
    }

    public static <T> ResponseObject<T> badArgumentValue() {
        return fail(BAD_ARGUMENT_VALUE);
    }

    public static <T> ResponseObject<T> dataNotExist() {
        return fail(NOT_EXIST);
    }
    public static <T> ResponseObject<T> unlogin() {
        return fail(UN_LOGIN);
    }

    public static <T> ResponseObject<T> serious() {
        return fail(SYS_INNER_ERR);
    }

    public static <T> ResponseObject<T> unsupport() {
        return fail(NOT_SUPPORT);
    }

    public static <T> ResponseObject<T> updatedDateExpired() {
        return fail(UPDATED_DATA_EXPIRED);
    }

    public static <T> ResponseObject<T> updatedDataFailed() {
        return fail(UPDATED_DATA_FAILED);
    }

    public static <T> ResponseObject<T> unauthz() {
        return fail(NO_PERMISSION);
    }

}
