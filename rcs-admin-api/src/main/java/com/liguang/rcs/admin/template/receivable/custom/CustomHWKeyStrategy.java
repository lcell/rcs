package com.liguang.rcs.admin.template.receivable.custom;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.template.KeyStrategy;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;

import java.util.Date;
import java.util.List;

/**
 * 客户分期key
 */
public class CustomHWKeyStrategy implements KeyStrategy<WriteOffSettlementVO> {
    private static final String TOTAL_BY_DUE_DATE_RANGE = "Total by Due Date Range";

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
            keys.add(TOTAL_BY_DUE_DATE_RANGE);
            return keys;
        } catch (Exception ex) {
            throw new RuntimeException("Inner Err");
        }
    }

    private String getKeyByMonth(int deltaMonth) {
        //List<String> keys = Lists.newArrayList();
        if (deltaMonth == 0) {
            return "0day";
        } else if (deltaMonth == 12) {
            return "331-365days";
        } else if (deltaMonth == 13) {
            return "366-390days";
        }
        return new StringBuffer(String.valueOf((deltaMonth - 1) * 30 + 1))
                .append("-").append(String.valueOf(deltaMonth * 30))
                .append("days").toString();
    }

    //返回统计的key
    private String getTotalKey(int deltaMonth) {
        if (deltaMonth < 2) {
            return "0-30days";
        } else if (deltaMonth > 3 && deltaMonth < 7) {
            return "91-180days";
        } else if (deltaMonth > 6 && deltaMonth < 13) {
            return "181-365days";
        } else if (deltaMonth > 12) {
            return ">365days";
        }
        return null;
    }

    @Override
    public List<String> getAllRowKey() {
        List<String> resultKeys = Lists.newArrayList();
        String preTotalRow = null;
        for (int i = 0; i < 25; i++) {
            String totalKey = getTotalKey(i);
            if (preTotalRow != null && !preTotalRow.equals(totalKey)) {
                resultKeys.add(preTotalRow);
            }
            preTotalRow = totalKey;
            resultKeys.add(getKeyByMonth(i));
        }
        if (preTotalRow != null) {
            resultKeys.add(preTotalRow);
        }
        resultKeys.add(TOTAL_BY_DUE_DATE_RANGE);
        return resultKeys;
    }

    public static void main(String[] args) {
        System.out.println(new CustomHWKeyStrategy(null).getAllRowKey());
    }
}
