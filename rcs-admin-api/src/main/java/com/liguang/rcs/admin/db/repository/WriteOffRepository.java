package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.WriteOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface WriteOffRepository extends JpaRepository<WriteOffEntity, Long> {

    @Query(value = "update rcs_write_off set ref_contract_id = ?1, ref_contract_no = ?2, type = ?3 " +
            " where id in (?4)", nativeQuery = true)
    void relationContract(Long contractId, String contractNo, String code, List<Long> writeOffIds);


    @Query(value = "update rcs_write_off set ref_contract_id = null, ref_contract_no = null, type = null " +
            " where id in (?4) and ref_contract_id = ?1 and ref_contract_no = ?2 and type = ?3", nativeQuery = true)
    void unrelationContract(Long contractId, String contractNo, String code, List<Long> writeOffIds);

    @Query(value = "select * from rcs_write_off " +
            "where custom_id = ?1 and payment_date >= ?2 order by payment_date", nativeQuery = true)
    List<WriteOffEntity> queryByCustomAndEffectTime(String customId, Timestamp effectTime);
}
