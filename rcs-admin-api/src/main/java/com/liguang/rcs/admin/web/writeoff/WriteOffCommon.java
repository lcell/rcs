package com.liguang.rcs.admin.web.writeoff;

import com.liguang.rcs.admin.common.enumeration.OverdueDateEnum;

/**
 * 用于计算核销的actionPlan
 */
public interface WriteOffCommon {

    /**
     * 是否结清
     * @return 应付-实际 <=0 支付
     */
    boolean cleanUpFlag();

    /**
     * 逾期天数
     * @return 逾期天数
     */
    OverdueDateEnum overDateEnum();

}
