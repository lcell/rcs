package com.liguang.rcs.admin.web.account;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rcs/account")
@Validated
@Api(tags = "用户管理API")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/changePassword")
    @ApiOperation("修改密码")
    public ResponseObject<Void> changePassword(@Valid @RequestBody ChangePasswdInput input) {
        if (input == null ||Strings.isNullOrEmpty(input.getNewPasswd())
                || Strings.isNullOrEmpty(input.getPasswd())) {
            log.error("[Account] input is invalid.");
            return ResponseObject.badArgumentValue();
        }
        try {
            accountService.changePasswd(input.getNewPasswd(), input.getPasswd());
            return ResponseObject.success();
        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("[Account] change password fail, exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/modify")
    @ApiOperation("修改账户信息") //不可以修改密码等信息
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

    @GetMapping("/queryByName")
    @ApiOperation("根据账户名称精确模糊查询账号")
    public ResponseObject<List<AccountVO>> queryByAccountName(@RequestParam(name = "accountNo") String accountNo) {
        //TODO
        return null;
    }

}
