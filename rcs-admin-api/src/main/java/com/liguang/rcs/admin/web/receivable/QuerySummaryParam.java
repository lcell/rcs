package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("查看应收汇总参数")
@Data
public class QuerySummaryParam {
    @ApiModelProperty(value = "产品类型 0-直销 1-渠道 2-爱德堡", dataType = "String")
    private String productType;
    @ApiModelProperty(value = "团队", dataType = "String")
    private String teamId;
    @ApiModelProperty(value = "销售经理名称", dataType = "String")
    private String salesName;
    @ApiModelProperty(value = "起始时间, 格式 yyyy-MM-dd", dataType = "String")
    private String beginDate;
    @ApiModelProperty(value = "合同状态 0-normal 1-legal 2-3rd party", dataType = "String")
    private String contractStatus;
}
