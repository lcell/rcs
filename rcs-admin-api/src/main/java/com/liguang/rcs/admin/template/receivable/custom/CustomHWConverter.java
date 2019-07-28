package com.liguang.rcs.admin.template.receivable.custom;

import com.liguang.rcs.admin.common.template.ConverterStrategy;
import com.liguang.rcs.admin.template.receivable.AbstractCommonConverter;
import com.liguang.rcs.admin.web.receivable.CustomHWReceivableVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;

import static com.liguang.rcs.admin.util.NumericUtils.*;

/**
 * 客户硬件分期对象转换
 */
public class CustomHWConverter extends AbstractCommonConverter implements ConverterStrategy<WriteOffSettlementVO, CustomHWReceivableVO> {
    @Override
    public CustomHWReceivableVO convert(String key, WriteOffSettlementVO data, CustomHWReceivableVO vo) {
        if (vo == null) {
            vo = new CustomHWReceivableVO();
            vo.setContractId(data.getContractId());
            vo.setNPer(key);
        }
        vo.setActualPayment(plus(data.getActualPayAmount(), vo.getActualPayment()));
        //本期合计为 本期应收
        vo.setReceivablePayment(plus(data.getPlanPayAmount(), vo.getReceivablePayment()));
        vo.setTotal(plus(data.getPlanPayAmount(), vo.getTotal()));
        if (isNullOrZero(data.getOverdueAmount())) {//没有逾期金额，则未逾期 = 计划应付 - 实际应付
            Double unOverdueAmount = minus(data.getPlanPayAmount(), data.getActualPayAmount());
            vo.setUnOverdueAmount(plus(unOverdueAmount, vo.getUnOverdueAmount()));
        } else { //存在逾期，则需要设置到对应的逾期上
            setOverdueAmount(vo, data.getOverdueNumOfDate(), data.getOverdueAmount());
        }
       //生成逾期统计
        buildOverdueTotal(vo);
        //未逾期的计算方式 合计应收-实际支付 - 逾期金额
        vo.setUnOverdueAmount(minus(vo.getReceivablePayment() , vo.getActualPayment(), vo.getOverdueTotal()));
        return vo;
    }
}
