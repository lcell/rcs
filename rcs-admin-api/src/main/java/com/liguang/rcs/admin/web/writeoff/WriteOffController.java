package com.liguang.rcs.admin.web.writeoff;

import com.liguang.rcs.admin.common.response.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "核销管理API")
@RestController
@RequestMapping("/rcs/writeoff")
public class WriteOffController {


    @ApiOperation("通过合同编号查询核销结算记录")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同编号", type = "String", required = true)
    )
    @GetMapping("/queryByContractId/{contractId}")
    public ResponseObject<WriteOffStatementsVO> queryByContractId(@PathVariable("contractId") String contractId) {

        //TODO
        return null;
    }

    @ApiOperation("删除关联核销记录")
    @DeleteMapping("/delete/{writeOffId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name ="writeOffId" ,value = "合同编号", type = "String", required = true)
    )
    public ResponseObject<Void> delete(@PathVariable("writeOffId") String writeOffId) {
        //TODO
        return null;
    }

    @ApiOperation("更新核销结算记录")
    @PostMapping("/update")
    public ResponseObject<Void> update(@Valid  @RequestBody  WriteOffStatementsVO writeOffStatementsVo) {

        //TODO
        return null;
    }

    @ApiOperation("导出核销记录")
    @GetMapping("/export/{contractId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同编号", type = "String", required = true)
    )
    public ResponseObject export(@PathVariable(value = "contractId") String contractId) {

        //TODO
        return null;
    }


    @ApiOperation("通过合同编号查询服务费核销结算记录")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同编号", type = "String", required = true)
    )
    @GetMapping("/queryCommissionByContractId/{contractId}")
    public ResponseObject<CommissionFeeStatementVO> queryCommissionByContractId(@PathVariable("contractId") String contractId) {

        //TODO
        return null;
    }

    @ApiOperation("删除关联服务费核销记录")
    @DeleteMapping("/deleteCommission/{writeOffId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name ="writeOffId" ,value = "合同编号", type = "String", required = true)
    )
    public ResponseObject<Void> deleteCommission(@PathVariable("writeOffId") String writeOffId) {
        //TODO
        return null;
    }

    @ApiOperation("更新服务费核销结算记录")
    @PostMapping("/updateCommission")
    public ResponseObject<Void> updateCommission(@Valid  @RequestBody  CommissionFeeStatementVO writeOffStatementsVo) {

        //TODO
        return null;
    }


    @ApiOperation("导出服务费核销记录")
    @GetMapping("/exportCommission/{contractId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同编号", type = "String", required = true)
    )
    public ResponseObject exportCommission(@PathVariable(value = "contractId") String contractId) {

        //TODO
        return null;
    }


}
