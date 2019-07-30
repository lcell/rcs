package com.liguang.rcs.admin.template.receivable;

import com.google.common.collect.Lists;

import java.util.List;

public abstract class AbstractKeyByMonth {
    protected String getKeyByMonth(int deltaMonth) {
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
    protected String getTotalKey(int deltaMonth) {
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

    public List<String> allRowKey() {
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
        return resultKeys;
    }
}
