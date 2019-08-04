package com.liguang.rcs.admin.web.invoice;

import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.CopyProperty;
import com.liguang.rcs.admin.common.copy.EnableCopyProperties;
import com.liguang.rcs.admin.common.copy.converter.StringToNumberConverter;
import com.liguang.rcs.admin.common.copy.converter.StringToTimestampConverter;
import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@ApiModel("发票信息")
@EnableCopyProperties
@Slf4j
public class InvoiceVO {
    @ApiModelProperty(value = "发票识别号", dataType = "String")
    @CopyProperty
    private String identifierCode;
    @ApiModelProperty(value = "发票ID", dataType = "String")
    @CopyProperty(typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String id;
    @ApiModelProperty(value = "发票编号", dataType = "String")
    @CopyProperty
    private String invoiceNo;
    @ApiModelProperty(value = "客户编号", required = true, dataType = "String")
    @CopyProperty
    private String customId;
    @ApiModelProperty(value = "客户名称", required = true, dataType = "String")
    @CopyProperty
    private String customName;
    @ApiModelProperty(value = "发票主题", required = true, dataType = "String")
    @CopyProperty
    private String subject;
    @ApiModelProperty(value = "发票金额", required = true, dataType = "String")
    @CopyProperty(typeCovertClass = StringToNumberConverter.class, extClass = Double.class)
    private String amount;
    @ApiModelProperty(value = "开票日期格式 yyyyMMdd", required = true, dataType = "String")
    @CopyProperty(typeCovertClass = StringToTimestampConverter.class, extMsg = "yyyyMMdd")
    private String billingDate;
    @CopyProperty(typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    @ApiModelProperty(value = "发票ID", dataType = "String")
    private String contractId;
//    @CopyProperty(typeCovertClass = StringToIEnumConverter.class, extClass = WriteOffTypeEnum.class)
//    @ApiModelProperty(value = "核销类型，0-分期， 1-服务", dataType = "String")
//    private String writeOffType;

    public static InvoiceVO buildFrom(InvoiceEntity entity) {
        try {
            InvoiceVO vo = new InvoiceVO();
            BeanUtils.reverseCopyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            log.warn("[Contract] build contractVO fail, Exception:{}", ex);
            return null;
        }
    }

    public InvoiceEntity toEntity() {
        try {
            InvoiceEntity entity = new InvoiceEntity();
            BeanUtils.copyProperties(this, entity);
            return entity;
        } catch (Exception ex) {
            log.warn("[Contract] build contractEntity fail, Exception:{}", ex);
            return null;
        }
    }

}
