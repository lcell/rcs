package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository  extends JpaRepository<ContractEntity, Long> {
}
