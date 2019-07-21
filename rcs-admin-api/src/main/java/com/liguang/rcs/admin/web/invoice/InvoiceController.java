package com.liguang.rcs.admin.web.invoice;

import com.liguang.rcs.admin.common.response.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/rcs/invoice")
@Api(tags = "发票管理API")
@RestController
public class InvoiceController {


    @ApiOperation("根据客户查询发票列表")
    @GetMapping ("/queryByCompany/{customId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "customId", value = "客户编号", type = "String")
    )
    public ResponseObject<List<InvoiceVo>> queryByCompany(@PathVariable("companyId") String companyId) {

        //TODO
        return null;
    }

    @ApiOperation("删除发票")
    @PostMapping("/delete/{invoiceId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "invoiceId", value = "发票编号", type = "String")
    )
    /**
     * 只是在发票数据上增加一个状态，并不是真正删除发票记录
     */
    public ResponseObject<Void> delete(@PathVariable("invoiceId") String invoiceId) {
        //TODO
        return null;
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
    public ResponseObject<Void> relationContract(@Valid @RequestBody RelationToContractVo relationToContractVo) {

        //TODO
        return null;
    }

}
