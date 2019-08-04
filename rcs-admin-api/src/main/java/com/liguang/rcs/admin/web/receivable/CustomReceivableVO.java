package com.liguang.rcs.admin.web.receivable;

import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.CopyProperty;
import com.liguang.rcs.admin.common.copy.converter.StringToNumberConverter;
import com.liguang.rcs.admin.db.domain.UnAppliedCashEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

import static com.liguang.rcs.admin.util.NumericUtils.minus;

@ApiModel("客户应收硬件分期")
@Data
@Slf4j
public class CustomReceivableVO extends TableCommonColumn implements Cloneable {
    @ApiModelProperty(value = "手动调整ID， 用于更新", dataType = "Long")
    @CopyProperty
    private Long id;
    @ApiModelProperty(value = "合同ID", dataType = "String")
    @CopyProperty(targetField = "refContractId", typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    @NotNull(message = "合同ID不可为空")
    private String contractId;
    @ApiModelProperty(value = "应收款合计, 本期总计",dataType = "String")
    @CopyProperty(targetField = "receivableAmount")
    private Double receivablePayment;
    @ApiModelProperty(value = "实际到账金额",dataType = "String")
    @CopyProperty(targetField = "actualPayAmount")
    private Double actualPayment;

    public UnAppliedCashEntity toEntity() {
        try {
            UnAppliedCashEntity entity = new UnAppliedCashEntity();
            BeanUtils.copyProperties(this, entity);
            return entity;
        } catch (Exception ex) {
            log.warn("[Contract] build contractEntity fail, Exception:{}", ex);
            return null;
        }
    }

    public static CustomReceivableVO buildFrom(UnAppliedCashEntity entity) {
        try {
            CustomReceivableVO vo = new CustomReceivableVO();
            BeanUtils.reverseCopyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            log.warn("[Contract] build contractVO fail, Exception:{}", ex);
            return null;
        }
    }

    @Override
    public CustomReceivableVO clone() {
        try {
            return (CustomReceivableVO)super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("[Receivable] Inner Err, Exception:{}", e);
            return null;
        }
    }

    public void minusOther(CustomReceivableVO other) {
        super.minusOther(other);
        receivablePayment = minus(receivablePayment, other.receivablePayment);
        actualPayment = minus(actualPayment, other.actualPayment);
    }
}
