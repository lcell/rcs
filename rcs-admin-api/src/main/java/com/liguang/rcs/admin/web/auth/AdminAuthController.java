package com.liguang.rcs.admin.web.auth;

import com.liguang.rcs.admin.common.response.ResponseObject;
import com.liguang.rcs.admin.db.domain.AccountEntity;
import com.liguang.rcs.admin.permission.Permission;
import com.liguang.rcs.admin.permission.PermissionUtil;
import com.liguang.rcs.admin.service.PermissionService;
import com.liguang.rcs.admin.service.RoleService;
import com.liguang.rcs.admin.util.ResponseUtil;
import com.liguang.rcs.admin.web.account.AccountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

import static com.liguang.rcs.admin.util.ResponseCode.USER_INVALID_ACCOUNT;

@RestController
@RequestMapping("rcs/auth")
@Api(tags = "用户鉴权管理")
@Validated
public class AdminAuthController {
    private static final Log LOG = LogFactory.getLog(AdminAuthController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ApplicationContext context;
    private HashMap<String, String> systemPermissionsMap = null;
    /*
     *  { username : value, password : value }
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public ResponseObject login(@RequestBody AuthLoginInput input) {

        if (StringUtils.isEmpty(input.getUsername()) || StringUtils.isEmpty(input.getPassword())) {
            return ResponseObject.badArgument();
        }
        Subject currentUser = SecurityUtils.getSubject();
        try {
                currentUser.login(new UsernamePasswordToken(input.getUsername(), input.getPassword()));
        } catch (UnknownAccountException uae) {
            return ResponseObject.fail(USER_INVALID_ACCOUNT);//, "用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            return ResponseObject.fail(USER_INVALID_ACCOUNT);//, "用户帐号已锁定不可用");

        } catch (AuthenticationException ae) {
            return ResponseObject.fail(USER_INVALID_ACCOUNT);//, "认证失败");
        }
        return ResponseObject.success(SecurityUtils.getSubject().getSession().getId());
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户登出")
    public Object logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return ResponseObject.success();
    }

    @GetMapping("/info")
    @ApiOperation(value = "查询当前用户信息")
    public Object info() {
        Subject currentUser = SecurityUtils.getSubject();
        AccountEntity user = (AccountEntity)currentUser.getPrincipal();
        if(user == null) {
            return ResponseObject.unlogin();
        }
        //返回权限信息
        return ResponseUtil.ok(AccountVO.buildFrom(user));
    }

    //返回当前用户的权限值
    private Object toAPI(Set<String> permissions) {
        if (systemPermissionsMap == null) {
            systemPermissionsMap = new HashMap<>();
            final String basicPackage = "com.liguang.rcs.admin.web";
            List<Permission> systemPermissions = PermissionUtil.listPermission(context, basicPackage);
            for (Permission permission : systemPermissions) {
                String perm = permission.getRequiresPermissions().value()[0];
                String api = permission.getApi();
                systemPermissionsMap.put(perm, api);
            }
        }

        Collection<String> apis = new HashSet<>();
        for (String perm : permissions) {
            String api = systemPermissionsMap.get(perm);
            apis.add(api);

            if (perm.equals("*")) {
                apis.clear();
                apis.add("*");
                return apis;
            }
        }
        return apis;

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
