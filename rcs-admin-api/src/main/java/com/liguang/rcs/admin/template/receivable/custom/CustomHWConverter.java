package com.liguang.rcs.admin.template.receivable.custom;

import com.liguang.rcs.admin.common.template.ConverterStrategy;
import com.liguang.rcs.admin.web.receivable.CustomReceivableVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;

import static com.liguang.rcs.admin.util.NumericUtils.*;

/**
 * 客户硬件分期对象转换
 */
public class CustomHWConverter extends AbstractCommonConverter implements ConverterStrategy<WriteOffSettlementVO, CustomReceivableVO> {
    @Override
    public CustomReceivableVO convert(String key, WriteOffSettlementVO data, CustomReceivableVO vo) {
        if (vo == null) {
            vo = new CustomReceivableVO();
            vo.setContractId(data.getContractId());
            vo.setNPer(key);
        }
        vo.setActualPayment(plus(data.getActualPayAmount(), vo.getActualPayment()));
        //本期合计为 本期应收
        vo.setReceivablePayment(plus(data.getPlanPayAmount(), vo.getReceivablePayment()));
        vo.setTotal(plus(data.getPlanPayAmount(), vo.getTotal()));
        if (!isNullOrZero(data.getOverdueAmount())) {//存在逾期，则需要设置到对应的逾期上
            setOverdueAmount(vo, data.getOverdueNumOfDate(), data.getOverdueAmount());
        }
       //生成逾期统计
        buildOverdueTotal(vo);
        //未逾期的计算方式 合计应收 - 实际支付 - 逾期金额
        vo.setUnOverdueAmount(minus(vo.getReceivablePayment() , vo.getActualPayment(), vo.getOverdueTotal()));
        return vo;
    }
}
