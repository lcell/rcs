package com.liguang.rcs.admin.web.user;

import com.liguang.rcs.admin.permission.RequiresPermissionsDesc;
import com.liguang.rcs.admin.util.RegexUtil;
import com.liguang.rcs.admin.util.ResponseUtil;
import com.liguang.rcs.admin.util.bcrypt.BCryptPasswordEncoder;
import com.liguang.rcs.admin.validator.Order;
import com.liguang.rcs.admin.validator.Sort;
import com.liguang.rcs.db.domain.RcsUser;
import com.liguang.rcs.db.service.RcsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.liguang.rcs.admin.util.ResponseCode.*;

@RestController
@RequestMapping("/admin/admin")
@Validated
@Api(tags = "用户管理API")
public class UserMangerController {
    private static final Log LOG = LogFactory.getLog(UserMangerController.class);

    @Autowired
    private RcsUserService userService;

    @RequiresPermissions("admin:admin:list")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="查询")
    @ApiOperation(value = "查询用户列表")
    @GetMapping("/list")
    public Object list(String username,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<RcsUser> adminList = userService.querySelective(username, page, limit, sort, order);
        return ResponseUtil.okList(adminList);
    }

    private Object validate(RcsUser user) {
        String name = user.getUsername();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isUsername(name)) {
            return ResponseUtil.fail(USER_INVALID_NAME, "管理员名称不符合规定");
        }
        String password = user.getPassword();
        if (StringUtils.isEmpty(password) || password.length() < 6) {
            return ResponseUtil.fail(USER_INVALID_NAME, "管理员密码长度不能小于6");
        }
        return null;
    }

    @RequiresPermissions("admin:admin:create")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="添加")
    @ApiOperation(value = "添加用户")
    @PostMapping("/create")
    public Object create(@RequestBody RcsUser user) {
        Object error = validate(user);
        if (error != null) {
            return error;
        }

        String username = user.getUsername();
        List<RcsUser> adminList = userService.findUser(username);
        if (adminList.size() > 0) {
            return ResponseUtil.fail(USER_NAME_EXIST, "管理员已经存在");
        }

        String rawPassword = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        userService.add(user);
        return ResponseUtil.ok(user);
    }

    @RequiresPermissions("admin:admin:read")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="详情")
    @ApiOperation(value = "查询用户详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        RcsUser admin = userService.findById(id);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:admin:update")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="编辑")
    @ApiOperation(value = "更新用户信息")
    @PostMapping("/update")
    public Object update(@RequestBody RcsUser user) {
        Object error = validate(user);
        if (error != null) {
            return error;
        }

        Integer anotherAdminId = user.getId();
        if (anotherAdminId == null) {
            return ResponseUtil.badArgument();
        }
        // 不允许管理员通过编辑接口修改密码
        user.setPassword(null);
        if (userService.updateById(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(user);
    }

    @RequiresPermissions("admin:admin:delete")
    @RequiresPermissionsDesc(menu={"系统管理" , "用户管理"}, button="删除")
    @ApiOperation(value = "删除用户")
    @PostMapping("/delete")
    public Object delete(@RequestBody RcsUser user) {
        Integer anotherAdminId = user.getId();
        if (anotherAdminId == null) {
            return ResponseUtil.badArgument();
        }

        // 管理员不能删除自身账号
        Subject currentUser = SecurityUtils.getSubject();
        RcsUser currentAdmin = (RcsUser) currentUser.getPrincipal();
        if (currentAdmin.getId().equals(anotherAdminId)) {
            return ResponseUtil.fail(USER_DELETE_NOT_ALLOWED, "管理员不能删除自己账号");
        }

        userService.deleteById(anotherAdminId);
        return ResponseUtil.ok();
    }
}
