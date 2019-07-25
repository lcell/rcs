package com.liguang.rcs.admin.web.writeoff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("核销关联合同处理参数")
@Data
public class WriteOffRelateParams {
    @ApiModelProperty(value = "合同ID", dataType = "String", required = true)
    @NotNull(message = "合同ID不可为空")
    private String contractId;
    @ApiModelProperty(value = "核销记录ID集合", dataType = "List", required = true)
    @NotNull(message = "核销记录ID不可为空")
    private List<String> writeOffIds;
    @ApiModelProperty(value = "核销类型, 0-硬件 1-服务", dataType = "String", required = true)
    private String writeOffType;
}
