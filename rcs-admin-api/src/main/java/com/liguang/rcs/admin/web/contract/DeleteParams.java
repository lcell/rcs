package com.liguang.rcs.admin.web.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("删除合同参数")
public class DeleteParams {
    @ApiModelProperty(value = "需要删除的合同ID集合", dataType = "List<String>")
    private List<String> contractIds;
}
