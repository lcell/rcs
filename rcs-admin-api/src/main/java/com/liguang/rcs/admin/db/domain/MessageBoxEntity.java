package com.liguang.rcs.admin.db.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rcs_message_box")
public class MessageBoxEntity extends AbstractEntity {
    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "message")
    private String message;
    @Column(name = "message_type")
    private String messageType;
    @Column(name = "send_flag")
    private boolean sendFlag;
}
