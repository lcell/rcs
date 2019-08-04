package com.liguang.rcs.admin.web.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("登录信息")
public class AuthLoginInput {
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "用户密码")
    private String password;
}
