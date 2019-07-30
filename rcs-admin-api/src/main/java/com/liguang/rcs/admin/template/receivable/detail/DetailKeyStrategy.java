package com.liguang.rcs.admin.template.receivable.detail;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.liguang.rcs.admin.common.Constant;
import com.liguang.rcs.admin.common.template.KeyStrategy;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.web.receivable.CustomReceivableVO;
import com.liguang.rcs.admin.web.receivable.TableCommonColumn;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DetailKeyStrategy implements KeyStrategy<CustomReceivableVO> {
    private final Set<String> keyList;
    private ContractEntity contract;

    public DetailKeyStrategy() {
        this.keyList = Sets.newLinkedHashSet();
    }

    @Override
    public List<String> getRowKey(CustomReceivableVO data) {
        List<String> keys = Lists.newArrayList();
        if (Constant.NET_TOTAL_BY_DUE_DATE.equals(data.getNPer())
                || Constant.UN_APPLIED_CASH.equals(data.getNPer())) { //"真实应收款合计"
            keys.add(data.getNPer());
        } else if(Constant.TOTAL_BY_DUE_DATE_RANGE.equals(data.getNPer())) {
            keys.add(Constant.TOTAL_BY_DUE_DATE_RANGE);
            keys.add(contract.getId().toString());
            keyList.add(contract.getId().toString());
        }
        return keys;
    }

    @Override
    public List<String> getAllRowKey() {
        List<String> keys = Lists.newArrayList(keyList);
        Collections.sort(keys);
        keys.add(Constant.TOTAL_BY_DUE_DATE_RANGE);
        keys.add(Constant.UN_APPLIED_CASH);
        keys.add(Constant.NET_TOTAL_BY_DUE_DATE);

        return keys;
    }

    public void setContract(ContractEntity contract) {
        this.contract = contract;
    }
}
