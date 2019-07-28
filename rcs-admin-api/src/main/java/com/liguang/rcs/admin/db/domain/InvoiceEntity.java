package com.liguang.rcs.admin.db.domain;

import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "rcs_invoice")
public class InvoiceEntity extends AbstractEntity {

    @Column(name = "identifier_code")
    private String identifierCode; //发票识别号

    @Column(name = "invoice_no")
    private String invoiceNo;  //发票编号

    @Column(name = "subject")
    private String subject;  //发票主题

    @Column(name = "custom_id")
    private String customId; //客户编号

    @Column(name = "custom_name")
    private String customName; //客户名称

    @Column(name = "amount")
    private Double amount; //发票金额

    @Column(name = "billing_date")
    private Timestamp billingDate; //开票日期
    @Column(name = "contract_id")
    private int contractId; // 发票所关联的合同ID, 外键
    @Column(name = "contract_no")
    private String contractNo;//发票所关联的合同编号
    @Column(name = "write_off_type")
    private WriteOffTypeEnum writeOffType; //发票核销类型

}
