package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
