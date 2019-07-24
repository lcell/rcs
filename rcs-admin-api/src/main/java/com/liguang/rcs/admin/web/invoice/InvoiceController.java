package com.liguang.rcs.admin.web.invoice;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.common.Constant;
import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.service.InvoiceService;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.EnumUtils;
import com.liguang.rcs.admin.util.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/rcs/invoice")
@Api(tags = "发票管理API")
@RestController
@Slf4j
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @ApiOperation("根据客户ID和合同生效时间查询发票列表")
    @GetMapping("/queryByCustomIdAndEffectTime")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "customId", type = "String", value = "客户ID", required = true),
                    @ApiImplicitParam(name = "effectDate", type = "String", value = "合同生效时间,格式yyyy-MM-dd HH:mm:SS", required = true)
            }
    )
    public ResponseObject<List<InvoiceVO>> queryByCustomIdAndEffectTime(
            @RequestParam(value = "customId") String customId,
            @RequestParam(value = "effectDate") String effectDate) {
        if (Strings.isNullOrEmpty(customId) || Strings.isNullOrEmpty(effectDate)) {
            log.error("[Invoice] customId or effectDate is empty.");
            return ResponseObject.badArgumentValue();
        }
        try {
            Timestamp timestamp = DateUtils.toTimestamp(effectDate, "yyyy-MM-dd HH:mm:SS");
            return ResponseObject.success(invoiceService.queryByCustomAndEffectDate(customId, timestamp));
        } catch (Exception ex) {
            log.error("[Invoice] query fail, Exception:{}", ex);
            return ResponseObject.badArgumentValue();
        }
    }


    @ApiOperation("根据客户编号进行同步发票")
    @PostMapping("/sync/{customId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "customId", value = "客户编号", type = "String")
    )
    public ResponseObject<Void> sync(@PathVariable String customId) {

        //TODO
        return null;
    }
    @ApiOperation("将发票关联到合同上")
    @PostMapping("/relationToContract")
    public ResponseObject<Void> relationToContract(@Valid @RequestBody RelationToContractVO input) {
        //check
        WriteOffTypeEnum writeOffType = null;
        if (input == null
                || Strings.isNullOrEmpty(input.getContractId())
                || input.getInvoiceIds() == null || input.getInvoiceIds().isEmpty()
                || (writeOffType = EnumUtils.findByCode(WriteOffTypeEnum.values(), input.getType())) == null) {
            log.error("[Invoice] input is invalid, input:{}", input);
            return ResponseObject.badArgumentValue();
        }
        try {
            List<Long> invoiceIds = input.getInvoiceIds().stream().map(Long::parseLong).collect(Collectors.toList());
            invoiceService.relationToContract(Long.parseLong(input.getContractId()), invoiceIds, writeOffType);
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[Invoice] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

}
