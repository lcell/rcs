package com.liguang.rcs.admin.service;

import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.exception.CopyPropertiesFailException;
import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.db.domain.AccountEntity;
import com.liguang.rcs.admin.db.domain.TeamEntity;
import com.liguang.rcs.admin.db.repository.AccountRepository;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.util.CollectionUtils;
import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.util.bcrypt.BCryptPasswordEncoder;
import com.liguang.rcs.admin.web.account.AccountVO;
import com.liguang.rcs.admin.web.team.TeamVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity queryByNo(String salesNo) {
        AccountEntity account = accountRepository.findByAccountNo(salesNo);
        if (account == null) {
            AccountEntity entity = new AccountEntity();
            entity.setId(1L);
            entity.setTeamId(1L);
            return entity;//
        }
        return account;
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

    @Transactional
    public void updateInfo(@Valid AccountVO input) throws BaseException, CopyPropertiesFailException {
        //查询账户信息
        Optional<AccountEntity> accountEntity = accountRepository.findById(NumericUtils.toLong(input.getId()));
        if (accountEntity == null || !accountEntity.isPresent()) {
            log.warn("[Account] account not exist, accountId:{}", input.getId());
            throw new BaseException(ResponseCode.NOT_EXIST);
        }
        AccountEntity entity = accountEntity.get();
        BeanUtils.copyProperties(input, entity);
        accountRepository.save(entity);
    }

    public void save(AccountVO account) throws BaseException {

        AccountEntity entity = account.toEntity();
        if (entity == null) {
            throw new BaseException(ResponseCode.SYS_INNER_ERR);
        }

        accountRepository.save(entity);

    }

    public List<AccountVO> queryByAccountName(String accountName) {

        List<AccountEntity> entityList = accountRepository.findByNameStartingWith(accountName);
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream().map(AccountVO::buildFrom).collect(Collectors.toList());

    }

    public AccountVO findById(Long accountId) throws BaseException {
        Optional<AccountEntity> entity = accountRepository.findById(accountId);
        if (entity.isPresent()) {
            return AccountVO.buildFrom(entity.get());
        }
        throw new BaseException(ResponseCode.NOT_EXIST);
    }

    public void addToTeam(TeamEntity team, List<Long> accountIds) {
        if (team == null || CollectionUtils.isEmpty(accountIds)) {
            return;
        }
        accountRepository.updateTeamInfo(team.getId(), team.getName(), accountIds);
    }

    public void addToDepartment(TeamEntity department, List<Long> accountIds) {
        if (department == null || CollectionUtils.isEmpty(accountIds)) {
            return;
        }
        accountRepository.updateDepartmentInfo(department.getId(), department.getName(), accountIds);
    }

    public PageableBody<AccountVO> queryByTeamId(Long teamId, int currentPage, int pageSize) {
        AccountEntity entity = new AccountEntity();
        entity.setTeamId(teamId);
        Example<AccountEntity> example = Example.of(entity);
        Pageable pagable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
        return PageableBody.buildFrom(accountRepository.findAll(example, pagable), AccountVO::buildFrom);
    }

    public void deleteTeamInfo(Long teamId) {
        if (teamId == null) {
            return ;
        }
        accountRepository.deleteTeamInfo(teamId);
    }

    public void deleteDepartmentInfo(Long departmentId) {
        if (departmentId == null) {
            return;
        }
        accountRepository.deleteDepartmentInfo(departmentId);
    }
}
