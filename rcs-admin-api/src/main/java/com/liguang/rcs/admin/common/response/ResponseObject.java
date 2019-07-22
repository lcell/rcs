package com.liguang.rcs.admin.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "请求响应报文体")
public class ResponseObject<T> {
    @ApiModelProperty(value = "响应码", dataType = "String")
    private int code;
    @ApiModelProperty(value = "错误消息，存在错误是有值", dataType = "String")
    private String errMsg;
    @ApiModelProperty("响应报文")
    private T body;

    public static <T> ResponseObject<T> success(T body) {
        ResponseObject<T> response = new ResponseObject<>();
        response.setBody(body);
        response.setCode(0);
        response.setErrMsg("success");
        return response;
    }
    public static <T> ResponseObject<T> success() {
        ResponseObject<T> response = new ResponseObject<>();
        response.setCode(0);
        response.setErrMsg("success");
        return response;
    }

    public static <T> ResponseObject<T> fail(int code, String errMsg) {
        ResponseObject<T> response = new ResponseObject<>();
        response.setCode(code);
        response.setErrMsg(errMsg);
        return response;
    }

    public static <T> ResponseObject<T> badArgument() {
        return fail(401, "参数不对");
    }

    public static <T> ResponseObject<T> badArgumentValue() {
        return fail(402, "参数值不对");
    }

    public static <T> ResponseObject<T> unlogin() {
        return fail(501, "请登录");
    }

    public static <T> ResponseObject<T> serious() {
        return fail(502, "系统内部错误");
    }

    public static <T> ResponseObject<T> unsupport() {
        return fail(503, "业务不支持");
    }

    public static <T> ResponseObject<T> updatedDateExpired() {
        return fail(504, "更新数据已经失效");
    }

    public static <T> ResponseObject<T> updatedDataFailed() {
        return fail(505, "更新数据失败");
    }

    public static <T> ResponseObject<T> unauthz() {
        return fail(506, "无操作权限");
    }

}
