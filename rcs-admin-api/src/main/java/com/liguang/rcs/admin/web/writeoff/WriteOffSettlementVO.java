package com.liguang.rcs.admin.web.writeoff;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

import static com.liguang.rcs.admin.util.NumericUtils.minus;
import static com.liguang.rcs.admin.util.NumericUtils.plus;

@Data
@ApiModel("核销结算")
public class WriteOffSettlementVO {
    @ApiModelProperty(value = "核销结算ID", dataType = "String")
    private String settlementId;
    @ApiModelProperty(value = "合同ID", dataType = "String")
    private String contractId;
    @ApiModelProperty(value = "月份", dataType = "String")
    private String payMonth;
    @ApiModelProperty(value = "每期计划收款", dataType = "String")
    private Double planPayAmount;
    @ApiModelProperty(value = "计划期数 0-n，0-首付款；1- 第1期", dataType = "String")
    private String planNumOfPeriods;
    @ApiModelProperty(value = "实际收款金额", dataType = "String")
    private Double actualPayAmount;
    @ApiModelProperty(value = "实际到账日期", dataType = "String")
    private String actualPayDate;
    @ApiModelProperty(value = "逾期金额", dataType = "String")
    private Double overdueAmount;
    @ApiModelProperty(value = "逾期天数", dataType = "String")
    private String overdueNumOfDate;
    @ApiModelProperty(value = "累计收款金额", dataType = "String")
    private Double accumulatedPayAmount;
    @ApiModelProperty(value = "应收剩余金额", dataType = "String")
    private Double receivableReasonable;


    public static WriteOffSettlementVO buildTotal(List<WriteOffSettlementVO> settlementVOS) {
        WriteOffSettlementVO vo = new WriteOffSettlementVO();
        vo.setPayMonth("小计");
        Double actualPayTotal = null;
        Double overdueTotal = null;
        Double receiveabelTotal = null;

        for (WriteOffSettlementVO settlementVO : settlementVOS) {
            actualPayTotal = plus(actualPayTotal, settlementVO.getActualPayAmount());
            overdueTotal = plus(overdueTotal, settlementVO.getOverdueAmount());
            receiveabelTotal = settlementVO.getReceivableReasonable();
        }
        vo.setActualPayAmount(actualPayTotal);
        vo.setOverdueAmount(overdueTotal);
        vo.setReceivableReasonable(receiveabelTotal);
        if (receiveabelTotal != null && receiveabelTotal <= 0) {
            vo.setActualPayDate("已结清");
        }
        return vo;
    }
}
