package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    List<InvoiceEntity> findByCustomIdAndBillingDateGreaterThanEqualOrderByBillingDateDesc(String customId, Timestamp billingDate);

    @Modifying
    @Query(value = "update rcs_invoice set contract_id = ?1 , write_off_type = ?2 " +
            "where id in (?3)", nativeQuery = true)
    int relationToContract(long contractId, String writeOffType, List<Long> invoiceIds);

    @Modifying
    @Query(value = "update rcs_invoice set contract_id = null , write_off_type = null " +
            "where id in (?3) and contract_id = ?1  and write_off_type = ?2", nativeQuery = true)
    void unRelationToContract(long contractId, String code, List<Long> invoiceIds);

    List<InvoiceEntity> findByContractIdAndWriteOffTypeOrderByBillingDate(Long contractId, WriteOffTypeEnum type);
}
