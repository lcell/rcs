package com.liguang.rcs.admin.service;

import com.liguang.rcs.admin.db.domain.AccountEntity;
import com.liguang.rcs.admin.db.repository.AccountRepository;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.util.bcrypt.BCryptPasswordEncoder;
import com.liguang.rcs.admin.web.account.AccountVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    @Transactional
    public void changePasswd(String newPasswd, String passwd) throws BaseException {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof AccountVO) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            passwd = encoder.encode(passwd);
            newPasswd = encoder.encode(newPasswd);
            Long accountId = ((AccountEntity) principal).getId();
            int num = accountRepository.changePasswd(accountId, passwd, newPasswd);
            if (num == 0) {
                throw new BaseException(ResponseCode.USER_INVALID_PASSWORD);
            }
            ((AccountEntity) principal).setPasswd(newPasswd);
        }
        throw new BaseException(ResponseCode.UN_LOGIN);

    }
}
