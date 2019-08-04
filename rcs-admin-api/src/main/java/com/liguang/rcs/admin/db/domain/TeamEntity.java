package com.liguang.rcs.admin.db.domain;

import com.liguang.rcs.admin.common.enumeration.TeamTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rcs_team")
@EqualsAndHashCode(callSuper = true)
public class TeamEntity extends AbstractEntity {
    @Column(name = "name")
    private String name;  //团队名称
    @Column(name = "desc")
    private String desc; //团队描述
    @Column(name = "type")
    private TeamTypeEnum type; //团队类型
    @Column(name = "parent_ref_team_id")
    private Long parentRefTeamId; //上级团队ID信息
    @Column(name = "team_leader_id")
    private Long teamLeaderId; //团队领导ID
    @Column(name = "team_leader_name")
    private String teamLeaderName; //团队领导名
}
