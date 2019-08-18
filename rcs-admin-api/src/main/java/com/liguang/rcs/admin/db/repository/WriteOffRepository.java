package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.WriteOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface WriteOffRepository extends JpaRepository<WriteOffEntity, Long> {
    @Transactional
    @Modifying
    @Query(value = "update rcs_write_off " +
            " set ref_contract_id = ?1, settlement_id = ?2" +
            " where id in (?3)", nativeQuery = true)
    void relationContract(Long contractId, String settlementId, List<Long> writeOffIds);

    @Transactional
    @Modifying
    @Query(value = "update rcs_write_off " +
            " set ref_contract_id = null, settlement_id = null " +
            " where id in (?3) and ref_contract_id = ?1  and settlement_id = ?2 ", nativeQuery = true)
    void unRelationContract(Long contractId, String settlementId, List<Long> writeOffIds);

    @Transactional
    @Modifying
    @Query(value = "update rcs_write_off " +
            "set ref_contract_id = null, settlement_id = null " +
            " where ref_contract_id = ?1 and settlement_id = ?2 ", nativeQuery = true)
    void unAllRelationContract(Long contractId, String settlementId);

    @Query(value = "select * from rcs_write_off " +
            "where custom_id = ?1 and payment_date >= ?2  order by payment_date DESC ", nativeQuery = true)
    List<WriteOffEntity> queryByCustomAndEffectTime(String customId, Timestamp effectTime);


    @Query(value = "select * from rcs_write_off " +
            "where custom_id = ?1 order by payment_date DESC ", nativeQuery = true)
    List<WriteOffEntity> queryByCustom(String customId);

    List<WriteOffEntity> findByRefContractId(Long contractId);

    @Query(value = "select count(*) from rcs_write_off where ref_contract_id in (?1)", nativeQuery = true)
    int countByContractIds(List<Long> contractIdList);
}
