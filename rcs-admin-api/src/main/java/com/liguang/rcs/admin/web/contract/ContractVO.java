package com.liguang.rcs.admin.web.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@ApiModel("合同信息")
public class ContractVO {
    @ApiModelProperty(value = "合同编号", required = true, dataType = "String")
    private String id;
    @ApiModelProperty(value = "客户编号", required = true, dataType = "String")
    private String customId;
    @ApiModelProperty(value = "客户名称", required = true, dataType = "String")
    private String customName;
    @ApiModelProperty(value = "合同类型", required = true, dataType = "String")
    private String type;
    @ApiModelProperty(value = "合同总金额", required = true, dataType = "String")
    private String totalAmount;
    @ApiModelProperty(value = "合同生效日期", required = true, dataType = "String")
    private String effectiveDate;
    @ApiModelProperty(value = "合同状态", required = true, dataType = "String")
    private String status;
    @ApiModelProperty(value = "联系人", required = true, dataType = "String")
    private String contactName;
    @ApiModelProperty(value = "联系方式", required = true, dataType = "String")
    private String tel;
    @ApiModelProperty(value = "联系邮箱", required = true, dataType = "String")
    private String email;


    @ApiModelProperty(value = "产品类型", required = true, dataType = "String")
    private String productType;

    @ApiModelProperty(value = "应收期数", required = true, dataType = "String")
    private String receivableNum;

    @ApiModelProperty(value = "首付款", required = true, dataType = "String")
    private String firstPayment;

    @ApiModelProperty(value = "每期应付", required = true, dataType = "String")
    private String periodPayment;


    @ApiModelProperty(value = "合同文件", required = true, dataType = "File")
    private MultipartFile file;


}
