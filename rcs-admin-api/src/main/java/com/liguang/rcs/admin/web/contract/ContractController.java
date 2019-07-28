package com.liguang.rcs.admin.web.contract;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.db.domain.AccountEntity;
import com.liguang.rcs.admin.service.AccountService;
import com.liguang.rcs.admin.service.ContractService;
import com.liguang.rcs.admin.util.NumericUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/rcs/contract")
@RestController
@Api(tags = "合同管理API")
@Slf4j
public class ContractController  {

    @Autowired
    private ContractService contractService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/query")
    @ApiOperation("查询合同列表")
    public ResponseObject<PageableBody<ContractVO>> query(@RequestBody QueryParams params) {
        //check
        if (params == null) {
            log.warn("[Contract] params can't be null.");
            return ResponseObject.badArgument();
        }

        return ResponseObject.success(contractService.query(params));
    }

    @GetMapping("/queryById/{contractId}")
    @ApiOperation("根据合同ID查询合同信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "contractId", value = "合同ID", dataType = "String")
    )
    public ResponseObject<ContractVO> query(@PathVariable("contractId") String contractId) {
        Long contractIdLong = null;
        if (Strings.isNullOrEmpty(contractId) || (contractIdLong = NumericUtils.toLong(contractId)) == null) {
            log.warn("[Contract] contract ID can't be empty.");
            return ResponseObject.badArgumentValue();
        }
        ContractVO contractVO = contractService.queryById(contractIdLong);
        if (contractVO == null) {
            log.warn("[Contract] contract not exist, contractId:{}", contractId);
            return ResponseObject.dataNotExist();
        }
        return ResponseObject.success(contractVO);
    }

    @PostMapping("/create")
    @ApiOperation("创建合同")
    public ResponseObject<Void> create(@Valid @RequestBody ContractVO contract) {
        AccountEntity entity = null;
        if (contract == null ||
                (entity = accountService.queryByNo(contract.getSalesNo())) == null) {
            log.error("[Contract] input is invalid, input:{}", contract);
            return ResponseObject.badArgumentValue();
        }
        try {
            contractService.createContract(contract, entity);
            return ResponseObject.success();
        } catch (Exception ex) {
            return ResponseObject.serious();
        }
    }

    @ApiOperation("修改合同")
    @PostMapping("/modify")
    public ResponseObject<Void> modify(@Valid @ModelAttribute ContractVO contract) {
        //TODO
        //check


        return null;
    }

    @ApiOperation("查看合同文件")
    @GetMapping("/getContractFile/{id}")
    public ResponseEntity getContractFile(ServletServerHttpResponse response, @RequestParam("id") String id) {
        return ResponseEntity.badRequest().body("Not Support.");
    }

}
