package com.liguang.rcs.admin.web.writeoff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("增加核销记录")
public class AddWriteOffParam {
    @ApiModelProperty(value = "客户编号", dataType = "String")
    private String customId;

    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String customName;

    @ApiModelProperty(value = "实际到账日期", dataType = "String")
    private String actualPayDate;

    @ApiModelProperty(value = "实际收款金额", dataType = "String")
    private String actualPayAmount;

    @ApiModelProperty(value = "关联的合同ID", dataType = "String")
    private String contractId;

    @ApiModelProperty(value = "核销类型， 0-硬件 1-服务", dataType = "String")
    private String type;
}
