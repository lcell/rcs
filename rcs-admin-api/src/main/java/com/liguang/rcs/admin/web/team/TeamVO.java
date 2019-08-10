package com.liguang.rcs.admin.web.team;

import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.CopyProperty;
import com.liguang.rcs.admin.common.copy.EnableCopyProperties;
import com.liguang.rcs.admin.common.copy.converter.StringToNumberConverter;
import com.liguang.rcs.admin.db.domain.AbstractEntity;
import com.liguang.rcs.admin.db.domain.TeamEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Data
@Slf4j
@ApiModel("团队信息")
@EnableCopyProperties
public class TeamVO {

    @ApiModelProperty(value = "团队ID", dataType = "String")
    @CopyProperty(typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String id;

    @ApiModelProperty(value = "团队名称", dataType = "String", required = true)
    @NotBlank(message = "团队名称不可为空")
    @CopyProperty
    private String name;

    @ApiModelProperty(value = "团队描述", dataType = "String")
    @CopyProperty
    private String desc;

    @ApiModelProperty(value = "上级团队ID", dataType = "String")
    @CopyProperty(typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String parentRefTeamId;

    @ApiModelProperty(value = "团队leaderId", dataType = "String")
    @CopyProperty(targetField = "teamLeaderId", typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String leaderId;

    @ApiModelProperty(value = "团队leader的名字", dataType = "String")
    @CopyProperty(targetField = "teamLeaderName")
    private String leaderName;

//    @ApiModelProperty("团队类型 1-部门 2-团队")
//    @CopyProperty(targetField = "type", typeCovertClass = StringToIEnumConverter.class, extClass = TeamTypeEnum.class)
//    private String teamType;

    /**
     * 通过entity生成vo对象
     */
    public static TeamVO buildFrom(TeamEntity entity) {
        try {
            TeamVO vo = new TeamVO();
            BeanUtils.reverseCopyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            log.warn("[Contract] build contractVO fail, Exception:{}", ex);
            return null;
        }
    }

    public TeamEntity toEntity() {
        try {
            TeamEntity entity = new TeamEntity();
            BeanUtils.copyProperties(this, entity);
            return entity;
        } catch (Exception ex) {
            log.warn("[Contract] build contractEntity fail, Exception:{}", ex);
            return null;
        }
    }
}
