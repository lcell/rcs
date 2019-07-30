package com.liguang.rcs.admin.web.writeoff;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

import static com.liguang.rcs.admin.util.NumericUtils.plus;

@Data
@ApiModel("服务费核销结算")
public class CommissionFeeSettlementVO {
    @ApiModelProperty(value = "核销结算ID", dataType = "String")
    private String settlementId;

    @ApiModelProperty(value = "开票日期", dataType = "String")
    private String payDate;

    @ApiModelProperty(value = "开票金额", dataType = "Double")
    private Double payAmount;

    @ApiModelProperty(value = "发票编号", dataType = "String")
    private String invoiceNo;

    @ApiModelProperty(value = "实际收款金额", dataType = "Double")
    private Double actualPayAmount;
    @ApiModelProperty(value = "实际到账日期", dataType = "String")
    private String actualPayDate;
    @ApiModelProperty(value = "逾期金额", dataType = "Double")
    private Double overdueAmount;
    @ApiModelProperty(value = "逾期天数", dataType = "String")
    private String overdueNumOfDate;
    @ApiModelProperty(value = "累计收款金额", dataType = "Double")
    private Double accumulatedPayAmount;
    @ApiModelProperty(value = "应收剩余金额", dataType = "Double")
    private Double receivableReasonable;

    public static CommissionFeeSettlementVO buildTotal(List<CommissionFeeSettlementVO> settlementVOs) {
        CommissionFeeSettlementVO vo = new CommissionFeeSettlementVO();
        vo.setPayDate("小计");
        Double planAmountTotal = null;
        Double actualPayTotal = null;
        Double receivableTotal = null;
        Double overdueTotal = null;
//        String payData = null;
        for(CommissionFeeSettlementVO settlementVO : settlementVOs) {
            planAmountTotal = plus(planAmountTotal, settlementVO.getPayAmount());
            actualPayTotal = plus(actualPayTotal, settlementVO.getActualPayAmount());
            receivableTotal = plus(receivableTotal, settlementVO.getReceivableReasonable());
            overdueTotal = plus(overdueTotal, settlementVO.getOverdueAmount());
//            if (!Strings.isNullOrEmpty(settlementVO.getActualPayDate())) {
//                payData = settlementVO.getActualPayDate();
//            }
        }
        vo.setActualPayAmount(actualPayTotal);
        vo.setOverdueAmount(overdueTotal);
        vo.setReceivableReasonable(receivableTotal);
        vo.setPayAmount(planAmountTotal);
        if (actualPayTotal != null && planAmountTotal != null && actualPayTotal >= planAmountTotal) {
            vo.setActualPayDate("已结清");
        }
        return vo;
    }

}
