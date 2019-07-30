package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("客户服务费应收输出")
@Data
public class CustomCommissionOutput {
    @ApiModelProperty(value = "合同ID", dataType = "String", required = true)
    private String contractId;
    @ApiModelProperty(value = "合同编号", dataType = "String", required = true)
    private String contractNo;
    @ApiModelProperty(value = "逾期手段", dataType = "String")
    private String actionPlan;
    @ApiModelProperty(value = "客户服务费应收", dataType = "List", required = true)
    private List<CustomReceivableVO> commissionReceivableVOS;
}
