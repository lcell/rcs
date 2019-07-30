package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.liguang.rcs.admin.util.NumericUtils.plus;

@ApiModel("应收明细")
@Data
@Slf4j
public class ReceivableDetailVo extends TableCommonColumn {
    @ApiModelProperty(value = "销售名字", dataType = "String")
    private String salerName;
    @ApiModelProperty(value = "客户状态", dataType = "String")
    private String customStatus;
    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String customName;
    @ApiModelProperty(value = "应收款合计", dataType = "String")
    private Double receivableTotal;
    @ApiModelProperty(value = "Action Plan: 0-结清 1-正常 2-催款函+利息 4-停服+法务催款函 5-第三方/诉讼", dataType = "String")
    private String actionPlan;

    public void plusOther(CustomReceivableVO other) {
        super.plusOther(other);
        this.receivableTotal = plus(receivableTotal, other.getReceivablePayment());
    }
}
