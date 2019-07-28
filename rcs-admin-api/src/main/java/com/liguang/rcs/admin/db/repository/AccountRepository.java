package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByAccountNo(String salesNo);
}
