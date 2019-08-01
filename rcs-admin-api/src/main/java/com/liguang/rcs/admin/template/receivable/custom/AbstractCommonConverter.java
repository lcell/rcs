package com.liguang.rcs.admin.template.receivable.custom;

import com.liguang.rcs.admin.common.enumeration.OverdueDateEnum;
import com.liguang.rcs.admin.web.receivable.TableCommonColumn;

import static com.liguang.rcs.admin.util.NumericUtils.*;


/**
 * 客户应收报表，分期和服务费相似的代码片段，
 */
public abstract  class AbstractCommonConverter {

    /**
     * 根据逾期的类型，将逾期的值写入到对应的类型中
     */
    protected void setOverdueAmount(TableCommonColumn vo, String overdueType, Double overdueAmount) {
        OverdueDateEnum overdueNum = OverdueDateEnum.valueOfColumn(overdueType);
        switch (overdueNum) {
            case DAY1_5:
            case DAY6_30:
            case DAY1_30:
                vo.setDay1_30(plus(vo.getDay1_30(), overdueAmount));
                break;
            case DAY31_60:
                vo.setDay31_60(plus(vo.getDay31_60(), overdueAmount));
                break;
            case DAY61_90:
                vo.setDay61_90(plus(vo.getDay61_90(), overdueAmount));
                break;
            case DAY91_180:
                vo.setDay91_180(plus(vo.getDay91_180(), overdueAmount));
                break;
            case DAY181_365:
                vo.setDay181_365(plus(vo.getDay181_365(), overdueAmount));
                break;
            case DAY_LT_365:
                vo.setDay_lt_365(plus(vo.getDay_lt_365(), overdueAmount));
                break;
            default:
                //Do Nothing
                break;
        }
    }

    /**
     * 逾期合计， 超过90天， 超过90天占比
     * @param vo
     */
    protected void buildOverdueTotal(TableCommonColumn vo) {
        vo.setDay_lt_90_total(plus(vo.getDay91_180(), vo.getDay181_365(), vo.getDay_lt_365()));
        vo.setOverdueTotal(plus( vo.getDay1_30(), vo.getDay31_60(), vo.getDay61_90(), vo.getDay_lt_90_total()));
        if (isNullOrZero(vo.getDay_lt_90_total())) {
            vo.setDay_lt_90_ratio(0d);
        } else {
            double rate = Math.floor(vo.getDay_lt_90_total() / vo.getTotal());
            vo.setDay_lt_90_ratio(formatDouble(rate, 2));
        }
    }
}
