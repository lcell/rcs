package com.liguang.rcs.admin.web.account;

import com.liguang.rcs.admin.db.domain.AccountEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("账户信息")
public class AccountVO {

    @ApiModelProperty(value = "账户编号", dataType = "String")
    private String id;

    private String name;

    private String department;

    private String district;

    private String team;

    private String position;

    private String telNum;

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
    }

}
