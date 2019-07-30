package com.liguang.rcs.admin.template.receivable.detail;

import com.liguang.rcs.admin.common.enumeration.ActionPlanEnum;
import com.liguang.rcs.admin.common.template.ConverterStrategy;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.web.receivable.CustomReceivableVO;
import com.liguang.rcs.admin.web.receivable.ReceivableDetailVo;

import javax.annotation.Nullable;

public class DetailConverter implements ConverterStrategy<CustomReceivableVO, ReceivableDetailVo> {
    private ContractEntity contract;
    private ActionPlanEnum actionPlan;
    @Override
    public ReceivableDetailVo convert(String key, CustomReceivableVO data, @Nullable ReceivableDetailVo originData) {
        if (originData == null) {
            originData = new ReceivableDetailVo();
            originData.setCustomName(contract.getCustomName());
            originData.setCustomStatus(contract.getStatus().getCode());
            originData.setSalerName(contract.getSalesName());
            originData.setNPer(key);
            originData.setActionPlan(actionPlan == null ? null : actionPlan.getCode());
        }
        originData.plusOther(data);
        return originData;
    }

    public void setContract(ContractEntity contract) {
        this.contract = contract;
    }

    public void setActionPlan(ActionPlanEnum actionPlan) {
        this.actionPlan = actionPlan;
    }
}
