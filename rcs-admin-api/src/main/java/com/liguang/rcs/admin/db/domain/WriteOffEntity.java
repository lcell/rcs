package com.liguang.rcs.admin.db.domain;

import com.liguang.rcs.admin.common.enumeration.WriteOffSourceEnum;
import com.liguang.rcs.admin.common.enumeration.converter.db.WriteOffSourceToString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "rcs_write_off")
@EqualsAndHashCode(callSuper = true)
public class WriteOffEntity extends AbstractEntity {
    @Column(name = "custom_id")
    private String customId; //客户编号
    @Column(name = "custom_name")
    private String customName; //客户名称
    @Column(name = "payment_date")
    private Timestamp paymentDate; //支付日期 不可为空
    @Column(name = "payment_amount")
    private Double paymentAmount; //支付金额 不可为空
    @Column(name = "ref_contract_id")
    private Long refContractId; //关联的合同ID
    @Column(name = "settlement_id")
    private String settlementId; //关联核销结算ID，用于区分绑定到哪条记录上
    @Column(name = "source")
    @Convert(converter = WriteOffSourceToString.class)
    private WriteOffSourceEnum source; //来源
//    @Column(name = "type")
//    @Convert(converter = WriteOffTypeToString.class)
//    private WriteOffTypeEnum type; //核销类型
}
