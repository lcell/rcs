package com.liguang.rcs.admin.template.receivable.custom;

import com.liguang.rcs.admin.common.template.ConverterStrategy;
import com.liguang.rcs.admin.web.receivable.CustomReceivableVO;
import com.liguang.rcs.admin.web.writeoff.CommissionFeeSettlementVO;

import static com.liguang.rcs.admin.util.NumericUtils.*;

/**
 * 服务费应收统计报表转换
 */
public class CustomServiceConverter extends AbstractCommonConverter implements ConverterStrategy<CommissionFeeSettlementVO, CustomReceivableVO> {
    @Override
    public CustomReceivableVO convert(String key, CommissionFeeSettlementVO data, CustomReceivableVO vo) {
        if (vo == null) {
            vo = new CustomReceivableVO();
            vo.setNPer(key);
        }
        //本期合计为 本期应收
        vo.setReceivablePayment(plus(data.getPayAmount(), vo.getReceivablePayment()));

        vo.setActualPayment(plus(data.getActualPayAmount(), vo.getActualPayment()));
        vo.setTotal(plus(data.getPayAmount(), vo.getTotal()));
        if (!isNullOrZero(data.getOverdueAmount())) { //存在逾期， 将逾期写入到对应的逾期天数条目中
            setOverdueAmount(vo, data.getOverdueNumOfDate(), data.getOverdueAmount());
        }
        //设置逾期统计
        buildOverdueTotal(vo);
        //未逾期的计算方式 合计应收-实际支付 - 逾期金额
        vo.setUnOverdueAmount(minus(vo.getReceivablePayment() , vo.getActualPayment(), vo.getOverdueTotal()));
        return vo;
    }
}
