package com.liguang.rcs.admin.web.receivable;

import com.liguang.rcs.admin.common.response.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
public class ReceivableController {

    @PostMapping("/queryCustomHw")
    @ApiOperation("查询客户硬件分期")
    public ResponseObject<List<CustomHWReceivable>> queryCustomHw(@Valid @RequestBody CustomQueryConditionParam param) {
        //TODO
        return null;
    }
    @PostMapping("/updateCustomHw/{contractId}")
    @ApiOperation("更新客户硬件分期")
    public ResponseObject<List<CustomHWReceivable>> queryCustomHw(@Valid @RequestBody CustomQueryConditionParam param,
                                                                  @PathVariable("contractId") String contractId) {
        //TODO
        return null;
    }

    @PostMapping("/queryCustomCommission")
    @ApiOperation("查询客户服务费分期")
    public ResponseObject<List<CustomHWReceivable>> queryCustomCommission(@Valid @RequestBody CustomQueryConditionParam param) {
        return null;
    }

    @PostMapping("/updateCustomCommission/{contractId}")
    @ApiOperation("更新客户服务费分期")
    public ResponseObject<Void> updateCustomCommission(@Valid @RequestBody CustomHWReceivable customHWReceivable,
                                                       @PathVariable("contractId") String contractId) {
        return null;
    }


    @PostMapping("/queryHwDetail")
    @ApiOperation("查询硬件明细")
    public ResponseObject<List<HardwareDetail>> queryHwDetail(@Valid @RequestBody QuerySummaryParam param) {
        //TODO
        return null;
    }

    @PostMapping("/queryHwSummary")
    @ApiOperation("查询硬件汇总")
    public ResponseObject<List<HardwareSummary>> queryHwSummary(@Valid @RequestBody QuerySummaryParam param) {
        //TODO
        return null;
    }

    @PostMapping("/queryServiceDetail")
    @ApiOperation("查询服务明细")
    public ResponseObject<List<ServiceDetail>> queryServiceDetail(@Valid @RequestBody QuerySummaryParam param) {

        //TODO
        return null;
    }

    @PostMapping("/queryServiceSummary")
    @ApiOperation("查询服务汇总")
    public ResponseObject<List<Serviceummary>> queryServiceSummary(@Valid @RequestBody QuerySummaryParam param) {

        //TODO
        return null;
    }


}
