package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

   List<InvoiceEntity> findByCustomIdAndBillingDateGreaterThanEqualOrderByBillingDateDesc(String customId, Timestamp billingDate);

   @Query(value = "update rcs_invoice set contract_id = ?1 , contract_no = ?2, write_off_type = ?3 " +
           "where id in (?4)", nativeQuery = true)
    void relationToContract(long contractId, String contractNo, String writeOffType, List<Long> invoiceIds);

    @Query(value = "update rcs_invoice set contract_id = null , contract_no = null, write_off_type = null " +
            "where id in (?4) and contract_id = ?1 and contract_no = ?2 and write_off_type = ?3", nativeQuery = true)
    void unRelationToContract(long contractId, String contractNo, String code, List<Long> invoiceIds);

    List<InvoiceEntity> findBycontractIdAndwriteOffTypeOrderByBillingDateDesc(Long contractId, WriteOffTypeEnum type);
}
