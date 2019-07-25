package com.liguang.rcs.admin.web.writeoff;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.service.ContractService;
import com.liguang.rcs.admin.service.WriteOffService;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.EnumUtils;
import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.web.contract.ContractVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

@Api(tags = "核销管理API")
@RestController
@RequestMapping("/rcs/writeOff")
@Slf4j
public class WriteOffController {

    @Autowired
    private WriteOffService writeOffService;

    @Autowired
    private ContractService contractService;

    @ApiOperation("通过合同ID查询核销结算记录")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同ID", type = "String", required = true)
    )
    @GetMapping("/querySettlementByContractId/{contractId}")
    public ResponseObject<List<WriteOffSettlementVO>> querySettlementByContractId(@PathVariable("contractId") String contractId) {
        Long contractIdLong = null;
        ContractEntity contract = null;
        if ((contractIdLong = NumericUtils.toLong(contractId)) == null
                || (contract = contractService.queryEntityById(contractIdLong)) == null) {
            log.error("[WriteOff] contract not exist, contractId:{}", contractId);
            return ResponseObject.dataNotExist();
        }
        //TODO

        return ResponseObject.success(writeOffService.querySettlement(contract, WriteOffTypeEnum.HARDWARE));
    }

    @ApiOperation("根据客户ID和合同生效时间查看核销记录列表")
    @PostMapping("/queryWriteOffRecord")
    public ResponseObject<List<WriteOffVO>> queryWriteOffRecord(@RequestBody QueryWriteOffParams params) {
        Timestamp effectTime = null;
        if (params == null
                || (effectTime = DateUtils.softToTimestamp(params.getEffectDate(), "yyyyMMdd")) == null
                || Strings.isNullOrEmpty(params.getEffectDate())) {
            log.error("[WriteOff] query params is invalid, params:{}", params);
            return ResponseObject.badArgumentValue();
        }
        return ResponseObject.success(writeOffService.queryWriteOffRecord(params.getCustomId(), effectTime));
    }


    @ApiOperation("关联核销记录到合同")
    @PostMapping("/relationToContract")
    public ResponseObject relationToContract(@Valid @RequestBody WriteOffRelateParams params) {
        //check
        Long conId;
        WriteOffTypeEnum type = null;
        if (params == null || (conId = NumericUtils.toLong(params.getContractId())) == null
                || params.getWriteOffIds() == null || params.getWriteOffIds().isEmpty()
                || (type = EnumUtils.findByCode(WriteOffTypeEnum.values(), params.getWriteOffType())) == null) {
            log.error("[WriteOff] params is invalid, params:{}", params);
            return ResponseObject.badArgumentValue();
        }
        ContractVO contractVO = contractService.queryById(conId);
        if (contractVO == null) {
            log.error("[WriteOff] contract is not exist, :{}", params.getContractId());
            return ResponseObject.badArgumentValue();
        }
        log.info("[WriteOff] relation to contract, params:{}", params);
        List<Long> writeOffIds = Lists.newArrayListWithCapacity(params.getWriteOffIds().size());
        for (String writeOffId : params.getWriteOffIds()) {
            Long tmpWriteOffId = null;
            if ((tmpWriteOffId = NumericUtils.toLong(writeOffId)) == null) {
                log.error("[WriteOff] writeOffId is invalid :{}", writeOffId);
                return ResponseObject.badArgumentValue();
            }
            writeOffIds.add(tmpWriteOffId);
        }
        writeOffService.relationContract(conId, contractVO.getContractNo(), writeOffIds, type);
        return ResponseObject.success();
    }

    @ApiOperation("取消关联核销记录到合同")
    @PostMapping("/unRelationToContract")
    public ResponseObject unRelationToContract(@Valid @RequestBody WriteOffRelateParams params) {
        Long conId = null;
        WriteOffTypeEnum type = null;
        if (params == null || (conId = NumericUtils.toLong(params.getContractId())) == null
                || params.getWriteOffIds() == null || params.getWriteOffIds().isEmpty()
                || (type = EnumUtils.findByCode(WriteOffTypeEnum.values(), params.getWriteOffType())) == null) {
            log.error("[WriteOff] params is invalid, params:{}", params);
            return ResponseObject.badArgumentValue();
        }
        ContractVO contractVO = contractService.queryById(conId);
        if (contractVO == null) {
            log.error("[WriteOff] contract is not exist, :{}", params.getContractId());
            return ResponseObject.badArgumentValue();
        }
        log.info("[WriteOff] unrelation to contract, params:{}", params);
        List<Long> writeOffIds = Lists.newArrayListWithCapacity(params.getWriteOffIds().size());
        for (String writeOffId : params.getWriteOffIds()) {
            Long tmpWriteOffId = null;
            if ((tmpWriteOffId = NumericUtils.toLong(writeOffId)) == null) {
                log.error("[WriteOff] writeOffId is invalid :{}", writeOffId);
                return ResponseObject.badArgumentValue();
            }
            writeOffIds.add(tmpWriteOffId);
        }
        writeOffService.unRelationContract(conId, contractVO.getContractNo(), writeOffIds, type);
        return ResponseObject.success();
    }

    @ApiOperation("删除核销记录")
    @DeleteMapping("/delete/{writeOffId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "writeOffId", value = "核销关联ID", type = "String", required = true)
    )
    public ResponseObject<Void> delete(@PathVariable("writeOffId") String writeOffId) {
        Long writeOffIdLong = null;
        if (Strings.isNullOrEmpty(writeOffId)
                || (writeOffIdLong = NumericUtils.toLong(writeOffId)) == null) {
            log.error("[WriteOff] writeoffId is invalid id:{}.", writeOffId);
            return ResponseObject.badArgumentValue();
        }
        try {
            log.info("[WriteOff] delete writeOff, Id:{}", writeOffIdLong);
            writeOffService.deleteWriteOffById(writeOffIdLong);
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[WriteOff] delete writeOff fail, Exception:{}", ex);
            return ResponseObject.success();
        }
    }

    @ApiOperation("增加核销结算记录")
    @PostMapping("/createWriteOff")
    public ResponseObject<Void> add(@Valid @RequestBody WriteOffVO writeOff) {
        if (writeOff == null
                || Strings.isNullOrEmpty(writeOff.getCustomId())
                || Strings.isNullOrEmpty(writeOff.getCustomName())
                || Strings.isNullOrEmpty(writeOff.getActualPayDate())
                || NumericUtils.toDouble(writeOff.getActualPayAmount()) == null) {
            log.error("[WriteOff] create fail, input param is invalid, input:{}.", writeOff);
            return ResponseObject.badArgumentValue();
        }
        try {
            writeOffService.createWriteOff(writeOff);
            return ResponseObject.success();
        } catch (BaseException ex) {
            log.error("[WriteOff] delete writeOff fail, Exception:{}", ex);
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        }
    }

    @ApiOperation("导出核销记录")
    @GetMapping("/export/{contractId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractId", value = "合同ID", type = "String", required = true),
            @ApiImplicitParam(name = "type", value = "核销类型0-硬件，1-服务", type = "String", required = true)
    })
    public void export(HttpServletResponse response,
                       @PathVariable(value = "contractId") String contractId,
                       @RequestParam(value = "type") String type) {

        //TODO
    }


    @ApiOperation("通过合同编号查询服务费核销结算记录")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同ID", type = "String", required = true)
    )
    @GetMapping("/queryCommissionByContractId/{contractId}")
    public ResponseObject<List<CommissionFeeSettlementVO>> queryCommissionByContractId(@PathVariable("contractId") String contractId) {

        //TODO
        return null;
    }

}
