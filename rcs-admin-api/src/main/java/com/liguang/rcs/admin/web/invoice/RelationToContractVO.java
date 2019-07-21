package com.liguang.rcs.admin.web.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
@ApiModel("发票关联合同参数")
public class RelationToContractVO {
    @ApiModelProperty(value = "合同ID", dataType = "String", required = true)
    private String contractId;
    @ApiModelProperty(value = "发票编号", dataType = "List", required = true)
    private List<String> invoiceIds;
    @ApiModelProperty(value = "核销类型 0-合同费 1-服务费", dataType = "String", required = true)
    private String type;
}
