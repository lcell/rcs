package com.liguang.rcs.db.service;

import com.google.common.collect.Sets;
import com.liguang.rcs.db.dao.RcsRoleMapper;
import com.liguang.rcs.db.domain.RcsRole;
import com.liguang.rcs.db.domain.RcsRoleExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class RcsRoleService {
    @Resource
    private RcsRoleMapper mapper;

    public Set<String> queryByIds(Integer[] roleIds) {

        Set<String> roles = Sets.newHashSet();
        if(roleIds.length == 0){
            return roles;
        }
        RcsRoleExample example = new RcsRoleExample();
        example.or().andIdIn(Arrays.asList(roleIds)).andEnabledEqualTo(true).andDeletedEqualTo(false);
        List<RcsRole> roleList = mapper.selectByExample(example);

        for(RcsRole role : roleList){
            roles.add(role.getName());
        }

        return roles;


    }
}
