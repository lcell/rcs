package com.liguang.rcs.admin.template.receivable.custom;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.Constant;
import com.liguang.rcs.admin.common.template.KeyStrategy;
import com.liguang.rcs.admin.template.receivable.AbstractKeyByMonth;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;

import java.util.Date;
import java.util.List;

/**
 * 客户分期key
 */
public class CustomHWKeyStrategy extends AbstractKeyByMonth implements KeyStrategy<WriteOffSettlementVO> {

    private final Date firstRowKey;

    public CustomHWKeyStrategy(Date firstRowKey) {
        this.firstRowKey = firstRowKey;
    }

    @Override
    public List<String> getRowKey(WriteOffSettlementVO data) {
        try {
            String payMonth = data.getPayMonth();
            int deltaMonth = Math.abs(DateUtils.dateMinusForMonth(firstRowKey, payMonth, "yyyyMM"));
            List<String> keys = Lists.newArrayList();
            keys.add(getKeyByMonth(deltaMonth));
            String totalKey = getTotalKey(deltaMonth);
            if (!Strings.isNullOrEmpty(totalKey)) {
                keys.add(totalKey);
            }
            keys.add(Constant.TOTAL_BY_DUE_DATE_RANGE);
            return keys;
        } catch (Exception ex) {
            throw new RuntimeException("Inner Err");
        }
    }



    @Override
    public List<String> getAllRowKey() {
        List<String> resultKeys = allRowKey();
        resultKeys.add(Constant.TOTAL_BY_DUE_DATE_RANGE);
        return resultKeys;
    }

    public static void main(String[] args) {
        System.out.println(new CustomHWKeyStrategy(null).getAllRowKey());
    }
}
