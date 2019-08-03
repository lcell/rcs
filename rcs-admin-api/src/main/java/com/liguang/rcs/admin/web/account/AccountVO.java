package com.liguang.rcs.admin.web.account;

import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.CopyProperty;
import com.liguang.rcs.admin.common.copy.EnableCopyProperties;
import com.liguang.rcs.admin.common.copy.converter.StringToNumberConverter;
import com.liguang.rcs.admin.db.domain.AccountEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@ApiModel("账户信息")
@EnableCopyProperties
@Slf4j
public class AccountVO {

    @ApiModelProperty(value = "账户ID", dataType = "String")
    @CopyProperty(typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String id;

    @ApiModelProperty(value = "账户编号", dataType = "String")
    @CopyProperty
    private String accountNo;
    @ApiModelProperty(value = "账户名称", dataType = "String")
    @CopyProperty
    private String name;

    @ApiModelProperty(value = "所属部门", dataType = "String")
    @CopyProperty
    private String department;

    @ApiModelProperty(value = "部门Id", dataType = "String")
    @CopyProperty(typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String departmentId;
    
    @ApiModelProperty(value = "所属区域", dataType = "String")
    @CopyProperty
    private String district;

    @ApiModelProperty(value = "所属团队", dataType = "String")
    @CopyProperty
    private String team;

    @CopyProperty(typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    @ApiModelProperty(value = "团队ID", dataType = "String")
    private String teamId;

    @ApiModelProperty(value = "职位", dataType = "String")
    @CopyProperty
    private String position;

    @ApiModelProperty(value = "手机号码", dataType = "String")
    @CopyProperty
    private String telNum;

    @ApiModelProperty(value = "联系邮箱", dataType = "String")
    @CopyProperty
    private String email;

    @ApiModelProperty(value = "角色ID集合", dataType = "Long[]")
    @CopyProperty
    private Long[] roleIds;
    /**
     * 通过entity生成vo对象
     */
    public static AccountVO buildFrom(AccountEntity entity) {
        try {
            AccountVO vo = new AccountVO();
            BeanUtils.reverseCopyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            log.warn("[Account] build accountVO fail, Exception:{}", ex);
            return null;
        }
    }

    public AccountEntity toEntity() {
        try {
            AccountEntity entity = new AccountEntity();
            BeanUtils.copyProperties(this, entity);
            return entity;
        } catch (Exception ex) {
            log.warn("[Account] build accountEntity fail, Exception:{}", ex);
            return null;
        }
    }

}
