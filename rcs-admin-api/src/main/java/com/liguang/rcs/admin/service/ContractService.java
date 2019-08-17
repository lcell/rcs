package com.liguang.rcs.admin.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.enumeration.ContractStatusEnum;
import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;
import com.liguang.rcs.admin.common.enumeration.ProductTypeEnum;
import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.db.domain.AccountEntity;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.db.repository.ContractRepository;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.EnumUtils;
import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.web.contract.ContractVO;
import com.liguang.rcs.admin.web.contract.QueryParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    public ContractVO queryById(long contractId) {
        ContractEntity entity = queryEntityById(contractId);
        if (entity == null) {
            return null;
        }
        return ContractVO.buildFrom(entity);
    }

    public ContractEntity queryEntityById(long contractId) {
        Optional<ContractEntity> entity = contractRepository.findById(contractId);
        if (!entity.isPresent()) {
            return null;
        }
        return entity.get();
    }

    public void updateEffectTime(Long id, Timestamp timestamp) {
        if (NumericUtils.isNullOrZero(id)) {
            return;
        }
        contractRepository.updateffectTime(timestamp, id);
    }

    @Transactional
    public void createContract(ContractVO contract, AccountEntity account)  {
        ContractEntity entity = contract.toEntity();
        entity.setSalesId(account.getId());
        entity.setTeamId(account.getTeamId());
        contractRepository.save(entity);
    }

    public List<ContractEntity> queryAll(final QueryParams params) {
        return contractRepository.findAll(buildSpec(params));
    }

    public PageableBody<ContractVO> query(final QueryParams params) {
        Sort order = Sort.by(Sort.Order.asc("createDate"));
        Pageable pageable = PageRequest.of(params.getCurrentPage() - 1, params.getPageSize(), order);
        Page<ContractEntity> page = contractRepository.findAll(buildSpec(params), pageable);
        return PageableBody.buildFrom(page, ContractVO::buildFrom);
    }

    private Specification<ContractEntity> buildSpec(QueryParams params) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> conditions = Lists.newArrayList();
            if (!Strings.isNullOrEmpty(params.getContractNo())) {
                conditions.add(cb.equal(root.get("contractNo"), params.getContractNo()));
            }
            if (!Strings.isNullOrEmpty(params.getType())) {
                conditions.add(cb.equal(root.get("type"),  EnumUtils.findByCode(ContractTypeEnum.values(), params.getType())));
            }
            if (!Strings.isNullOrEmpty(params.getCustomId())) {
                conditions.add(cb.equal(root.get("customId"), params.getCustomId()));
            }
            if (!Strings.isNullOrEmpty(params.getSalesName())) {
                conditions.add(cb.equal(root.get("salesName"), params.getSalesName()));
            }
            if (!Strings.isNullOrEmpty(params.getStatus())) {
                conditions.add(cb.equal(root.get("status"), EnumUtils.findByCode(ContractStatusEnum.values(), params.getStatus())));
            }
            if (!Strings.isNullOrEmpty(params.getProductType())) {
                conditions.add(cb.equal(root.get("productType"), EnumUtils.findByCode(ProductTypeEnum.values(), params.getProductType())));
            }
            if (!Strings.isNullOrEmpty(params.getCustomName())) {
                conditions.add(cb.equal(root.get("customName"), params.getCustomName()));
            }
            if (!Strings.isNullOrEmpty(params.getTeamId()) && NumericUtils.toLong(params.getTeamId()) != null) {
                conditions.add(cb.equal(root.get("teamId"), NumericUtils.toLong(params.getTeamId())));
            }
            if (!Strings.isNullOrEmpty(params.getStartDate()) && !Strings.isNullOrEmpty(params.getEndDate())) {
                conditions.add(cb.between(root.get("effectiveDate"),
                        DateUtils.softToTimestamp(params.getStartDate(), "yyyy-MM-dd"),
                        DateUtils.softToTimestamp(params.getEndDate(), "yyyy-MM-dd")));
            }
            return cb.and(conditions.toArray(new Predicate[conditions.size()]));
        };
    }

    public void deleteByIds(List<Long> contractIds) {
        contractRepository.deleteByIds(contractIds);
    }
}
