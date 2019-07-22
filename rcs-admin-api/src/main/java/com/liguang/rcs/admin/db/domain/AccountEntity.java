package com.liguang.rcs.admin.db.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rcs_account")
@Data
public class AccountEntity extends AbstractEntity {
    @Column(name = "account_no", unique = true)
    private String accountNo; //编号
    @Column(name = "name")
    private String name; //姓名
    @Column(name = "department")
    private String department;//部门
    @Column(name = "department_id")
    private Long departmentId;//部门ID
    @Column(name = "district")
    private String district; //所属区域

    @Column(name = "team_name")
    private String teamName;//团队

    @Column(name = "team_id")
    private String teamId;//团队ID

    @Column(name = "position")
    private String position;//职位

    @Column(name = "tel_number")
    private String telNum; //手机号码

    @Column(name = "email")
    private String email; //邮箱

    @Column(name = "passwd")
    private String passwd; //密码

    @Column(name = "role_ids")
    private Long[] roleIds;//角色
}
