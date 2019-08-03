package com.liguang.rcs.admin.web.writeoff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("服务费核销结算返回")
@Data
public class QueryCommissionFeeSettlementOutput {

    @ApiModelProperty("服务费结算actionPlan")
    private String actionPlan;

    @ApiModelProperty("服务费核销结算记录")
    private List<CommissionFeeSettlementVO> dataList;
}
