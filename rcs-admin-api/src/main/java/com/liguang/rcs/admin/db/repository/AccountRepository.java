package com.liguang.rcs.admin.db.repository;

import com.liguang.rcs.admin.db.domain.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByAccountNo(String salesNo);

    @Transactional
    @Modifying
    @Query(value = "update rcs_account set passwd = ?3 " +
            " where id = ?1 and passwd = ?2", nativeQuery = true)
    int changePasswd(Long accountId, String passwd, String newPasswd);

    List<AccountEntity> findByNameStartingWith(String accountName);

    @Transactional
    @Modifying
    @Query(value = " update rcs_account set team_id = ?1 , team_name = ?2 " +
            " where id in (?3)", nativeQuery = true)
    void updateTeamInfo(Long teamId, String teamName, List<Long> accountIds);


    @Transactional
    @Modifying
    @Query(value = " update rcs_account set department_id = ?1 , department = ?2 " +
            " where id in (?3)", nativeQuery = true)
    void updateDepartmentInfo(Long departmentId, String departmentName, List<Long> accountIds);


    @Modifying
    @Transactional
    @Query(value = " UPDATE rcs_account set team_id=null, team_name = null " +
            " where team_id = ?1", nativeQuery = true)
    void deleteTeamInfo(Long teamId);


    @Modifying
    @Transactional
    @Query(value = " UPDATE rcs_account set department_id=null, department = null " +
            " where department_id = ?1", nativeQuery = true)
    void deleteDepartmentInfo(Long departmentId);
}
