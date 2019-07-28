package com.liguang.rcs.admin.common.enumeration;

import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.web.receivable.TableCommonColumn;

/**
 * 1-30天逾期为正常
 * 31-61天逾期
 */
public enum ActionPlanEnum {
    NORMAL("正常"),
    PROMPT("催款函+利息") ,
    STOP_SERVICE("停服+法务催款函"),
    PROSECUTE("第三方/诉讼")    ;
    private final  String code;

    ActionPlanEnum(String code) {
        this.code = code;
    }

    public static ActionPlanEnum getActionPlan(TableCommonColumn column) {
        if (!NumericUtils.isNullOrZero(column.getDay_lt_90_total())) {
            return PROSECUTE;
        }
        if (!NumericUtils.isNullOrZero(column.getDay61_90())) {
            return STOP_SERVICE;
        }
        if (!NumericUtils.isNullOrZero(column.getDay31_60())) {
            return PROMPT;
        }
        return NORMAL;
    }

    public String getCode() {
        return code;
    }
}
