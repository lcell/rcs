package com.liguang.rcs.admin.template.receivable.custom;

import com.liguang.rcs.admin.common.template.ConverterStrategy;
import com.liguang.rcs.admin.template.receivable.AbstractCommonConverter;
import com.liguang.rcs.admin.web.receivable.CustomCommissionReceivableVO;
import com.liguang.rcs.admin.web.writeoff.CommissionFeeSettlementVO;

import static com.liguang.rcs.admin.util.NumericUtils.*;

/**
 * 服务费应收统计报表转换
 */
public class CustomServiceConverter extends AbstractCommonConverter implements ConverterStrategy<CommissionFeeSettlementVO, CustomCommissionReceivableVO> {
    @Override
    public CustomCommissionReceivableVO convert(String key, CommissionFeeSettlementVO data, CustomCommissionReceivableVO vo) {
        if (vo == null) {
            vo = new CustomCommissionReceivableVO();
            vo.setNPer(key);
        }
        //本期合计为 本期应收
        vo.setTotal(plus(data.getPayAmount(), vo.getTotal()));
        if (isNullOrZero(data.getOverdueAmount())) { //如果逾期为0，则未逾期 = 计划应付-实际应付
            Double unOverdueAmount = minus(data.getPayAmount(), data.getActualPayAmount());
            vo.setUnOverdueAmount(plus(unOverdueAmount, vo.getUnOverdueAmount()));
        } else { //存在逾期， 将逾期写入到对应的逾期天数条目中
            setOverdueAmount(vo, data.getOverdueNumOfDate(), data.getOverdueAmount());
        }
        //设置逾期统计
        buildOverdueTotal(vo);
        return vo;
    }
}
