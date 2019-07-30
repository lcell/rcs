package com.liguang.rcs.admin.web.invoice;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.service.InvoiceService;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.EnumUtils;
import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.web.contract.ContractVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
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
                    @ApiImplicitParam(name = "effectDate", type = "String", value = "合同生效时间,格式yyyy-MM-dd", required = true)
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
            Timestamp timestamp = DateUtils.toTimestamp(effectDate, "yyyy-MM-dd");
            return ResponseObject.success(invoiceService.queryByCustomAndEffectDate(customId, timestamp));
        } catch (Exception ex) {
            log.error("[Invoice] query fail, Exception:{}", ex);
            return ResponseObject.badArgumentValue();
        }
    }

    @ApiModelProperty("根据合同编号和类型查询已关联的发票列表")
    @GetMapping("/queryRelatedInvoice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractId")
    })
    public ResponseObject<List<InvoiceVO>> queryRelatedInvoice(@RequestParam("contractId") String contractId,
                                                               @RequestParam("type") String type) {
        Long contractIdLong = null;
        WriteOffTypeEnum writeOffType = null;
        if((contractIdLong = NumericUtils.toLong(contractId)) == null
                || (writeOffType = EnumUtils.findByCode(WriteOffTypeEnum.values(), type) ) == null) {
            log.error("[Invoice] param is invalid, contractId:{}, type:{}", contractId, type);
            return ResponseObject.badArgumentValue();
        }
        return ResponseObject.success(invoiceService.queryRelatedList(contractIdLong, writeOffType));
    }

    @ApiOperation("创建或修改发票信息 for Test")
    @PostMapping("/create")
    public ResponseObject create(@Valid @RequestBody InvoiceVO invoiceVO) {
        invoiceService.saveInvoice(invoiceVO.toEntity());
        return ResponseObject.success();
    }

    @ApiOperation("根据客户编号进行同步发票")
    @PostMapping("/sync/{customId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "customId", value = "客户编号", type = "String")
    )
    public ResponseObject<Void> sync(@PathVariable String customId) {

        //TODO
        return ResponseObject.success();
    }
    @ApiOperation("将发票关联到合同上")
    @PostMapping("/relationToContract")
    public ResponseObject<Void> relationToContract(@Valid @RequestBody RelationToContractVO vo) {
        if (vo == null) {
            log.error("[Invoice] input is null.");
            return ResponseObject.badArgumentValue();
        }
        try {
            vo.check();
            invoiceService.relationToContract(vo.checkAndGetContractId(), vo.checkAndGetInvoiceIds(), vo.checkAndGetType());
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[Invoice] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @ApiOperation("取消关联发票到合同上")
    @PostMapping("/unRelationToContract")
    public ResponseObject unRelationToContract(@Valid @RequestBody RelationToContractVO vo) {
        if (vo == null) {
            log.error("[Invoice] input is null.");
            return ResponseObject.badArgumentValue();
        }
        try {
            vo.check();
            invoiceService.unRelationToContract(vo.checkAndGetContractId(), vo.checkAndGetInvoiceIds(), vo.checkAndGetType());
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[Invoice] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

}
