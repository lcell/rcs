package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
}
