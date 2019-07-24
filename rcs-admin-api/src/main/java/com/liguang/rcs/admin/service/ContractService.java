package com.liguang.rcs.admin.service;

import com.liguang.rcs.admin.common.response.PageableBody;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.db.repository.ContractRepository;
import com.liguang.rcs.admin.web.contract.ContractVO;
import com.liguang.rcs.admin.web.contract.QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    public ContractVO queryById(long contractId) {
        ContractEntity entity = queryEntityById(contractId);
        if (entity == null) {
            return null;
        }
        return new ContractVO(entity);
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
        ContractEntity entity = contract.toEntity();
        entity.setFilePath(UUID.randomUUID().toString());;
        contractRepository.save(entity);
        //save file
        MultipartFile file = contract.getFile();
        File outputFile = new File("/contract/" + entity.getFilePath() + "/" + file.getOriginalFilename());
        if (outputFile.exists()) {
            throw new RuntimeException("Inner Err");
        } else {
            outputFile.getParentFile().mkdirs();
        }
        file.transferTo(outputFile);
    }

    public PageableBody<ContractVO> query(QueryParams params) {
        //查询合同列表
        return null;


    }
}
