package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModelProperty;

//@ApiModel("客户应收查询条件")
//@Data
public class CustomQueryConditionParam {

    @ApiModelProperty(value = "客户编号", dataType = "String")
    private String customNo;

    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String customName;

    @ApiModelProperty(value = "合同编号", dataType = "String")
    private String contractNo;

    @ApiModelProperty(value = "销售经理名称", dataType = "String")
    private String salesName;

    @ApiModelProperty(value = "产品类型  0-直销 1-渠道 2-爱德堡", dataType = "String")
    private String productType;

    @ApiModelProperty(value = "合同类型 0-硬件，1-服务", dataType = "String")
    private String contractType;

    @ApiModelProperty(value = "起始时间 格式yyyy-MM-dd", dataType = "String")
    private String beginDate;
}
