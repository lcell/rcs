package com.liguang.rcs.admin.web.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("账户信息")
public class Account {
    @ApiModelProperty(value = "账户编号", dataType = "String")
    private String id;

    private String name;

    private String department;

    private String district;

    private String team;

    private String position;

    private String telNum;

    private String email;

}
