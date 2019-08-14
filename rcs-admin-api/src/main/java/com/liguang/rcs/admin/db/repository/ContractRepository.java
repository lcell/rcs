package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface ContractRepository extends JpaRepository<ContractEntity, Long>,
        JpaSpecificationExecutor<ContractEntity> {

    @Modifying
    @Query(value = "UPDATE  rcs_contract SET effective_date = ?1 where id = ?2", nativeQuery = true)
    void updateffectTime(Timestamp effectTime, Long id);
}
