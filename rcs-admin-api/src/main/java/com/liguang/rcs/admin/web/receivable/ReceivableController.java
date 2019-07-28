package com.liguang.rcs.admin.web.receivable;

import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.service.ContractService;
import com.liguang.rcs.admin.service.ReceiveableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    private ReceiveableService receiveableService;

    @PostMapping("/queryCustomHw")
    @ApiOperation("查询客户硬件分期")
    public ResponseObject<CustomHwOutput> queryCustomHw(@Valid @RequestBody CustomQueryConditionParam param) {
        //TODO //check 参数校验
        try {
            return ResponseObject.success(receiveableService.queryCustomHw(param));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }
    @PostMapping("/updateCustomHw")
    @ApiOperation("更新客户硬件分期")
    public ResponseObject<List<CustomHWReceivableVO>> updateCustomHw(@Valid @RequestBody CustomHWReceivableVO customHWReceivableVO) {
        //TODO
        return null;
    }

    @PostMapping("/queryCustomCommission")
    @ApiOperation("查询客户服务费应收")
    public ResponseObject<CustomCommissionOutput> queryCustomCommission(@Valid @RequestBody CustomQueryConditionParam param) {
        try {
            return ResponseObject.success(receiveableService.queryCustomCommission(param));
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/updateCustomCommission")
    @ApiOperation("更新客户服务费手动核实到账")
    public ResponseObject<Void> updateCustomCommission(@Valid @RequestBody CustomCommissionReceivableVO customCommissionReceivableVO) {
        return null;
    }


    @PostMapping("/queryHwDetail")
    @ApiOperation("查询硬件明细")
    public ResponseObject<List<HardwareDetailVO>> queryHwDetail(@Valid @RequestBody QuerySummaryParam param) {
        //TODO
        return null;
    }

    @PostMapping("/queryHwSummary")
    @ApiOperation("查询硬件汇总")
    public ResponseObject<List<HardwareSummaryVO>> queryHwSummary(@Valid @RequestBody QuerySummaryParam param) {
        //TODO
        return null;
    }

    @PostMapping("/queryServiceDetail")
    @ApiOperation("查询服务明细")
    public ResponseObject<List<ServiceDetailVO>> queryServiceDetail(@Valid @RequestBody QuerySummaryParam param) {

        //TODO
        return null;
    }

    @PostMapping("/queryServiceSummary")
    @ApiOperation("查询服务汇总")
    public ResponseObject<List<ServiceSummaryVO>> queryServiceSummary(@Valid @RequestBody QuerySummaryParam param) {

        //TODO
        return null;
    }


}
