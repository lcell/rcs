package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    @Query(value = "select * from rcs_invoice " +
            "where custom_id= ?1 and billing_date >= ?2 order by billing_date desc", nativeQuery = true)

    List<InvoiceEntity> findByCustomAndBillingDate(String customId, Timestamp billingDate);


    @Query(value = "select * from rcs_invoice " +
            "where custom_id= ?1 order by billing_date desc", nativeQuery = true)
    List<InvoiceEntity> findByCustom(String customId);

    @Modifying
    @Query(value = "update rcs_invoice set contract_id = ?1 " +
            " where id in (?2)", nativeQuery = true)
    int relationToContract(long contractId, List<Long> invoiceIds);

    @Modifying
    @Query(value = "update rcs_invoice set contract_id = null  " +
            " where id in (?2) and contract_id = ?1 ", nativeQuery = true)
    void unRelationToContract(long contractId, List<Long> invoiceIds);

    List<InvoiceEntity> findByContractIdOrderByBillingDate(Long contractId);

    @Query(value = "select count(*) from rcs_invoice where contract_id in (?1)", nativeQuery = true)
    int countByContractIds(List<Long> contractIdList);
}
