package com.liguang.rcs.admin.web.invoice;

import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import com.liguang.rcs.admin.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("发票信息")
public class InvoiceVO {
    @ApiModelProperty(value = "发票识别号", dataType = "String")
    private String identifierCode;
    @ApiModelProperty(value = "发票ID", dataType = "String")
    private String id;
    @ApiModelProperty(value = "发票编号", dataType = "String")
    private String invoiceNo;
    @ApiModelProperty(value = "客户编号", required = true, dataType = "String")
    private String customId;
    @ApiModelProperty(value = "客户名称", required = true, dataType = "String")
    private String customName;
    @ApiModelProperty(value = "发票主题", required = true, dataType = "String")
    private String subject;
    @ApiModelProperty(value = "发票金额", required = true, dataType = "String")
    private String amount;
    @ApiModelProperty(value = "开票日期", required = true, dataType = "String")
    private String billingDate;
    @ApiModelProperty(value = "发票ID", dataType = "String")
    private String contractId;
    @ApiModelProperty(value = "发票编号", dataType = "String")
    private String contractNo;
    @ApiModelProperty(value = "核销类型，0-分期， 1-服务", dataType = "String")
    private String writeOffType;

    public InvoiceVO(InvoiceEntity entity) {
        this.amount = entity.getAmount();
        this.billingDate = DateUtils.toString(entity.getBillingDate(), "yyyy-MM-dd HH:mm:SS");
        this.customId = entity.getCustomId();
        this.customName = entity.getCustomName();
        this.id = entity.getId().toString();
        this.identifierCode = entity.getIdentifierCode();
        this.invoiceNo = entity.getInvoiceNo();
        this.subject = entity.getSubject();
        this.contractId = String.valueOf(entity.getContractId());
        this.contractNo = entity.getContractNo();
        this.writeOffType = entity.getWriteOffType() == null ? null : entity.getWriteOffType().getCode();
    }

}
