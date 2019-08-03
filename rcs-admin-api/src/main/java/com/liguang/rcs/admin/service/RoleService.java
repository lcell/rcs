package com.liguang.rcs.admin.service;

import com.google.common.collect.Sets;
import com.liguang.rcs.admin.db.domain.RoleEntity;
import com.liguang.rcs.admin.db.repository.RoleRepository;
import com.liguang.rcs.admin.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Set<String> queryByIds(List<Long> roleIds) {
        List<RoleEntity> roleList = roleRepository.findAllById(roleIds);
        if (CollectionUtils.isEmpty(roleList)) {
            return Collections.emptySet();
        }
        return Sets.newHashSet(roleList.stream().map(RoleEntity::getName).collect(Collectors.toList()));
    }
}
