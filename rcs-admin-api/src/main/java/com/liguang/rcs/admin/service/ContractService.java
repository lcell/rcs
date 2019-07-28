package com.liguang.rcs.admin.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.db.domain.AccountEntity;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.db.repository.ContractRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContractService {
    private static final String CONTRACT__PREFIX_FILE_PATH = "/contract/";

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

        PageableBody<ContractVO> pageBody = new PageableBody<>();
        pageBody.setCurrentPage(params.getCurrentPage());
        pageBody.setPageSize(params.getPageSize());
        pageBody.setDataList(page.getContent().stream().map(ContractVO::buildFrom).collect(Collectors.toList()));
        pageBody.setTotalSize((int)page.getTotalElements());
        return pageBody;
    }

    private Specification<ContractEntity> buildSpec(QueryParams params) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> conditions = Lists.newArrayList();
            if (!Strings.isNullOrEmpty(params.getContractNo())) {
                conditions.add(cb.equal(root.get("contractNo"), params.getContractNo()));
            }
            if (!Strings.isNullOrEmpty(params.getContractType())) {
                conditions.add(cb.equal(root.get("type"), params.getContractType()));
            }
            if (!Strings.isNullOrEmpty(params.getCustomId())) {
                conditions.add(cb.equal(root.get("customId"), params.getCustomId()));
            }
            if (!Strings.isNullOrEmpty(params.getProductType())) {
                conditions.add(cb.equal(root.get("productType"), params.getProductType()));
            }
            if (!Strings.isNullOrEmpty(params.getStartDate()) && !Strings.isNullOrEmpty(params.getEndDate())) {
                conditions.add(cb.between(root.get("effectiveDate"), params.getStartDate(), params.getEndDate()));
            }

            return cb.and(conditions.toArray(new Predicate[conditions.size()]));
        };
    }
}
