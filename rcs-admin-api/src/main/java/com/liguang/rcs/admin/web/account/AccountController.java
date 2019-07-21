package com.liguang.rcs.admin.web.account;

import com.liguang.rcs.admin.common.response.ResponseObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public ResponseObject<Void> modify(@Valid @RequestBody Account input) {
        //TODO
        return  ResponseObject.success(null);
    }

    @GetMapping("/query")
    @ApiOperation("查询信息")
    public ResponseObject<Account> query(@RequestParam(name = "accountId") String accountId) {

        //TODO
        return  ResponseObject.success(null);

    }

}
