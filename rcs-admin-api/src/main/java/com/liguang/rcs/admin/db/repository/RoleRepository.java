package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
