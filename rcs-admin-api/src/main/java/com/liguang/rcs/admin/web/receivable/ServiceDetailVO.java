package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("服务明细")
@Data
public class ServiceDetailVO extends TableCommonColumn {
    @ApiModelProperty(value = "销售名字", dataType = "String")
    private String salerName;
    @ApiModelProperty(value = "客户状态", dataType = "String")
    private String customStatus;
    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String customName;
    @ApiModelProperty(value = "应收款合计", dataType = "String")
    private String receivableTotal;
}