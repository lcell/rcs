package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("客户应收查询条件")
@Data
public class CustomQueryConditionParam {
    @ApiModelProperty(value = "客户编号", dataType = "String")
    private String customId;
    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String customName;
    @ApiModelProperty(value = "合同编号", dataType = "String")
    private String contractId;
    @ApiModelProperty(value = "起始时间", dataType = "String")
    private String beginDate;
    @ApiModelProperty(value = "结束时间", dataType = "String")
    private String endDate;
}
