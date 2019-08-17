package com.liguang.rcs.admin.web.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("合同查询条件参数")
public class QueryParams {

    /**
     * 合同查询使用的参数
     */
    @ApiModelProperty(value = "客户名称", dataType = "String")
    private String customName;
    @ApiModelProperty(value = "合同编号", dataType = "String")
    private String contractNo;
    @ApiModelProperty(value = "产品类型  0-直销 1-渠道 2-爱德堡", dataType = "String")
    private String productType;
    @ApiModelProperty(value = "合同类型  0-硬件，1-服务", dataType = "String")
    private String type;
    @ApiModelProperty(value = "生效开始日期, yyyy-MM-dd", dataType = "String")
    private String startDate;
    @ApiModelProperty(value = "生效结束日期, yyyy-MM-dd", dataType = "String")
    private String endDate;

    @ApiModelProperty(value = "当前页面", dataType = "int")
    private int currentPage = 1;
    @ApiModelProperty(value = "页面数据条数", dataType = "int")
    private int pageSize = 20;
    /**
     *  合同查询使用参数
     */

    @ApiModelProperty(value = "客户编号", dataType = "String")
    private String customId;
    @ApiModelProperty(value = "销售经理名称", dataType = "String")
    private String salesName;


    @ApiModelProperty(value = "合同状态 0-normal 1-legal 2-3rd party", dataType = "String")
    private String status;

    @ApiModelProperty(value = "团队ID", dataType = "String")
    private String teamId;


}
