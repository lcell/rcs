package com.liguang.rcs.admin.auth;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.db.domain.AccountEntity;
import com.liguang.rcs.admin.service.AccountService;
import com.liguang.rcs.admin.service.PermissionService;
import com.liguang.rcs.admin.service.RoleService;
import com.liguang.rcs.admin.util.CollectionUtils;
import com.liguang.rcs.admin.util.bcrypt.BCryptPasswordEncoder;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UserAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        AccountEntity user = (AccountEntity) getAvailablePrincipal(principals);
        Long[] roleIds = user.getRoleIds();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<Long> roleIdList = Lists.newArrayList(roleIds);
            Set<String> roles = roleService.queryByIds(roleIdList);
            Set<String> permissions = permissionService.queryByRoleIds(roleIdList);
            info.setRoles(roles);
            info.setStringPermissions(permissions);
        } else {
            info.setRoles(Collections.emptySet());
            info.setStringPermissions(Collections.emptySet());
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String accountNo = upToken.getUsername();
        String password = new String(upToken.getPassword());

        if (Strings.isNullOrEmpty(accountNo) || Strings.isNullOrEmpty(password)) {
            throw new AccountException("账号和密码不能为空");
        }

        AccountEntity accountEntity = accountService.queryByNo(accountNo);
        if (accountEntity == null) {
            throw new UnknownAccountException("找不到用户（" + accountNo + "）的帐号信息");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, accountEntity.getPasswd())) {
            throw new UnknownAccountException("账号或者密码输入有误");
        }
        return new SimpleAuthenticationInfo(accountEntity, password, getName());
    }


}
