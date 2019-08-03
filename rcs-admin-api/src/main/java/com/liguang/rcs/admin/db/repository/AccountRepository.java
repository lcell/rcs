package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByAccountNo(String salesNo);

    @Query(value = "update rcs_account set passwd = ?3 " +
            " where id = ?1 and passwd = ?2", nativeQuery = true)
    int changePasswd(Long accountId, String passwd, String newPasswd);
}
