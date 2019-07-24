package com.liguang.rcs.admin.web.invoice;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("查询发票参数")
@Data
public class QueryInvoiceParam {
    private String invoiceId;
    private String customId;
    private String contractId;
    private String effectDate;
}
