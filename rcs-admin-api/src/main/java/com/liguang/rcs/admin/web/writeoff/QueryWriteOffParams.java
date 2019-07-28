package com.liguang.rcs.admin.web.writeoff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("查询核销记录条件参数")
@Data
public class QueryWriteOffParams {
    @ApiModelProperty(value = "客户ID", dataType = "String")
    private String customId;

    @ApiModelProperty(value = "合同生效时间, 格式yyyyMMdd", dataType = "String")
    private String effectDate;

    @ApiModelProperty(value = "核销结算ID", dataType = "String")
    private String settlementId;

    @ApiModelProperty(value = "核销结算类型，0-分期 1-服务费", dataType = "String")
    private String writeOffType;

    @ApiModelProperty(value = "合同ID", dataType = "String")
    private String contractId;

}
