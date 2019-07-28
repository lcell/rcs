package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.UnAppliedCashEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnAppliedCashRepository extends JpaRepository<UnAppliedCashEntity, Long> {
}
