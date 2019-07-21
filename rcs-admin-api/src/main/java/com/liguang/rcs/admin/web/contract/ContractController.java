package com.liguang.rcs.admin.web.contract;

import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.common.response.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
