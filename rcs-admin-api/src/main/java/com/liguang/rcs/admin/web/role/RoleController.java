package com.liguang.rcs.admin.web.role;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/role")
@Validated
@Api(tags = "角色管理API")
public class RoleController {
//    @Autowired
//    private RcsRoleService roleService;
//    @Autowired
//    private RcsPermissionService permissionService;
//    @Autowired
//    private RcsUserService userService;

//    @Autowired
//    private ApplicationContext context;
//    private List<PermVo> systemPermissions = null;
//    private Set<String> systemPermissionsString = null;
//
//    @RequiresPermissions("admin:role:list")
//    @RequiresPermissionsDesc(menu={"系统管理" , "角色管理"}, button="角色查询")
//    @GetMapping("/list")
//    @ApiOperation(value = "查询角色列表")
//    public Object list(String name,
//                       @RequestParam(defaultValue = "1") Integer page,
//                       @RequestParam(defaultValue = "10") Integer limit,
//                       @Sort @RequestParam(defaultValue = "add_time") String sort,
//                       @Order @RequestParam(defaultValue = "desc") String order) {
//        List<RcsRole> roleList = roleService.querySelective(name, page, limit, sort, order);
//        return ResponseUtil.okList(null);
//    }

}
