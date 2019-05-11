package com.liguang.rcs.admin.web;

import com.liguang.rcs.admin.util.ResponseUtil;
import com.liguang.rcs.db.service.RcsPermissionService;
import com.liguang.rcs.db.service.RcsRoleService;
import com.liguang.rcs.db.service.RcsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.liguang.rcs.admin.util.ResponseCode.USER_INVALID_ACCOUNT;

@RestController
@RequestMapping("admin/auth")
@Api(tags = "用户API")
@Validated
public class AdminAuthController {
    private static final Log LOG = LogFactory.getLog(AdminAuthController.class);

    @Autowired
    private RcsUserService userService;
    @Autowired
    private RcsRoleService roleService;
    @Autowired
    private RcsPermissionService permissionService;

    /*
     *  { username : value, password : value }
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Object login(@RequestBody AuthLoginInput input) {

        if (StringUtils.isEmpty(input.getUsername()) || StringUtils.isEmpty(input.getPassword())) {
            return ResponseUtil.badArgument();
        }
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(new UsernamePasswordToken(input.getUsername(), input.getPassword()));
        } catch (UnknownAccountException uae) {
            return ResponseUtil.fail(USER_INVALID_ACCOUNT, "用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            return ResponseUtil.fail(USER_INVALID_ACCOUNT, "用户帐号已锁定不可用");

        } catch (AuthenticationException ae) {
            return ResponseUtil.fail(USER_INVALID_ACCOUNT, "认证失败");
        }
        currentUser = SecurityUtils.getSubject();
        return ResponseUtil.ok(currentUser.getSession().getId());
    }

    @GetMapping("/401")
    @ApiIgnore
    public Object page401() {
        return ResponseUtil.unlogin();
    }

    @GetMapping("/index")
    @ApiIgnore
    public Object pageIndex() {
        return ResponseUtil.ok();
    }

    @GetMapping("/403")
    @ApiIgnore
    public Object page403() {
        return ResponseUtil.unauthz();
    }

}
