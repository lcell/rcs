package com.liguang.rcs.admin.web.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("合同查询条件参数")
public class QueryParams {

    @ApiModelProperty(value = "合同编号", dataType = "String")
    private String contractId;
    @ApiModelProperty(value = "客户编号", dataType = "String")
    private String customId;
    @ApiModelProperty(value = "产品类型", dataType = "String")
    private String productType;
    @ApiModelProperty(value = "合同类型", dataType = "String")
    private String contractType;
    @ApiModelProperty(value = "生效开始日期", dataType = "String")
    private String startDate;
    @ApiModelProperty(value = "生效结束日期", dataType = "String")
    private String endDate;

    @ApiModelProperty("当前页面")
    private String currentPage;
    @ApiModelProperty("页面数据条数")
    private String pageSize;
}
