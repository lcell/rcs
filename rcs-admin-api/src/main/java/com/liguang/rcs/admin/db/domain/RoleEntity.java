package com.liguang.rcs.admin.db.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rcs_role")
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends AbstractEntity {
    @Column(name = "name")
    private String name;//角色名称
    @Column(name = "desc")
    private String desc;//角色描述
}
