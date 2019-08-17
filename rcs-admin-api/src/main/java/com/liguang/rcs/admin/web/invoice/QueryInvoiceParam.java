package com.liguang.rcs.admin.web.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("查询发票参数")
@Data
public class QueryInvoiceParam {
    @ApiModelProperty(value = "客户ID", dataType = "String")
    @NotBlank(message = "客户ID不可为空")
    private String customId;
    @ApiModelProperty(value = "合同ID", dataType = "String")
    @NotBlank(message = "合同ID不可为空")
    private String contractId;
    @ApiModelProperty(value = "生效时间 yyyy-MM-dd", dataType = "String")
    private String effectiveDate;
}
