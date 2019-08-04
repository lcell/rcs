package com.liguang.rcs.admin.web.writeoff;

import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.CopyProperty;
import com.liguang.rcs.admin.common.copy.EnableCopyProperties;
import com.liguang.rcs.admin.common.copy.converter.StringToNumberConverter;
import com.liguang.rcs.admin.common.copy.converter.StringToTimestampConverter;
import com.liguang.rcs.admin.db.domain.WriteOffEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@ApiModel("增加核销记录")
@Data
@EnableCopyProperties
@Slf4j
public class WriteOffVO {

    @ApiModelProperty(value = "客户编号", dataType = "String", required = true)
    @NotNull(message = "客户ID不可为空")
    @CopyProperty(targetField = "customId")
    private String customId;

    @ApiModelProperty(value = "客户名称", dataType = "String", required = true)
    @CopyProperty(targetField = "customName")
    @NotNull(message = "客户名称不可为空")
    private String customName;

    @ApiModelProperty(value = "实际到账日期，yyyyMMdd", dataType = "String", required = true)
    @CopyProperty(targetField = "paymentDate", typeCovertClass = StringToTimestampConverter.class, extMsg = "yyyyMMdd")
    @NotNull(message = "实际到账日期不可为空")
    private String actualPayDate;

    @ApiModelProperty(value = "实际收款金额", dataType = "String", required = true)
    @CopyProperty(targetField = "paymentAmount", typeCovertClass = StringToNumberConverter.class, extClass = Double.class)
    @NotNull(message = "实际收款金额不可为空")
    private String actualPayAmount;

    @ApiModelProperty(value = "核销结算ID，区分不同核销条目")
    @CopyProperty(targetField = "settlementId")
    private String settlementId;

    @ApiModelProperty(value = "关联的合同ID", dataType = "String")
    @CopyProperty(targetField = "refContractId", typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String contractId;

//    @ApiModelProperty(value = "核销类型， 0-硬件 1-服务", dataType = "String")
//    @CopyProperty(targetField = "type", typeCovertClass = StringToIEnumConverter.class, extClass = WriteOffTypeEnum.class)
//    private String type;
    @ApiModelProperty(value = "核销记录ID", dataType = "String")
    @CopyProperty(targetField = "id", typeCovertClass = StringToNumberConverter.class, extClass = Long.class)
    private String writeOffId;
//
//    @ApiModelProperty(value = "核销类型， 0-硬件 1-服务", dataType = "String")
//    private String type;

    public WriteOffEntity toEntity() {
        try {
            WriteOffEntity entity = new WriteOffEntity();
            BeanUtils.copyProperties(this, entity);
            return entity;
        } catch (Exception ex) {
            log.warn("[WriteOff] convert to Entity fail, vo:{}", this);
            log.error("[WriteOff] convert to Entity fail, Exception:{}", ex);
            return null;
        }
    }

    public static WriteOffVO buildFrom(WriteOffEntity entity) {
        try {
            WriteOffVO vo = new WriteOffVO();
            BeanUtils.reverseCopyProperties(vo, entity);
            return vo;
        } catch (Exception ex) {
            log.warn("[WriteOff] build contractVO fail, Exception:{}", ex);
            return null;
        }
    }
}
