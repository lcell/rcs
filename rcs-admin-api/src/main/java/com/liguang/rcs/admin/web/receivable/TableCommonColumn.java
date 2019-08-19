package com.liguang.rcs.admin.web.receivable;

import com.liguang.rcs.admin.common.copy.CopyProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static com.liguang.rcs.admin.util.NumericUtils.*;

@ApiModel("应收表通用字段")
@Data
public class TableCommonColumn  implements Cloneable {
    @ApiModelProperty(value = "期数", dataType = "String")
    private String nPer;
    @ApiModelProperty(value = "未逾期金额", dataType = "Double")
    @CopyProperty
    private Double unOverdueAmount;
    @ApiModelProperty(value = "1~5天", dataType = "Double")
    @CopyProperty
    private Double day1_5;
    @ApiModelProperty(value = "6～30天", dataType = "Double")
    @CopyProperty
    private Double day6_30;

    @ApiModelProperty(value = "1~30天", dataType = "Double")
    @CopyProperty
    private Double day1_30;
    @ApiModelProperty(value = "31～60天", dataType = "Double")
    @CopyProperty
    private Double day31_60;
    @ApiModelProperty(value = "61～90天", dataType = "Double")
    @CopyProperty
    private Double day61_90;
    @ApiModelProperty(value = "91～180天", dataType = "Double")
    @CopyProperty
    private Double day91_180;
    @ApiModelProperty(value = "181～365天", dataType = "Double")
    @CopyProperty
    private Double day181_365;
    @ApiModelProperty(value = ">365天", dataType = "Double")
    @CopyProperty
    private Double day_lt_365;
    @ApiModelProperty(value = ">90合计", dataType = "Double")
    @CopyProperty
    private Double day_lt_90_total;
    @ApiModelProperty(value = ">90占比", dataType = "Double")
    @CopyProperty
    private Double day_lt_90_ratio;
    @ApiModelProperty(value = "逾期总额", dataType = "Double")
    @CopyProperty
    private Double overdueTotal;
    @ApiModelProperty(value = "本期总额, 实际支付 + 逾期 + 未逾期", dataType = "Double")
    @CopyProperty
    private Double total;

    protected void minusOther(TableCommonColumn other) {
        unOverdueAmount = minus(unOverdueAmount, other.unOverdueAmount);
        day1_5 = minus(day1_5, other.day1_5);
        day6_30 = minus(day6_30, other.day6_30);
        day1_30 = minus(day1_30, other.day1_30);
        day31_60 = minus(day31_60, other.day31_60);
        day61_90 = minus(day61_90, other.day61_90);
        day91_180 = minus(day91_180, other.day91_180);
        day181_365 = minus(day181_365, other.day181_365);
        day_lt_365 = minus(day_lt_365, other.day_lt_365);
        day_lt_90_total = minus(day_lt_90_total, other.day_lt_90_total);
        overdueTotal = minus(overdueTotal, other.overdueTotal);
        total = minus(total, other.total);
        day_lt_90_ratio = div(day_lt_90_total, total);

    }

    protected void plusOther(TableCommonColumn other) {
        unOverdueAmount = plus(unOverdueAmount, other.unOverdueAmount);
        day1_5 = minus(day1_5, other.day1_5);
        day6_30 = minus(day6_30, other.day6_30);
        day1_30 = plus(day1_30, other.day1_30);
        day31_60 = plus(day31_60, other.day31_60);
        day61_90 = plus(day61_90, other.day61_90);
        day91_180 = plus(day91_180, other.day91_180);
        day181_365 = plus(day181_365, other.day181_365);
        day_lt_365 = plus(day_lt_365, other.day_lt_365);
        day_lt_90_total = plus(day_lt_90_total, other.day_lt_90_total);
        overdueTotal = plus(overdueTotal, other.overdueTotal);
        total = plus(total, other.total);
        day_lt_90_ratio = div(day_lt_90_total, total);
    }
}
