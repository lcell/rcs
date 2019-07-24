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
import org.springframework.web.multipart.MultipartFile;

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
    @ApiModelProperty(value = "合同类型", required = true, dataType = "String")
    @CopyProperty(targetField = "type", typeCovertClass = StringToIEnumConverter.class, extClass = ContractTypeEnum.class)
    @NotNull(message = "合同类型不可为空")
    private String type;
    @ApiModelProperty(value = "合同总金额", required = true, dataType = "String")
    @CopyProperty(targetField = "totalAmount", typeCovertClass = StringToNumberConverter.class, extClass = Double.class)
    @NotNull(message = "合同总金额不可为空")
    private String totalAmount;

    @ApiModelProperty(value = "合同生效日期 yyyyMMdd", required = true, dataType = "String")
    @CopyProperty(targetField = "status", typeCovertClass = StringToTimestampConverter.class, extMsg = "yyyyMMdd")
    @NotNull(message = "合同生效日期不可为空")
    private String effectiveDate;

    @ApiModelProperty(value = "合同状态", required = true, dataType = "String")
    @CopyProperty(targetField = "status", typeCovertClass = StringToIEnumConverter.class, extClass= ContractStatusEnum.class)
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
    @CopyProperty(targetField = "productType", typeCovertClass = StringToIEnumConverter.class, extClass= ProductTypeEnum.class)
    @NotNull(message = "产品类型不可为空")
    private String productType;

    @ApiModelProperty(value = "应收期数", required = true, dataType = "String")
    @CopyProperty(targetField = "productType", typeCovertClass = StringToNumberConverter.class, extClass = Integer.class)
    @NotNull(message = "应收期数不可为空")
    private String receivableNum;

    @ApiModelProperty(value = "首付款", required = true, dataType = "String")
    @CopyProperty(targetField = "firstPayment", typeCovertClass = StringToNumberConverter.class, extClass = Double.class)
    @NotNull(message = "首付款不可为空")
    private String firstPayment;

    @ApiModelProperty(value = "每期应付", required = true, dataType = "String")
    @CopyProperty(targetField = "periodPayment", typeCovertClass = StringToNumberConverter.class, extClass = Double.class)
    @NotNull(message = "每期应付不可为空")
    private String periodPayment;


    @ApiModelProperty(value = "合同文件", required = true, dataType = "File")
    @NotNull(message = "合同文件不可为空")
    private MultipartFile file;

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
