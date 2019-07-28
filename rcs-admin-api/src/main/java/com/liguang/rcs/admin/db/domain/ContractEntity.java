package com.liguang.rcs.admin.db.domain;

import com.liguang.rcs.admin.common.enumeration.ContractStatusEnum;
import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;
import com.liguang.rcs.admin.common.enumeration.ProductTypeEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "rcs_contract")
public class ContractEntity extends  AbstractEntity {
    @Column(name = "contract_no")
    private String contractNo;//合同编号
    @Column(name = "custom_id")
    private String customId;//客户ID
    @Column(name = "custom_name")
    private String customName;//客户名称
    @Column(name = "contract_type")
    private ContractTypeEnum type;//合同类型
    @Column(name = "total_amount")
    private Double totalAmount;//总金额
    @Column(name = "effective_date")
    private Timestamp effectiveDate;//生效日期
    @Column(name = "contract_status")
    private ContractStatusEnum status; //合同状态0-normal 1-legal 2-3rd party
    @Column(name = "contract_name")
    private String contactName; //联系人名称
    @Column(name = "custom_tel")
    private String tel; //客户联系方式
    @Column(name = "custom_email")
    private String email; //客户邮箱
    @Column(name = "product_type")
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
    @Column(name = "team_id")
    private Long teamId; //团队ID
//    @Column(name = "file_path")
//    private String filePath; //合同路径

}
