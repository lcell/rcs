package com.liguang.rcs.admin.web.receivable;

import com.liguang.rcs.admin.common.copy.CopyProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static com.liguang.rcs.admin.util.NumericUtils.minus;
import static com.liguang.rcs.admin.util.NumericUtils.plus;

@ApiModel("应收表通用字段")
@Data
public class TableCommonColumn {
    @ApiModelProperty(value = "期数", dataType = "String")
    private String nPer;
    @ApiModelProperty(value = "未逾期金额", dataType = "Double")
    @CopyProperty
    private Double unOverdueAmount;
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
    @ApiModelProperty(value = "本期合计, 实际支付 + 逾期 + 未逾期", dataType = "Double")
    @CopyProperty
    private Double total;

    protected void minusOther(TableCommonColumn other) {
        unOverdueAmount = minus(unOverdueAmount, other.unOverdueAmount);
        day1_30 = minus(day1_30, other.day1_30);
        day31_60 = minus(day31_60, other.day31_60);
        day61_90 = minus(day61_90, other.day61_90);
        day91_180 = minus(day91_180, other.day91_180);
        day181_365 = minus(day181_365, other.day181_365);
        day_lt_365 = minus(day_lt_365, other.day_lt_365);
        day_lt_90_total = minus(day_lt_90_total, other.day_lt_90_total);
        day_lt_90_ratio = minus(day_lt_90_ratio, other.day_lt_90_ratio);
        overdueTotal = minus(overdueTotal, other.overdueTotal);
        total = minus(total, other.total);

    }

    protected void plusOther(TableCommonColumn other) {
        unOverdueAmount = plus(unOverdueAmount, other.unOverdueAmount);
        day1_30 = plus(day1_30, other.day1_30);
        day31_60 = plus(day31_60, other.day31_60);
        day61_90 = plus(day61_90, other.day61_90);
        day91_180 = plus(day91_180, other.day91_180);
        day181_365 = plus(day181_365, other.day181_365);
        day_lt_365 = plus(day_lt_365, other.day_lt_365);
        day_lt_90_total = plus(day_lt_90_total, other.day_lt_90_total);
        overdueTotal = plus(overdueTotal, other.overdueTotal);
        day_lt_90_ratio = plus(day_lt_90_ratio, other.day_lt_90_ratio);
        if (day_lt_90_ratio != null) {
            day_lt_90_ratio /= 2;
        }
        total = plus(total, other.total);
    }
}
