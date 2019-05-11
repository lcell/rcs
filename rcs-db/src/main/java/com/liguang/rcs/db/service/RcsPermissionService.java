package com.liguang.rcs.db.service;

import com.google.common.collect.Sets;
import com.liguang.rcs.db.dao.RcsPermissionMapper;
import com.liguang.rcs.db.dao.RcsRoleMapper;
import com.liguang.rcs.db.domain.RcsPermission;
import com.liguang.rcs.db.domain.RcsPermissionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
@Service
public class RcsPermissionService {

    @Resource
    private RcsPermissionMapper permMapper;

    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = Sets.newHashSet();
        if(roleIds.length == 0){
            return permissions;
        }
        RcsPermissionExample example = new RcsPermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<RcsPermission> permissionList = permMapper.selectByExample(example);

        for(RcsPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }
        return permissions;
    }
}
