package com.liguang.rcs.admin.web.account;

import com.liguang.rcs.admin.db.domain.AccountEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("账户信息")
public class AccountVO {


    @ApiModelProperty(value = "账户ID", dataType = "String")
    private String id;

    @ApiModelProperty(value = "账户编号", dataType = "String")
    private String accountNo;
    @ApiModelProperty(value = "账户名称", dataType = "String")
    private String name;

    @ApiModelProperty(value = "账户名称", dataType = "String")
    private String department;

    @ApiModelProperty(value = "账户名称", dataType = "String")
    private String district;

    @ApiModelProperty(value = "账户名称", dataType = "String")
    private String team;

    @ApiModelProperty(value = "账户名称", dataType = "String")
    private String position;

    @ApiModelProperty(value = "账户名称", dataType = "String")
    private String telNum;

    @ApiModelProperty(value = "账户名称", dataType = "String")
    private String email;

    public AccountVO(AccountEntity entity) {
        this.department = entity.getDepartment();
        this.district = entity.getDistrict();
        this.email = entity.getEmail();
        this.id = entity.getId().toString();
        this.name = entity.getName();
        this.position = entity.getPosition();
        this.team = entity.getTeamName();
        this.telNum = entity.getTelNum();
        this.accountNo = entity.getAccountNo();
    }

}
