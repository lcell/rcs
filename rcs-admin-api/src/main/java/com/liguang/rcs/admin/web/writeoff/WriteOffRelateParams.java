package com.liguang.rcs.admin.web.writeoff;

import com.google.common.collect.Lists;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.util.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@ApiModel("核销关联合同处理参数")
@Data
@Slf4j
public class WriteOffRelateParams {
    @ApiModelProperty(value = "合同ID", dataType = "String", required = true)
    @NotNull(message = "合同ID不可为空")
    private String contractId;
    @ApiModelProperty(value = "核销记录ID集合, 对于删除核销，如果为空则清空所有的关联数据", dataType = "List<String>", required = true)
    private List<String> writeOffIds;
//    @ApiModelProperty(value = "核销类型, 0-硬件 1-服务", dataType = "String", required = true)
//    @NotNull(message = "核销类型不可为空")
//    private String writeOffType;
    @ApiModelProperty(value = "关联的核销结算的ID", dataType = "String", required = true)
    @NotNull(message = "关联的核销结算的ID不可为空")
    private String settlementId;

    public Long checkAndGetContractId() throws BaseException {
        Long conId = null;
        if((conId = NumericUtils.toLong(getContractId())) == null) {
            log.error("[WriteOff] contractID is invalid, contractId:{}", getContractId());
            throw new BaseException(ResponseCode.BAD_ARGUMENT_VALUE);
        }
        return conId;
    }

    public List<Long> checkAndGetWriteOffIds() throws BaseException {
        if (writeOffIds == null || writeOffIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> writeOffIds = Lists.newArrayListWithCapacity(this.writeOffIds.size());
        for (String writeOffId : this.writeOffIds) {
            Long tmpWriteOffId = null;
            if ((tmpWriteOffId = NumericUtils.toLong(writeOffId)) == null) {
                log.error("[WriteOff] writeOffId is invalid :{}", writeOffId);
                throw new BaseException(ResponseCode.BAD_ARGUMENT_VALUE);
            }
            writeOffIds.add(tmpWriteOffId);
        }
        return writeOffIds;
    }
}
