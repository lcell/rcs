package com.liguang.rcs.admin.web.writeoff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("查询分期结算的输出")
@Data
public class QuerySettlementOutput {
    @ApiModelProperty(value = "合同的actionPlan", dataType = "String")
    private String actionPlan;

    @ApiModelProperty("核销列表详细")
    private List<WriteOffSettlementVO> dataList;

}
