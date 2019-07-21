package com.liguang.rcs.admin.web.profile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@ApiModel("修改密码请求")
public class ChangePwdInput {
    @ApiModelProperty(value = "原始密码", dataType = "String", required = true)
    private String oldPassword;
    @ApiModelProperty(value = "新密码", dataType = "String", required = true)
    private String newPassword;

}
