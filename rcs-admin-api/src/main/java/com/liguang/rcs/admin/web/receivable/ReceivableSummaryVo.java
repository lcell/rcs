package com.liguang.rcs.admin.web.receivable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ApiModel("应收汇总")
@Data
@Slf4j
public class ReceivableSummaryVo extends TableCommonColumn {

    public void pulsOther(CustomReceivableVO data) {
        super.plusOther(data);
    }
}
