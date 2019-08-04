package com.liguang.rcs.admin.web.receivable;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.db.domain.UnAppliedCashEntity;
import com.liguang.rcs.admin.service.ContractService;
import com.liguang.rcs.admin.service.ReceivableService;
import com.liguang.rcs.admin.util.NumericUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.liguang.rcs.admin.util.NumericUtils.plus;

@Api(tags = "应收管理API")
@RequestMapping("/rcs/receivable")
@RestController
@Slf4j
public class ReceivableController {

    @Autowired
    private ReceivableService receivableService;

    @Autowired
    private ContractService contractService;

    @PostMapping("/queryCustomHw/{contractId}")
    @ApiOperation("根据合同ID查询客户硬件分期")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同ID", type = "String", required = true)
    )
    public ResponseObject<CustomHwOutput> queryCustomHw(@PathVariable("contractId") String contractId) {
        if (Strings.isNullOrEmpty(contractId) || NumericUtils.toLong(contractId) == null) {
            log.error("[Receivable] input contractId is invalid, contractId:{}", contractId);
            return ResponseObject.badArgumentValue();
        }
        try {
            ContractEntity contractEntity = contractService.queryEntityById(NumericUtils.toLong(contractId));
            if (contractEntity == null || contractEntity.getType() != ContractTypeEnum.HARDWARE) {
                log.error("[Receivable] contract is invalid, contract:{}", contractEntity);
                return ResponseObject.dataNotExist();
            }
            return ResponseObject.success(receivableService.queryCustomHw(contractEntity));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }
    @PostMapping("/updateCustomHw")
    @ApiOperation("更新客户硬件分期手动核实到账")
    public ResponseObject<Void> updateCustomHw(@Valid @RequestBody CustomReceivableVO customHWReceivableVO) {
        try {
            UnAppliedCashEntity entity = customHWReceivableVO.toEntity();
            receivableService.saveUnAppliedCash(entity);
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/queryCustomService/{contractId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同ID", type = "String", required = true)
    )
    @ApiOperation("根据合同ID查询客户服务费应收")
    public ResponseObject<CustomCommissionOutput> queryCustomCommission(@PathVariable("contractId") String contractId) {
        if (Strings.isNullOrEmpty(contractId) || NumericUtils.toLong(contractId) == null) {
            log.error("[Receivable] input contractId is invalid, contractId:{}", contractId);
            return ResponseObject.badArgumentValue();
        }
        try {
            ContractEntity contractEntity = contractService.queryEntityById(NumericUtils.toLong(contractId));
            if (contractEntity == null || contractEntity.getType() != ContractTypeEnum.SERVICE) {
                log.error("[Receivable] contract is invalid, contract:{}", contractEntity);
                return ResponseObject.dataNotExist();
            }

            return ResponseObject.success(receivableService.queryCustomCommission(contractEntity));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/updateCustomService")
    @ApiOperation("更新客户服务费手动核实到账")
    public ResponseObject<Void> updateCustomCommission(@Valid @RequestBody CustomReceivableVO vo) {
        try {
            //计算一下1-30的数据, 服务费没有1-30这个分类，因此需要加上
            vo.setDay1_30(plus(vo.getDay1_5(), vo.getDay6_30()));
            UnAppliedCashEntity entity = vo.toEntity();
            receivableService.saveUnAppliedCash(entity);
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }


    @PostMapping("/queryHwDetail")
    @ApiOperation("查询硬件明细")
    public ResponseObject<List<ReceivableDetailVo>> queryHwDetail(@Valid @RequestBody QuerySummaryParam param) {
        //TODO //check 参数校验
        try {
            return ResponseObject.success(receivableService.queryReceivableDetail(param, ContractTypeEnum.HARDWARE));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/queryHwSummary")
    @ApiOperation("查询硬件汇总")
    public ResponseObject<List<ReceivableSummaryVo>> queryHwSummary(@Valid @RequestBody QuerySummaryParam param) {
        //TODO //check 参数校验
        try {
            return ResponseObject.success(receivableService.queryReceivableSummary(param, ContractTypeEnum.HARDWARE));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/queryServiceDetail")
    @ApiOperation("查询服务明细")
    public ResponseObject<List<ReceivableDetailVo>> queryServiceDetail(@Valid @RequestBody QuerySummaryParam param) {
        //TODO //check 参数校验
        try {
            return ResponseObject.success(receivableService.queryReceivableDetail(param, ContractTypeEnum.SERVICE));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/queryServiceSummary")
    @ApiOperation("查询服务汇总")
    public ResponseObject<List<ReceivableSummaryVo>> queryServiceSummary(@Valid @RequestBody QuerySummaryParam param) {
        //TODO //check 参数校验
        try {
            return ResponseObject.success(receivableService.queryReceivableSummary(param, ContractTypeEnum.SERVICE));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/queryHWAndServiceSummary")
    @ApiOperation("查询服务汇总")
    public ResponseObject<List<ReceivableSummaryVo>> queryHWAndServiceSummary(@Valid @RequestBody QuerySummaryParam param) {
        //TODO //check 参数校验
        try {
            return ResponseObject.success(receivableService.queryReceivableSummary(param, null));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }


}
