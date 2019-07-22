package com.liguang.rcs.admin.db.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rcs_role")
public class RoleEntity extends AbstractEntity {
    @Column(name = "name")
    private String name;//角色名称
    @Column(name = "desc")
    private String desc;//角色描述
}
