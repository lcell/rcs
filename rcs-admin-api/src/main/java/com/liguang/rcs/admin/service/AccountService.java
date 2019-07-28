package com.liguang.rcs.admin.service;

import com.liguang.rcs.admin.db.domain.AccountEntity;
import com.liguang.rcs.admin.db.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity queryByNo(String salesNo) {
        return accountRepository.findByAccountNo(salesNo);
    }
}
