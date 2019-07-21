package com.liguang.rcs.admin.web.writeoff;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("核销数据")
public class WriteOffRecordVO {
    @ApiModelProperty(value = "客户编号", dataType = "String")
    private String customId;

    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String customName;

    @ApiModelProperty(value = "实际到账日期", dataType = "String")
    private String actualPayDate;

    @ApiModelProperty(value = "实际收款金额", dataType = "String")
    private String actualPayAmount;

    @ApiModelProperty(value = "是否关联，0-未关联，1-已关联", dataType = "String")
    private String status;
}
