package com.liguang.rcs.admin.template.receivable.summary;

import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.Constant;
import com.liguang.rcs.admin.common.template.KeyStrategy;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.template.receivable.AbstractKeyByMonth;
import com.liguang.rcs.admin.web.receivable.CustomReceivableVO;

import java.util.List;

/**
 * 汇总， 每次都需要将合同写入
 */
public class SummaryKeyStrategy extends AbstractKeyByMonth implements KeyStrategy<CustomReceivableVO> {
    private ContractEntity contract;
    @Override
    public List<String> getRowKey(CustomReceivableVO data) {
        List<String> keys = Lists.newArrayList();
        keys.add(data.getNPer());
        if (Constant.NET_TOTAL_BY_DUE_DATE.equals(data.getNPer()) && contract != null) {
            switch (contract.getStatus()) {
                case NORMAL_PART:
                    keys.add(Constant.NORMAL_PART);
                    break;
                case LEGAL_PART:
                    keys.add(Constant.LEGAL_PART);
                    break;
                case THIRD_PARTY:
                    keys.add(Constant.THIRD_PARTY);
                    break;
                default:
                    break;
            }
        }
        return keys;
    }

    @Override
    public List<String> getAllRowKey() {
        List<String> keys = allRowKey();
        keys.add(Constant.TOTAL_BY_DUE_DATE_RANGE);
        keys.add(Constant.UN_APPLIED_CASH);
        keys.add(Constant.NET_TOTAL_BY_DUE_DATE);
        keys.add(Constant.LEGAL_PART);
        keys.add(Constant.THIRD_PARTY);
        keys.add(Constant.NORMAL_PART);
        return keys;
    }

    public void setContract(ContractEntity contract) {
        this.contract = contract;
    }
}
