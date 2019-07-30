package com.liguang.rcs.admin.template.receivable.summary;

import com.liguang.rcs.admin.common.template.ConverterStrategy;
import com.liguang.rcs.admin.web.receivable.CustomReceivableVO;
import com.liguang.rcs.admin.web.receivable.ReceivableSummaryVo;

import javax.annotation.Nullable;

public class SummaryConverter implements ConverterStrategy<CustomReceivableVO, ReceivableSummaryVo> {

    @Override
    public ReceivableSummaryVo convert(String key, CustomReceivableVO data, @Nullable ReceivableSummaryVo originData) {
        if (originData == null) {
            originData = new ReceivableSummaryVo();
            originData.setNPer(key);
        }
        originData.pulsOther(data);
        return originData;
    }

}
