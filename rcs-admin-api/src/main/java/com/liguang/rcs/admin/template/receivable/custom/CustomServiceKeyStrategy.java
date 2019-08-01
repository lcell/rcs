package com.liguang.rcs.admin.template.receivable.custom;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.Constant;
import com.liguang.rcs.admin.common.template.KeyStrategy;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.web.writeoff.CommissionFeeSettlementVO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class CustomServiceKeyStrategy implements KeyStrategy<CommissionFeeSettlementVO> {
    private final String firstDate;

    public CustomServiceKeyStrategy(String firstDate) {
        this.firstDate = firstDate;
    }

    @Override
    public List<String> getRowKey(CommissionFeeSettlementVO data) {
        try {
            //计算出相差的天数-30, 此后开始计算逾期
            long deltaDate = DateUtils.dateMinus( data.getPayDate(), firstDate, "yyyyMMdd") - 30;
            List<String> keys = Lists.newArrayList();
            keys.add(getKey(deltaDate));
            String totalKey = getTotalKey(deltaDate);
            if (!Strings.isNullOrEmpty(totalKey)) {
                keys.add(totalKey);
            }
            keys.add(Constant.TOTAL_BY_DUE_DATE_RANGE);
            return keys;
        } catch (Exception ex) {
            log.error("[Converter] Get Key fail, Exception:{}", ex);
            throw new RuntimeException("Inner Err");
        }
    }

    private String getKey(long deltaDay) {
        if (deltaDay <= 0) {
            return "0day";
        } else if(deltaDay >= 331 && deltaDay <= 365) {
            return "331-365days";
        } else if(deltaDay >= 366 && deltaDay <= 390) {
            return  "366-390days";
        }
        int deltaMonth = (int) Math.ceil(deltaDay / 30.0);
        return new StringBuffer(String.valueOf((deltaMonth - 1) * 30 + 1))
                .append("-").append(String.valueOf(deltaMonth * 30))
                .append("days").toString();
    }
    private String getTotalKey(long deltaDay) {
        if (deltaDay <= 30) {
            return "0-30days";
        }
        if (deltaDay >= 91 && deltaDay <= 180) {
            return "91-180days";
        }
        if (deltaDay >= 181 && deltaDay <= 365) {
            return "181-365days";
        }
        if (deltaDay > 365) {
            return ">365days";
        }
        return null;
    }

    @Override
    public List<String> getAllRowKey() {
        List<String> resultKeys = Lists.newArrayList();
        String preTotalRow = null;
        for (int i = 0; i < 25; i++) {
            int deltaDay = i * 30;
            String totalKey = getTotalKey(deltaDay);
            if (preTotalRow != null && !preTotalRow.equals(totalKey)) {
                resultKeys.add(preTotalRow);
            }
            preTotalRow = totalKey;
            resultKeys.add(getKey(deltaDay));
        }
        if (preTotalRow != null) {
            resultKeys.add(preTotalRow);
        }
        resultKeys.add(Constant.TOTAL_BY_DUE_DATE_RANGE);
        return resultKeys;
    }
    /*
    测试一下
     */
    public static void main(String[] args) {
        CustomServiceKeyStrategy keyStrategy = new CustomServiceKeyStrategy(null);
        System.out.println(keyStrategy.getAllRowKey());

        System.out.println(keyStrategy.getKey(0).equals("0day"));
        System.out.println(keyStrategy.getKey(-1).equals("0day"));
        System.out.println(keyStrategy.getKey(10).equals("1-30days"));
        System.out.println(keyStrategy.getKey(1).equals("1-30days"));
        System.out.println(keyStrategy.getKey(30).equals("1-30days"));

        System.out.println(keyStrategy.getKey(31).equals("31-60days"));
        System.out.println(keyStrategy.getKey(35).equals("31-60days"));
        System.out.println(keyStrategy.getKey(60).equals("31-60days"));


        System.out.println(keyStrategy.getKey(61).equals("61-90days"));
        System.out.println(keyStrategy.getKey(65).equals("61-90days"));
        System.out.println(keyStrategy.getKey(90).equals("61-90days"));

    }
}
