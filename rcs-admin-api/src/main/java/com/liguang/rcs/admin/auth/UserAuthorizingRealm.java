package com.liguang.rcs.admin.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAuthorizingRealm extends AuthorizingRealm {


    private static final Logger log = LoggerFactory.getLogger(UserAuthorizingRealm.class);
//    @Autowired
//    private RcsUserService userService;
//    @Autowired
//    private RcsRoleService roleService;
//    @Autowired
//    private RcsPermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

//
//
//        RcsUser user = (RcsUser) getAvailablePrincipal(principals);
//        Integer[] roleIds = user.getRoleIds();
//        Set<String> roles = roleService.queryByIds(roleIds);
//        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setRoles(roles);
//        info.setStringPermissions(permissions);
        return null;//info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

//        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
//        String username = upToken.getUsername();
//        String password=new String(upToken.getPassword());
//
//        if (StringUtils.isEmpty(username)) {
//            throw new AccountException("用户名不能为空");
//        }
//        if (StringUtils.isEmpty(password)) {
//            throw new AccountException("密码不能为空");
//        }
//
//        List<RcsUser> adminList = userService.findUser(username);
//        Assert.state(adminList.size() < 2, "同一个用户名存在两个账户");
//        if (adminList.size() == 0) {
//            throw new UnknownAccountException("找不到用户（"+username+"）的帐号信息");
//        }
//        RcsUser user = adminList.get(0);
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        if (!encoder.matches(password, user.getPassword())) {
//            throw new UnknownAccountException("找不到用户（"+username+"）的帐号信息");
//        }

        return new SimpleAuthenticationInfo();//SimpleAuthenticationInfo(user,password,getName());
    }


}
