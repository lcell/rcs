package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("客户应收硬件分期")
@Data
public class CustomHWReceivableVO extends TableCommonColumn {
    @ApiModelProperty(value = "应收款合计",dataType = "String")
    private String receivablePaymentTotal;
    @ApiModelProperty(value = "实际到账金额",dataType = "String")
    private String actualPayment;
}
