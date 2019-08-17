package com.liguang.rcs.admin.web.invoice;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.service.InvoiceService;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.NumericUtils;
import io.swagger.annotations.*;
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
    @GetMapping("/queryInvoice")
    public ResponseObject<List<InvoiceVO>> queryInvoice( @Valid @RequestBody QueryInvoiceParam params) {
        if (params == null
                || Strings.isNullOrEmpty(params.getCustomId())
                || Strings.isNullOrEmpty(params.getContractId())) {
            log.error("[Invoice] customId  is empty.");
            return ResponseObject.badArgumentValue();
        }
        try {
            List<InvoiceVO> invoiceList;
            if (Strings.isNullOrEmpty(params.getEffectDate())) {
                invoiceList = invoiceService.queryByCustomId(params.getCustomId());
            } else {
                Timestamp timestamp = DateUtils.toTimestamp(params.getEffectDate(), "yyyy-MM-dd");
                invoiceList = invoiceService.queryByCustomAndEffectDate(params.getCustomId(), timestamp);
            }

            return ResponseObject.success(invoiceList.stream()
                    .filter(invoiceVO -> Strings.isNullOrEmpty(invoiceVO.getContractId())
                            || invoiceVO.getContractId().equals(params.getContractId()))
            .collect(Collectors.toList()));
        } catch (Exception ex) {
            log.error("[Invoice] query fail, Exception:{}", ex);
            return ResponseObject.badArgumentValue();
        }
    }

    @ApiModelProperty("根据合同编号和类型查询已关联的发票列表")
    @GetMapping("/queryRelatedInvoice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractId", value = "合同ID", type = "String")
    })
    public ResponseObject<List<InvoiceVO>> queryRelatedInvoice(@RequestParam("contractId") String contractId) {
        Long contractIdLong;
        if((contractIdLong = NumericUtils.toLong(contractId)) == null) {
            log.error("[Invoice] param is invalid, contractId:{}", contractId);
            return ResponseObject.badArgumentValue();
        }
        return ResponseObject.success(invoiceService.queryRelatedList(contractIdLong));
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
            invoiceService.relationToContract(vo.checkAndGetContractId(), vo.checkAndGetInvoiceIds());
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
            invoiceService.unRelationToContract(vo.checkAndGetContractId(), vo.checkAndGetInvoiceIds());
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[Invoice] Inner Err, Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

}
