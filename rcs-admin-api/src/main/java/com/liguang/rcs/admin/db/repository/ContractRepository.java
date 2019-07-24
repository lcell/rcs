package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContractRepository extends JpaRepository<ContractEntity, Long>,
        JpaSpecificationExecutor<ContractEntity> {
}
