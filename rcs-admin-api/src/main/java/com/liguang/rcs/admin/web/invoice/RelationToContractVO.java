package com.liguang.rcs.admin.web.invoice;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.util.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel("发票关联合同参数")
@Slf4j
public class RelationToContractVO {
    @ApiModelProperty(value = "合同ID", dataType = "String", required = true)
    private String contractId;
    @ApiModelProperty(value = "发票编号", dataType = "List", required = true)
    private List<String> invoiceIds;
//    @ApiModelProperty(value = "核销类型 0-合同费 1-服务费", dataType = "String", required = true)
//    private String type;


    public void check() throws BaseException {
        if (Strings.isNullOrEmpty(getContractId())
                || getInvoiceIds() == null || getInvoiceIds().isEmpty()) {
            log.error("[Invoice] input is invalid, input:{}", this);
            throw new BaseException(ResponseCode.BAD_ARGUMENT);
        }
    }

    public Long checkAndGetContractId() {
        return Long.parseLong(contractId);
    }

    public List<Long> checkAndGetInvoiceIds() {
        return getInvoiceIds().stream().map(Long::parseLong).collect(Collectors.toList());
    }
}
