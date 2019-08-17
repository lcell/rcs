package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface ContractRepository extends JpaRepository<ContractEntity, Long>,
        JpaSpecificationExecutor<ContractEntity> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE  rcs_contract SET effective_date = ?1 where id = ?2", nativeQuery = true)
    void updateffectTime(Timestamp effectTime, Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM rcs_contract WHERE id in (?1)", nativeQuery = true)
    void deleteByIds(List<Long> contractIds);
}
