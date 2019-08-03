package com.liguang.rcs.admin.db.domain;

import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.common.enumeration.converter.db.WriteOffSourceToString;
import com.liguang.rcs.admin.common.enumeration.converter.db.WriteOffTypeToString;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rcs_unapplied_cache")
public class UnAppliedCashEntity extends AbstractEntity {
    @Column(name = "ref_contract_id")
    private Long refContractId; //关联的合同Id
    @Column(name = "receivable_amount")
    private Double receivableAmount; //应收合计
    @Column(name = "actual_pay_amount")
    private Double actualPayAmount;//实际支付
    @Column(name = "unoverdue_amount")
    private Double unOverdueAmount; //未逾期
    @Column(name = "day1_5")
    private Double day1_5; //逾期1-5天
    @Column(name = "day6_30")
    private Double day6_30; //逾期6-30天
    @Column(name = "day1_30")
    private Double day1_30; //逾期1-30天
    @Column(name = "day31_60")
    private Double day31_60;//逾期31-60天
    @Column(name = "day61_90")
    private Double day61_90;//逾期61-90天
    @Column(name = "day91_180")
    private Double day91_180;//逾期91-180天
    @Column(name = "day181_365")
    private Double day181_365;//逾期181-365天
    @Column(name = "day_lt_365")
    private Double day_lt_365;//逾期超过365
    @Column(name = "day_lt_total")
    private Double day_lt_90_total;//逾期大于90天合计
    @Column(name = "day_lt_90_total")
    private Double day_lt_90_ratio;//逾期大于90天占比
    @Column(name = "overdue_total")
    private Double overdueTotal; //逾期合计
    @Column(name = "total")
    private Double total; //本期合计
    @Column(name = "write_off_type")
    @Convert(converter = WriteOffTypeToString.class)
    private WriteOffTypeEnum writeOffType;

}
