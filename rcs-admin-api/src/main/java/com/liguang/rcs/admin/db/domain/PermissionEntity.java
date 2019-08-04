package com.liguang.rcs.admin.db.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rcs_permission")
@EqualsAndHashCode(callSuper = true)
public class PermissionEntity extends AbstractEntity {

    @Column(name = "role_id")
    private Long roleId;//角色id
    
    @Column(name = "permission")
    private String permission;//权限
}
