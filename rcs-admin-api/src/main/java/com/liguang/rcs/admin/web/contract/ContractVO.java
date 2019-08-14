package com.liguang.rcs.admin.web.contract;

import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.CopyProperty;
import com.liguang.rcs.admin.common.copy.EnableCopyProperties;
import com.liguang.rcs.admin.common.copy.converter.StringToNumberConverter;
import com.liguang.rcs.admin.common.copy.converter.StringToTimestampConverter;
import com.liguang.rcs.admin.common.enumeration.ContractStatusEnum;
import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;
import com.liguang.rcs.admin.common.enumeration.ProductTypeEnum;
import com.liguang.rcs.admin.common.enumeration.converter.StringToIEnumConverter;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("合同信息")
@EnableCopyProperties
@Slf4j
public class ContractVO {

    @ApiModelProperty(value = "合同ID", required = true, dataType = "String")
    @CopyProperty(targetField = "id", typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String contractId;
    @ApiModelProperty(value = "合同编号", required = true, dataType = "String")
    @NotNull(message = "合同编号不可为空")
    @CopyProperty(targetField = "contractNo")
    private String contractNo;
    @ApiModelProperty(value = "客户编号", required = true, dataType = "String")
    @NotNull(message = "客户编号不可为空")
    @CopyProperty(targetField = "customId")
    private String customId;
    @ApiModelProperty(value = "客户名称", required = true, dataType = "String")
    @CopyProperty(targetField = "customName")
    @NotNull(message = "客户名称不可为空")
    private String customName;
    @ApiModelProperty(value = "合同类型 0-硬件，1-服务", required = true, dataType = "String")
    @CopyProperty(targetField = "type", typeCovertClass = StringToIEnumConverter.class, extClass = ContractTypeEnum.class)
    //@NotNull(message = "合同类型不可为空")
    private String type;
    @ApiModelProperty(value = "合同总金额", required = true, dataType = "String")
    @CopyProperty(targetField = "totalAmount", typeCovertClass = StringToNumberConverter.class, extClass = Double.class)
    @NotNull(message = "合同总金额不可为空")
    private String totalAmount;

//    @ApiModelProperty(value = "合同生效日期 yyyyMMdd", required = true, dataType = "String")
//    @CopyProperty(targetField = "effectiveDate", typeCovertClass = StringToTimestampConverter.class, extMsg = "yyyyMMdd")
//    @NotNull(message = "合同生效日期不可为空")
//    private String effectiveDate;

    @ApiModelProperty(value = "合同状态 0-normal 1-legal 2-3rd party", required = true, dataType = "String")
    @CopyProperty(targetField = "status", typeCovertClass = StringToIEnumConverter.class, extClass= ContractStatusEnum.class)
    @NotNull(message = "合同状态不可为空")
    private String status;

    @ApiModelProperty(value = "联系人信息, json字符串", required = true, dataType = "String")
    @CopyProperty
    @NotNull(message = "联系人信息不可为空")
    private String contactsInfo;

    @ApiModelProperty(value = "产品类型 0-直销 1-渠道 2-爱德堡", required = true, dataType = "String")
    @CopyProperty(targetField = "productType", typeCovertClass = StringToIEnumConverter.class, extClass= ProductTypeEnum.class)
    //@NotNull(message = "产品类型不可为空")
    private String productType;

    @ApiModelProperty(value = "应收期数", required = true, dataType = "String")
    @CopyProperty(targetField = "receivableNum", typeCovertClass = StringToNumberConverter.class, extClass = Integer.class)
//    @NotNull(message = "应收期数不可为空")
    private String receivableNum;

    @ApiModelProperty(value = "首付款", required = true, dataType = "String")
    @CopyProperty(targetField = "firstPayment", typeCovertClass = StringToNumberConverter.class, extClass = Double.class)
//    @NotNull(message = "首付款不可为空")
    private String firstPayment;

    @ApiModelProperty(value = "每期应付", required = true, dataType = "String")
    @CopyProperty(targetField = "periodPayment", typeCovertClass = StringToNumberConverter.class, extClass = Double.class)
//    @NotNull(message = "每期应付不可为空")
    private String periodPayment;


    @ApiModelProperty(value = "销售经理名称", required = true, dataType = "String")
    @CopyProperty
    @NotNull(message = "销售经理名称不可为空")
    private String salesName;

    @ApiModelProperty(value = "销售经理编号", required = true, dataType = "String")
    @CopyProperty
    @NotNull(message = "销售经理编号不可为空")
    private String salesNo;


    /**
     * 通过entity生成vo对象
     */
    public static ContractVO buildFrom(ContractEntity entity) {
        try {
            ContractVO vo = new ContractVO();
            BeanUtils.reverseCopyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            log.warn("[Contract] build contractVO fail, Exception:{}", ex);
            return null;
        }
    }

    public ContractEntity toEntity() {
        try {
            ContractEntity entity = new ContractEntity();
            BeanUtils.copyProperties(this, entity);
            return entity;
        } catch (Exception ex) {
            log.warn("[Contract] build contractEntity fail, Exception:{}", ex);
            return null;
        }
    }
}
