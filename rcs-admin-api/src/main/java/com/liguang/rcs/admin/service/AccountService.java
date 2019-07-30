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
        //TODO TEst

        AccountEntity entity = new AccountEntity();
        entity.setId(1L);
        entity.setTeamId(1L);
        return entity;//accountRepository.findByAccountNo(salesNo);
    }
}
