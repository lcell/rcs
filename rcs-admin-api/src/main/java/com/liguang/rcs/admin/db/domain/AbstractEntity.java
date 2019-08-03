package com.liguang.rcs.admin.db.domain;

import com.liguang.rcs.admin.web.account.AccountVO;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

@MappedSuperclass
@Data
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;//ID，本平台自动生成的

    @Column(name = "create_by", nullable = false, updatable = false)
    private String createBy;//创建者ID
    @Column(name = "create_date", nullable = false, updatable = false)
    private Timestamp createDate; //创建日期
    @Column(name = "update_by")
    private String updateBy; //更新者ID
    @Column(name = "update_date")
    private Timestamp updateDate;//更新时间

    @PrePersist
    public void prePersist() {
        this.createDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.updateDate = this.createDate;
        this.updateBy = currentUserName();
        this.createBy = updateBy;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.updateBy = currentUserName();
    }

    private String currentUserName() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal instanceof AccountVO) {
            return ((AccountVO)principal).getName();
        }
        //系统内部的数据库操作，标记为system
        return "system";
    }
}
