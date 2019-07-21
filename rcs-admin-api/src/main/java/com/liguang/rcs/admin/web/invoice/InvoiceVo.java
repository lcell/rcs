package com.liguang.rcs.admin.web.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("发票信息")
public class InvoiceVo {
    @ApiModelProperty(value = "合同编号", dataType = "String", required = true)
    @NotNull(message = "发票编号不可为空")
    private String contractId;
    @ApiModelProperty(value = "发票识别号", dataType = "String")
    private String identifierCode;
    @ApiModelProperty(value = "发票编号", dataType = "String")
    private String id;
    @ApiModelProperty(value = "客户编号", required = true, dataType = "String")
    private String customId;

    @ApiModelProperty(value = "客户名称", required = true, dataType = "String")
    private String customName;

    @ApiModelProperty(value = "发票主题", required = true, dataType = "String")
    private String subject;

    @ApiModelProperty(value = "发票金额", required = true, dataType = "String")
    private String amount;

    @ApiModelProperty(value = "开票日期", required = true, dataType = "String")
    private String billingDate;

    @ApiModelProperty(value = "首付款", dataType = "String")
    private String firstPayment;

    @ApiModelProperty(value = "余款", dataType = "String")
    private String remainsAmount;

    @ApiModelProperty(value = "期数", dataType = "String")
    private String paymentNum;

}
