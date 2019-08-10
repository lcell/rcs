package com.liguang.rcs.admin.web.account;

import com.google.common.base.Strings;
import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.service.AccountService;
import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.web.team.AddToTeamParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
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
    public ResponseObject<Void> changePassword(@Valid @RequestBody ChangePasswdParams input) {
        if (input == null || Strings.isNullOrEmpty(input.getNewPasswd())
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
        if (Strings.isNullOrEmpty(input.getId())
                || NumericUtils.toLong(input.getId()) == null) {
            log.error("[Account] accountId can't be empty:{}", input);
            return ResponseObject.badArgumentValue();
        }
        try {

            accountService.updateInfo(input);
            return ResponseObject.success();
        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("[Account] unExpect Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @PostMapping("/create")
    @ApiOperation("创建账户")
    public ResponseObject<Void> create(@Valid @RequestBody AccountVO input) {
        try {
            accountService.save(input);
            return ResponseObject.success();
        } catch (Exception ex) {
            log.error("[Account] unExpect Exception:{}", ex);
            return ResponseObject.serious();
        }
    }

    @GetMapping("/query/{accountId}")
    @ApiOperation("查询信息")
    public ResponseObject<AccountVO> query(@PathVariable(name = "accountId") String accountId) {

        if (Strings.isNullOrEmpty(accountId)
                || NumericUtils.toLong(accountId) == null) {
            log.warn("[Account] accountId is valid, id:{}.", accountId);
            return ResponseObject.badArgumentValue();
        }
        try {
            return ResponseObject.success(accountService.findById(NumericUtils.toLong(accountId)));
        } catch (BaseException ex) {
            return ResponseObject.fail(ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            log.error("[Account] query err, Exception:{}", ex);
            return ResponseObject.serious();
        }

    }

    @GetMapping("/queryByName")
    @ApiOperation("根据账户名称前置模糊查询账号")
    public ResponseObject<List<AccountVO>> queryByAccountName(@RequestParam(name = "accountName") String accountName) {

        if (Strings.isNullOrEmpty(accountName)) {
            log.warn("[Account] accountName can't ");
            return ResponseObject.badArgumentValue("姓名不可为空");
        }
        return ResponseObject.success(accountService.queryByAccountName(accountName));
    }


    @PostMapping("/queryList")
    @ApiOperation("查询账户列表")
    public ResponseObject<PageableBody<AccountVO>> queryList() {
        //TODO
        return null;
    }
}
