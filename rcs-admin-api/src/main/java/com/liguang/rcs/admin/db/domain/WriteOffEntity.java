package com.liguang.rcs.admin.db.domain;

import com.liguang.rcs.admin.common.enumeration.WriteOffSourceEnum;
import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "rcs_write_off")
public class WriteOffEntity extends AbstractEntity {
    @Column(name = "custom_id")
    private String customId; //客户编号
    @Column(name = "custom_name")
    private String customName; //客户名称
    @Column(name = "payment_date")
    private Timestamp paymentDate; //支付日期
    @Column(name = "payment_amount")
    private String paymentAmount; //支付金额
    @Column(name = "ref_contract_id")
    private Long refContractId; //关联的合同ID
    @Column(name = "ref_contract_no")
    private Long refContractNo; //关联的合同编号
    @Column(name = "source")
    private WriteOffSourceEnum source; //来源
    @Column(name = "type")
    private WriteOffTypeEnum type; //核销类型
}
