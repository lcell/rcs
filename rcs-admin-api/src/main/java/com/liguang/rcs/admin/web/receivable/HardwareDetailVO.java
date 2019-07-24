package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gggg
 */
@ApiModel("硬件明细")
@Data
@EqualsAndHashCode(callSuper = true)
public class HardwareDetailVO extends TableCommonColumn {
    @ApiModelProperty(value = "销售名字", dataType = "String")
    private String salerName;
    @ApiModelProperty(value = "客户状态", dataType = "String")
    private String customStatus;
    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String customName;
    @ApiModelProperty(value = "应收款合计", dataType = "String")
    private String receivableTotal;
    @ApiModelProperty(value = "Action Plan: 0-结清 1-正常 2-催款函+利息 4-停服+法务催款函 5-第三方/诉讼", dataType = "String")
    private String actionPlan;

}
