package com.liguang.rcs.db.service;

import com.google.common.collect.Sets;
import com.liguang.rcs.db.dao.RcsPermissionMapper;
import com.liguang.rcs.db.domain.RcsPermission;
import com.liguang.rcs.db.domain.RcsPermissionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class RcsPermissionService {

    @Resource
    private RcsPermissionMapper mapper;

    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = Sets.newHashSet();
        if(roleIds.length == 0){
            return permissions;
        }
        RcsPermissionExample example = new RcsPermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<RcsPermission> permissionList = mapper.selectByExample(example);

        for(RcsPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }
        return permissions;
    }


    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }

        RcsPermissionExample example = new RcsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        List<RcsPermission> permissionList = mapper.selectByExample(example);

        for(RcsPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }

        RcsPermissionExample example = new RcsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return mapper.countByExample(example) != 0;
    }

    public void deleteByRoleId(Integer roleId) {
        RcsPermissionExample example = new RcsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        mapper.logicalDeleteByExample(example);
    }

    public void add(RcsPermission litemallPermission) {
        litemallPermission.setAddTime(LocalDateTime.now());
        litemallPermission.setUpdateTime(LocalDateTime.now());
        mapper.insertSelective(litemallPermission);
    }
}
