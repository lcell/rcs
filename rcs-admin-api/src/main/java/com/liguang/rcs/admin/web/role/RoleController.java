package com.liguang.rcs.admin.web.role;

import com.liguang.rcs.admin.permission.PermVo;
import com.liguang.rcs.admin.permission.Permission;
import com.liguang.rcs.admin.permission.PermissionUtil;
import com.liguang.rcs.admin.permission.RequiresPermissionsDesc;
import com.liguang.rcs.admin.util.JacksonUtil;
import com.liguang.rcs.admin.util.ResponseUtil;
import com.liguang.rcs.admin.validator.Order;
import com.liguang.rcs.admin.validator.Sort;
import com.liguang.rcs.db.domain.RcsPermission;
import com.liguang.rcs.db.domain.RcsRole;
import com.liguang.rcs.db.domain.RcsUser;
import com.liguang.rcs.db.service.RcsPermissionService;
import com.liguang.rcs.db.service.RcsRoleService;
import com.liguang.rcs.db.service.RcsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.liguang.rcs.admin.util.ResponseCode.*;

@RestController
@RequestMapping("/admin/role")
@Validated
@Api(value = "角色管理API")
public class RoleController {
    @Autowired
    private RcsRoleService roleService;
    @Autowired
    private RcsPermissionService permissionService;
    @Autowired
    private RcsUserService userService;

    @Autowired
    private ApplicationContext context;
    private List<PermVo> systemPermissions = null;
    private Set<String> systemPermissionsString = null;

    @RequiresPermissions("admin:role:list")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色查询")
    @GetMapping("/list")
    @ApiOperation(value = "查询角色列表")
    public Object list(String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<RcsRole> roleList = roleService.querySelective(name, page, limit, sort, order);
        return ResponseUtil.okList(roleList);
    }

    @GetMapping("/options")
    @ApiIgnore
    public Object options(){
        List<RcsRole> roleList = roleService.queryAll();

        List<Map<String, Object>> options = new ArrayList<>(roleList.size());
        for (RcsRole role : roleList) {
            Map<String, Object> option = new HashMap<>(2);
            option.put("value", role.getId());
            option.put("label", role.getName());
            options.add(option);
        }

        return ResponseUtil.okList(options);
    }

    @RequiresPermissions("admin:role:read")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色详情")
    @GetMapping("/read")
    @ApiOperation(value = "查询角色详情")
    public Object read(@NotNull Integer id) {
        RcsRole role = roleService.findById(id);
        return ResponseUtil.ok(role);
    }


    private Object validate(RcsRole role) {
        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

    @RequiresPermissions("admin:role:create")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色添加")
    @PostMapping("/create")
    @ApiOperation(value = "添加角色")
    public Object create(@RequestBody RcsRole role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }

        if (roleService.checkExist(role.getName())){
            return ResponseUtil.fail(ROLE_NAME_EXIST, "角色已经存在");
        }

        roleService.add(role);

        return ResponseUtil.ok(role);
    }

    @RequiresPermissions("admin:role:update")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色编辑")
    @PostMapping("/update")
    @ApiOperation(value = "更新角色")
    public Object update(@RequestBody RcsRole role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }

        roleService.updateById(role);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:role:delete")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色删除")
    @PostMapping("/delete")
    @ApiOperation(value = "删除角色")
    public Object delete(@RequestBody RcsRole role) {
        Integer id = role.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        // 如果当前角色所对应管理员仍存在，则拒绝删除角色。
        List<RcsUser> adminList = userService.all();
        for(RcsUser admin : adminList){
            Integer[] roleIds = admin.getRoleIds();
            for(Integer roleId : roleIds){
                if(id.equals(roleId)){
                    return ResponseUtil.fail(ROLE_USER_EXIST, "当前角色存在管理员，不能删除");
                }
            }
        }

        roleService.deleteById(id);
        return ResponseUtil.ok();
    }



    private List<PermVo> getSystemPermissions(){
        final String basicPackage = "com.liguang.rcs.admin";
        if(systemPermissions == null){
            List<Permission> permissions = PermissionUtil.listPermission(context, basicPackage);
            systemPermissions = PermissionUtil.listPermVo(permissions);
            systemPermissionsString = PermissionUtil.listPermissionString(permissions);
        }
        return systemPermissions;
    }

    private Set<String> getAssignedPermissions(Integer roleId){
        // 这里需要注意的是，如果存在超级权限*，那么这里需要转化成当前所有系统权限。
        // 之所以这么做，是因为前端不能识别超级权限，所以这里需要转换一下。
        Set<String> assignedPermissions = null;
        if(permissionService.checkSuperPermission(roleId)){
            getSystemPermissions();
            assignedPermissions = systemPermissionsString;
        }
        else{
            assignedPermissions = permissionService.queryByRoleId(roleId);
        }

        return assignedPermissions;
    }

    /**
     * 管理员的权限情况
     *
     * @return 系统所有权限列表和管理员已分配权限
     */
    @RequiresPermissions("admin:role:permission:get")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="权限详情")
    @GetMapping("/permissions")
    @ApiOperation(value = "查询权限详情")
    public Object getPermissions(Integer roleId) {
        List<PermVo> systemPermissions = getSystemPermissions();
        Set<String> assignedPermissions = getAssignedPermissions(roleId);

        Map<String, Object> data = new HashMap<>();
        data.put("systemPermissions", systemPermissions);
        data.put("assignedPermissions", assignedPermissions);
        return ResponseUtil.ok(data);
    }


    /**
     * 更新管理员的权限
     *
     * @param body
     * @return
     */
    @RequiresPermissions("admin:role:permission:update")
    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="权限变更")
    @PostMapping("/permissions")
    @ApiOperation(value = "更新权限")
    @Transactional
    public Object updatePermissions(@RequestBody String body) {
        Integer roleId = JacksonUtil.parseInteger(body, "roleId");
        List<String> permissions = JacksonUtil.parseStringList(body, "permissions");
        if(roleId == null || permissions == null){
            return ResponseUtil.badArgument();
        }

        // 如果修改的角色是超级权限，则拒绝修改。
        if(permissionService.checkSuperPermission(roleId)){
            return ResponseUtil.fail(ROLE_SUPER_SUPERMISSION, "当前角色的超级权限不能变更");
        }

        // 先删除旧的权限，再更新新的权限
        permissionService.deleteByRoleId(roleId);
        for(String permission : permissions){
            RcsPermission litemallPermission = new RcsPermission();
            litemallPermission.setRoleId(roleId);
            litemallPermission.setPermission(permission);
            permissionService.add(litemallPermission);
        }
        return ResponseUtil.ok();
    }

}
