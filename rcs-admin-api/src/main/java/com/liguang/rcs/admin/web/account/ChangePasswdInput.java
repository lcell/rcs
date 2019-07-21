package com.liguang.rcs.admin.web.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@ToString
@ApiModel("修改密码参数")
public class ChangePasswdInput {

    @ApiModelProperty(value = "原始密码", dataType = "String", required = true)
    @NotBlank(message = "原始密码不可为空")
    private String passwd;

    @ApiModelProperty(value = "新密码", dataType = "String", required = true)
    @NotBlank(message = "新密码不能为空")
    private String newPasswd;
}
