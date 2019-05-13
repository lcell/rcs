package com.liguang.rcs.admin.web.auth;

import com.liguang.rcs.admin.permission.Permission;
import com.liguang.rcs.admin.permission.PermissionUtil;
import com.liguang.rcs.admin.util.ResponseUtil;
import com.liguang.rcs.db.domain.RcsUser;
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
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
@RequestMapping("admin/auth")
@Api(tags = "用户鉴权API")
@Validated
public class AdminAuthController {
    private static final Log LOG = LogFactory.getLog(AdminAuthController.class);

    @Autowired
    private RcsRoleService roleService;
    @Autowired
    private RcsPermissionService permissionService;

    @Autowired
    private ApplicationContext context;
    private HashMap<String, String> systemPermissionsMap = null;
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

    @PostMapping("/logout")
    @ApiOperation(value = "用户登出")
    public Object logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return ResponseUtil.ok();
    }

    @GetMapping("/info")
    @ApiOperation(value = "查询当前用户信息")
    public Object info() {
        Subject currentUser = SecurityUtils.getSubject();
        RcsUser user = (RcsUser) currentUser.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getUsername());
        data.put("avatar", user.getAvatar());

        Integer[] roleIds = user.getRoleIds();
        Set<String> roles = roleService.queryByIds(roleIds);
        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
        data.put("roles", roles);
        // NOTE
        // 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
        data.put("perms", toAPI(permissions));
        return ResponseUtil.ok(data);
    }

    //返回当前用户的权限值
    private Object toAPI(Set<String> permissions) {
        if (systemPermissionsMap == null) {
            systemPermissionsMap = new HashMap<>();
            final String basicPackage = "com.liguang.rcs.admin";
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
