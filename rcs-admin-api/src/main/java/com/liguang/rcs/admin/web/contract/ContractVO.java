package com.liguang.rcs.admin.web.contract;

import com.liguang.rcs.admin.common.Constant;
import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.CopyProperty;
import com.liguang.rcs.admin.common.copy.EnableCopyProperties;
import com.liguang.rcs.admin.common.copy.converter.StringToDoubleConverter;
import com.liguang.rcs.admin.common.copy.converter.StringToIntegerConverter;
import com.liguang.rcs.admin.common.copy.converter.StringToLongConverter;
import com.liguang.rcs.admin.common.copy.converter.StringToTimestampConverter;
import com.liguang.rcs.admin.common.enumeration.ContractStatusEnum;
import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;
import com.liguang.rcs.admin.common.enumeration.ProductTypeEnum;
import com.liguang.rcs.admin.common.enumeration.converter.ContractStatusConverter;
import com.liguang.rcs.admin.common.enumeration.converter.ContractTypeConverter;
import com.liguang.rcs.admin.common.enumeration.converter.ProductTypeConverter;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.EnumUtils;
import com.liguang.rcs.admin.util.NumericUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("合同信息")
@EnableCopyProperties
public class ContractVO {

    @ApiModelProperty(value = "合同ID", required = true, dataType = "String")
    @CopyProperty(targetField = "id", typeCovertClass = StringToLongConverter.class)
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
    @ApiModelProperty(value = "合同类型", required = true, dataType = "String")
    @CopyProperty(targetField = "type", typeCovertClass = ContractTypeConverter.class)
    @NotNull(message = "合同类型不可为空")
    private String type;
    @ApiModelProperty(value = "合同总金额", required = true, dataType = "String")
    @CopyProperty(targetField = "totalAmount", typeCovertClass = StringToDoubleConverter.class)
    @NotNull(message = "合同总金额不可为空")
    private String totalAmount;

    @ApiModelProperty(value = "合同生效日期 yyyyMMdd", required = true, dataType = "String")
    @CopyProperty(targetField = "status", typeCovertClass = StringToTimestampConverter.class, extMsg = "yyyyMMdd")
    @NotNull(message = "合同生效日期不可为空")
    private String effectiveDate;

    @ApiModelProperty(value = "合同状态", required = true, dataType = "String")
    @CopyProperty(targetField = "status", typeCovertClass = ContractStatusConverter.class)
    @NotNull(message = "合同状态不可为空")
    private String status;
    @ApiModelProperty(value = "联系人", required = true, dataType = "String")
    @CopyProperty(targetField = "contactName")
    @NotNull(message = "联系人不可为空")
    private String contactName;
    @ApiModelProperty(value = "联系手机号码", required = true, dataType = "String")
    @CopyProperty(targetField = "tel")
    @NotNull(message = "联系手机号码不可为空")
    private String tel;
    @ApiModelProperty(value = "联系邮箱", required = true, dataType = "String")
    @NotNull(message = "联系邮箱不可为空")
    @CopyProperty(targetField = "email")
    private String email;


    @ApiModelProperty(value = "产品类型", required = true, dataType = "String")
    @CopyProperty(targetField = "productType", typeCovertClass = ProductTypeConverter.class)
    @NotNull(message = "产品类型不可为空")
    private String productType;

    @ApiModelProperty(value = "应收期数", required = true, dataType = "String")
    @CopyProperty(targetField = "productType", typeCovertClass = StringToIntegerConverter.class)
    @NotNull(message = "应收期数不可为空")
    private String receivableNum;

    @ApiModelProperty(value = "首付款", required = true, dataType = "String")
    @CopyProperty(targetField = "firstPayment", typeCovertClass = StringToDoubleConverter.class)
    @NotNull(message = "首付款不可为空")
    private String firstPayment;

    @ApiModelProperty(value = "每期应付", required = true, dataType = "String")
    @CopyProperty(targetField = "periodPayment", typeCovertClass = StringToDoubleConverter.class)
    @NotNull(message = "每期应付不可为空")
    private String periodPayment;


    @ApiModelProperty(value = "合同文件", required = true, dataType = "File")
    @NotNull(message = "合同文件不可为空")
    private MultipartFile file;

    public ContractVO(ContractEntity entity) {
        this.contactName = entity.getContactName();
        this.contractId = entity.getId().toString();
        this.contractNo = entity.getContractNo();
        this.customId = entity.getCustomId();
        this.customName = entity.getCustomName();
        this.effectiveDate = DateUtils.toString(entity.getEffectiveDate(), Constant.EFFECT_DATE_FORMAT);
        this.email = entity.getEmail();
        this.firstPayment = entity.getFirstPayment().toString();
        this.periodPayment = entity.getPeriodPayment().toString();
        this.productType = entity.getProductType().getCode();
        this.receivableNum = entity.getReceivableNum().toString();
        this.status = entity.getStatus().getCode();
        this.totalAmount = entity.getTotalAmount().toString();
        this.tel = entity.getTel();
        //this.type = entity.getType().

    }

    public ContractEntity toEntity() throws Exception {
        ContractEntity entity = new ContractEntity();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }
}
