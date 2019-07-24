package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("应收表通用字段")
@Data
public class TableCommonColumn {
    @ApiModelProperty(value = "期数", dataType = "String")
    private String nper;
    @ApiModelProperty(value = "未逾期金额", dataType = "String")
    private String unOverdueAmount;
    @ApiModelProperty(value = "1~30天", dataType = "String")
    private String day1_30;
    @ApiModelProperty(value = "31～60天", dataType = "String")
    private String day31_60;
    @ApiModelProperty(value = "61～90天", dataType = "String")
    private String day61_90;
    @ApiModelProperty(value = "91～180天", dataType = "String")
    private String day91_180;
    @ApiModelProperty(value = "181～365天", dataType = "String")
    private String day181_365;
    @ApiModelProperty(value = ">365天", dataType = "String")
    private String day_lt_365;
    @ApiModelProperty(value = ">90合计", dataType = "String")
    private String day_lt_90_total;
    @ApiModelProperty(value = ">90占比", dataType = "String")
    private String day_lt_90_ratio;
    @ApiModelProperty(value = "本期合计", dataType = "String")
    private String total;
//    @ApiModelProperty(value = "本期净值", dataType = "String")
//    private String netWorth;

}
