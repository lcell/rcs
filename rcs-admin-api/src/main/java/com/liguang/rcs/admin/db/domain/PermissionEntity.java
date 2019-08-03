package com.liguang.rcs.admin.db.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rcs_permission")
public class PermissionEntity extends AbstractEntity {

    @Column(name = "role_id")
    private Long roleId;//角色id
    
    @Column(name = "permission")
    private String permission;//权限
}
