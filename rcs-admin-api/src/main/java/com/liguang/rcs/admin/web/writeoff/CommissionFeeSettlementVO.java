package com.liguang.rcs.admin.web.writeoff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("服务费核销结算")
public class CommissionFeeSettlementVO {

    @ApiModelProperty(value = "核销结算记录编号", dataType = "String")
    private String id;

    @ApiModelProperty(value = "开票日期", dataType = "String")
    private String payDate;

    @ApiModelProperty(value = "开票金额", dataType = "String")
    private String payAmount;

    @ApiModelProperty(value = "发票编号", dataType = "String")
    private String invoiceId;

    @ApiModelProperty(value = "实际收款金额", dataType = "String")
    private String actualPayAmount;
    @ApiModelProperty(value = "实际到账日期", dataType = "String")
    private String actualPayDate;
    @ApiModelProperty(value = "逾期金额", dataType = "String")
    private String overdueAmount;
    @ApiModelProperty(value = "逾期天数", dataType = "String")
    private String overdueNumOfDate;
    @ApiModelProperty(value = "累计收款金额", dataType = "String")
    private String accumulatedPayAmount;
    @ApiModelProperty(value = "应收剩余金额", dataType = "String")
    private String receivableReasonable;


}
