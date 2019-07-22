package com.liguang.rcs.admin.web.account;

import com.liguang.rcs.admin.common.response.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rcs/account")
@Validated
@Api(tags = "用户信息API")
public class AccountController {

    @PostMapping("/changePasswd")

    @ApiOperation("修改密码")
    public ResponseObject<Void> changePasswd(@Valid @RequestBody ChangePasswdInput input) {

        //TODO
        return  ResponseObject.success(null);
    }

    @PostMapping("/modify")
    @ApiOperation("修改账户信息")
    public ResponseObject<Void> modify(@Valid @RequestBody AccountVO input) {
        //TODO
        return  ResponseObject.success(null);
    }

    @GetMapping("/query/{accountId}")
    @ApiOperation("查询信息")
    public ResponseObject<AccountVO> query(@PathVariable(name = "accountId") String accountId) {

        //TODO
        return  ResponseObject.success(null);

    }

    @GetMapping("/queryByNo")
    @ApiOperation("根据账户编号模糊查询账号")
    public ResponseObject<List<AccountVO>> queryByNo(@RequestParam(name = "accountNo") String accountNo) {
        //TODO
        return null;
    }

}
