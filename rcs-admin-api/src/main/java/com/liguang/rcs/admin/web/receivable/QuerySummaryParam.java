package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("查看应收汇总参数")
@Data
public class QuerySummaryParam {
    @ApiModelProperty(value = "产品类型", dataType = "String")
    private String productType;
    @ApiModelProperty(value = "团队", dataType = "String")
    private String teamId;
    @ApiModelProperty(value = "员工工号", dataType = "String")
    private String accountId;
    @ApiModelProperty(value = "员工姓名", dataType = "String")
    private String accountName;
    @ApiModelProperty(value = "起始时间", dataType = "String")
    private String beginDate;
    @ApiModelProperty(value = "结束时间", dataType = "String")
    private String endDate;
//    @ApiModelProperty(value = "操作者类型, 0-销售， 1-团队 2-PP业务", dataType = "String")
//    private String operatorType;
}
