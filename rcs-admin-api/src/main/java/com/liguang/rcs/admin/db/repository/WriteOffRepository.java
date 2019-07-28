package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.db.domain.WriteOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface WriteOffRepository extends JpaRepository<WriteOffEntity, Long> {

    @Query(value = "update rcs_write_off " +
            " set ref_contract_id = ?1, type = ?2, set settlement_id = ?3" +
            " where id in (?4)", nativeQuery = true)
    void relationContract(Long contractId, String type, String settlementId, List<Long> writeOffIds);


    @Query(value = "update rcs_write_off " +
            " set ref_contract_id = null, type = null , settlement_id = null " +
            " where id in (?4) and ref_contract_id = ?1 and type = ?2 and settlement_id = ?3 ", nativeQuery = true)
    void unRelationContract(Long contractId, String type, String settlementId, List<Long> writeOffIds);


    @Query(value = "update rcs_write_off " +
            "set ref_contract_id = null, type = null , settlement_id = null " +
            " where ref_contract_id = ?1 and type = ?2 and settlement_id = ?3 ", nativeQuery = true)
    void unAllRelationContract(Long contractId, String type, String settlementId);

    @Query(value = "select * from rcs_write_off " +
            "where custom_id = ?1 and payment_date >= ?2 order by payment_date", nativeQuery = true)
    List<WriteOffEntity> queryByCustomAndEffectTime(String customId, Timestamp effectTime);

    List<WriteOffEntity> findByRefContractIdAndType(Long contractId, WriteOffTypeEnum type);
}
