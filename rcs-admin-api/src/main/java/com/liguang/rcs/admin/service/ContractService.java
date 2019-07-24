package com.liguang.rcs.admin.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.db.repository.ContractRepository;
import com.liguang.rcs.admin.web.contract.ContractVO;
import com.liguang.rcs.admin.web.contract.QueryParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createContract(ContractVO contract) throws Exception {
        //save file
        MultipartFile file = contract.getFile();
        if (file == null) {
            log.warn("[Contract] file is null, please upload contract file.");
            throw new RuntimeException("File is empty");
        }
        ContractEntity entity = contract.toEntity();
        entity.setFilePath(UUID.randomUUID().toString() + "/" + file.getOriginalFilename());;
        contractRepository.save(entity);
        File outputFile = new File(CONTRACT__PREFIX_FILE_PATH + entity.getFilePath());
        if (outputFile.exists()) {
            throw new RuntimeException("Inner Err");
        } else {
            outputFile.getParentFile().mkdirs();
        }
        file.transferTo(outputFile);
    }

    public PageableBody<ContractVO> query(final QueryParams params) {
        Sort order = Sort.by(Sort.Order.asc("createDate"));
        Pageable pageable = PageRequest.of(params.getCurrentPage() - 1, params.getPageSize(), order);
        Page<ContractEntity> page = contractRepository.findAll((Specification<ContractEntity>) (root, criteriaQuery, cb) -> {
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
        }, pageable);

        PageableBody<ContractVO> pageBody = new PageableBody<>();
        pageBody.setCurrentPage(params.getCurrentPage());
        pageBody.setPageSize(params.getPageSize());
        pageBody.setDataList(page.getContent().stream().map(ContractVO::buildFrom).collect(Collectors.toList()));
        pageBody.setTotalSize((int)page.getTotalElements());
        return pageBody;


    }

    public ResponseEntity<?> downloadFile(long contractId) throws IOException {
        ContractEntity entity = this.queryEntityById(contractId);
        if (entity == null) {
            log.error("[Contract] contract not exist, contractID:{}", contractId);
            return ResponseEntity.notFound().build();
        }

        File outputFile = new File(CONTRACT__PREFIX_FILE_PATH + entity.getFilePath());
        if (!outputFile.exists()) {
            log.error("[Contract] contract file not exist, filePath:{}", entity.getFilePath());
            return ResponseEntity.notFound().build();
        }
        FileSystemResource file = new FileSystemResource(outputFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }
}
