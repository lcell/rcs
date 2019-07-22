package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
}
