package com.liguang.rcs.admin.db.domain;

import com.liguang.rcs.admin.common.enumeration.ContractStatusEnum;
import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;
import com.liguang.rcs.admin.common.enumeration.ProductTypeEnum;
import com.liguang.rcs.admin.common.enumeration.converter.db.ContractStatusToString;
import com.liguang.rcs.admin.common.enumeration.converter.db.ContractTypeToString;
import com.liguang.rcs.admin.common.enumeration.converter.db.ProductTypeToString;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "rcs_contract")
@EqualsAndHashCode(callSuper = true)
public class ContractEntity extends AbstractEntity {
    @Column(name = "contract_no")
    private String contractNo;//合同编号
    @Column(name = "custom_id")
    private String customId;//客户ID
    @Column(name = "custom_name")
    private String customName;//客户名称
    @Column(name = "contract_type")
    @Convert(converter = ContractTypeToString.class)
    private ContractTypeEnum type;//合同类型
    @Column(name = "total_amount")
    private Double totalAmount;//总金额
    @Column(name = "effective_date")
    private Timestamp effectiveDate;//生效日期
    @Column(name = "contract_status")
    @Convert(converter = ContractStatusToString.class)
    private ContractStatusEnum status; //合同状态0-normal 1-legal 2-3rd party
    @Column(name = "contacts_info")
    private String contactsInfo; //联系人信息
    @Column(name = "product_type")
    @Convert(converter = ProductTypeToString.class)
    private ProductTypeEnum productType;//产品类型
    @Column(name = "receivable_num")
    private Integer receivableNum;//应收期数
    @Column(name = "first_payment")
    private Double firstPayment;//首付
    @Column(name = "period_payment")
    private Double periodPayment;// 每期付款
    @Column(name = "sales_id")
    private Long salesId; //销售经理ID
    @Column(name = "sales_no")
    private String salesNo; //销售经理编号
    @Column(name = "sales_name")
    private String salesName; //销售经理名称
    @Column(name = "team_id")
    private Long teamId; //团队ID

}
