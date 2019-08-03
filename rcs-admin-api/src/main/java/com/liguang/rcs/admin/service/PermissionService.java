package com.liguang.rcs.admin.service;

import com.google.common.collect.Sets;
import com.liguang.rcs.admin.db.domain.PermissionEntity;
import com.liguang.rcs.admin.db.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;


    public Set<String> queryByRoleIds(@NotNull List<Long> roleIds) {
        List<PermissionEntity> permissionList = permissionRepository.findByRoleIdIn(roleIds);
        if (permissionList == null || permissionList.isEmpty()) {
            return Collections.emptySet();
        }
        return Sets.newHashSet(permissionList.stream()
                .map(PermissionEntity::getPermission)
                .collect(Collectors.toList()));
    }
}
