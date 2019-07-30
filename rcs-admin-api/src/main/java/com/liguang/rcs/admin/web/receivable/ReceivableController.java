package com.liguang.rcs.admin.web.receivable;

import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.db.domain.UnAppliedCashEntity;
import com.liguang.rcs.admin.service.ReceivableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "应收管理API")
@RequestMapping("/rcs/receivable")
@RestController
@Slf4j
public class ReceivableController {

    @Autowired
    private ReceivableService receivableService;

    @PostMapping("/queryCustomHw")
    @ApiOperation("查询客户硬件分期")
    public ResponseObject<CustomHwOutput> queryCustomHw(@Valid @RequestBody CustomQueryConditionParam param) {
        //TODO //check 参数校验
        try {
            return ResponseObject.success(receivableService.queryCustomHw(param));
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
            entity.setWriteOffType(WriteOffTypeEnum.HARDWARE);
            receivableService.saveUnAppliedCash(entity);
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/queryCustomCommission")
    @ApiOperation("查询客户服务费应收")
    public ResponseObject<CustomCommissionOutput> queryCustomCommission(@Valid @RequestBody CustomQueryConditionParam param) {
        try {
            return ResponseObject.success(receivableService.queryCustomCommission(param));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/updateCustomCommission")
    @ApiOperation("更新客户服务费手动核实到账")
    public ResponseObject<Void> updateCustomCommission(@Valid @RequestBody CustomReceivableVO customCommissionReceivableVO) {
        try {
            UnAppliedCashEntity entity = customCommissionReceivableVO.toEntity();
            entity.setWriteOffType(WriteOffTypeEnum.SERVICE);
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
            return ResponseObject.success(receivableService.queryReceivableDetail(param, WriteOffTypeEnum.HARDWARE));
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
            return ResponseObject.success(receivableService.queryReceivableSummary(param, WriteOffTypeEnum.HARDWARE));
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
            return ResponseObject.success(receivableService.queryReceivableDetail(param, WriteOffTypeEnum.SERVICE));
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
            return ResponseObject.success(receivableService.queryReceivableSummary(param, WriteOffTypeEnum.SERVICE));
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
