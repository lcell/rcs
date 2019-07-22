package com.liguang.rcs.admin.web.writeoff;

import com.liguang.rcs.admin.common.response.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "核销管理API")
@RestController
@RequestMapping("/rcs/writeOff")
public class WriteOffController {


    @ApiOperation("通过合同ID查询核销结算记录")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同ID", type = "String", required = true)
    )
    @GetMapping("/queryByContractId/{contractId}")
    public ResponseObject<List<WriteOffStatementsVO>> queryByContractId(@PathVariable("contractId") String contractId) {

        //TODO
        return null;
    }

    @ApiOperation("删除关联核销记录")
    @DeleteMapping("/delete/{writeOffId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "writeOffId", value = "核销关联ID", type = "String", required = true)
    )
    public ResponseObject<Void> delete(@PathVariable("writeOffId") String writeOffId) {
        //TODO
        return null;
    }

    @ApiOperation("增加核销结算记录")
    @PostMapping("/add")
    public ResponseObject<Void> add(@Valid @RequestBody AddWriteOffParam param) {

        //TODO
        return null;
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
    public ResponseObject<List<CommissionFeeStatementVO>> queryCommissionByContractId(@PathVariable("contractId") String contractId) {

        //TODO
        return null;
    }

}
