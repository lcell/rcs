package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    List<InvoiceEntity> findByCustomIdAndBillingDateGreaterThanEqualOrderByBillingDateDesc(String customId, Timestamp billingDate);

    @Modifying
    @Query(value = "update rcs_invoice set contract_id = ?1 " +
            " where id in (?2)", nativeQuery = true)
    int relationToContract(long contractId, List<Long> invoiceIds);

    @Modifying
    @Query(value = "update rcs_invoice set contract_id = null  " +
            " where id in (?2) and contract_id = ?1 ", nativeQuery = true)
    void unRelationToContract(long contractId, List<Long> invoiceIds);

    List<InvoiceEntity> findByContractIdOrderByBillingDate(Long contractId);
}
