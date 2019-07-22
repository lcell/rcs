package com.liguang.rcs.admin.db.domain;

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
    private String amount; //发票金额

    @Column(name = "billing_date")
    private Timestamp billingDate; //开票日期

}
