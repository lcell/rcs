package com.liguang.rcs.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.liguang.rcs.db.dao.RcsRoleMapper;
import com.liguang.rcs.db.domain.RcsRole;
import com.liguang.rcs.db.domain.RcsRoleExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

    public List<RcsRole> querySelective(String name, Integer page, Integer limit, String sort, String order) {
        RcsRoleExample example = new RcsRoleExample();
        RcsRoleExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return mapper.selectByExample(example);
    }

    public RcsRole findById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    public void add(RcsRole role) {
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        mapper.insertSelective(role);
    }

    public void deleteById(Integer id) {
        mapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(RcsRole role) {
        role.setUpdateTime(LocalDateTime.now());
        mapper.updateByPrimaryKeySelective(role);
    }

    public boolean checkExist(String name) {
        RcsRoleExample example = new RcsRoleExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false);
        return mapper.countByExample(example) != 0;
    }

    public List<RcsRole> queryAll() {
        RcsRoleExample example = new RcsRoleExample();
        example.or().andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }
}
