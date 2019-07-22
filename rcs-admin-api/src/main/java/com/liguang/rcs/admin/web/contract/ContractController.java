package com.liguang.rcs.admin.web.contract;

import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.common.response.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/rcs/contract")
@RestController
@Api(tags = "合同管理API")
public class ContractController  {
    @PostMapping("/query")
    @ApiOperation("查询合同列表")
    public ResponseObject<PageableBody<ContractVO>> query(@RequestBody QueryParams params) {
        //TODO
        return null;
    }

    @GetMapping("/queryById/{contractId}")
    @ApiOperation("根据合同ID查询合同信息")
    public ResponseObject<ContractVO> query(@PathVariable("contractId") String contractId) {
        //TODO
        return null;
    }

    @PostMapping("/create")
    @ApiOperation("创建合同")
    public ResponseObject<Void> create(@Valid @ModelAttribute ContractVO contract) {
        //TODO
        return null;
    }

    @ApiOperation("修改合同")
    @PostMapping("/modify")
    public ResponseObject<Void> modify(@Valid @ModelAttribute ContractVO contract) {
        //TODO
        return null;
    }

    @ApiOperation("查看合同文件")
    @GetMapping("/getContractFile/{id}")
    public ResponseObject getContractFile(@RequestParam("id") String id) {
        //TODO
        return null;
    }

}
