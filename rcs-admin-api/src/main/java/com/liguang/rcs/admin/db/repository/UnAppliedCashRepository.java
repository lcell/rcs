package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.UnAppliedCashEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface UnAppliedCashRepository extends JpaRepository<UnAppliedCashEntity, Long> {

    UnAppliedCashEntity findByRefContractIdAndCreateDateAfter(Long refContractId, Timestamp timestamp);

    List<UnAppliedCashEntity> findByRefContractIdInAndCreateDateAfter(List<Long> contractIds, Timestamp timestamp);

}
