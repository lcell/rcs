package com.liguang.rcs.admin.service;

import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import com.liguang.rcs.admin.db.repository.InvoiceRepository;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.web.contract.ContractVO;
import com.liguang.rcs.admin.web.invoice.InvoiceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ContractService contractService;
    /**
     * 根据客户ID和合同生效时间查询发票列表
     * @param customId
     * @param effectDate
     */
    public List<InvoiceVO> queryByCustomAndEffectDate(String customId, Timestamp effectDate) {
        List<InvoiceEntity> entity = invoiceRepository.findByCustomIdAndBillingDateGreaterThanEqualOrderByBillingDateDesc(customId, effectDate);
        return entity.stream().map(InvoiceVO::buildFrom).collect(Collectors.toList());
    }

    /**
     * 将发票关联到合同上
     * @param contractId 合同ID
     * @param invoiceIds 发票ID集合
     */
    @Transactional
    public void relationToContract(Long contractId, List<Long> invoiceIds) throws BaseException {
        ContractVO contract = contractService.queryById(contractId);
        if (contract == null) {
            log.error("[Invoice] contract not exist, contractId:{}", contractId);
            throw new BaseException(ResponseCode.NOT_EXIST);
        }
        invoiceRepository.relationToContract(contractId, invoiceIds);
    }

    @Transactional
    public void unRelationToContract(long contractId, List<Long> invoiceIds) throws BaseException {
        ContractVO contract = contractService.queryById(contractId);
        if (contract == null) {
            log.error("[Invoice] contract not exist, contractId:{}", contractId);
            throw new BaseException(ResponseCode.NOT_EXIST);
        }
        invoiceRepository.unRelationToContract(contractId, invoiceIds);
    }

    public List<InvoiceEntity> queryRelatedEntityList(Long contractId) {
        List<InvoiceEntity> invoiceEntityLst = invoiceRepository.findByContractIdOrderByBillingDate(contractId);
        if (invoiceEntityLst == null || invoiceEntityLst.isEmpty()) {
            return Collections.emptyList();
        }
        return invoiceEntityLst;
    }

    public List<InvoiceVO> queryRelatedList(Long contractId) {
        return queryRelatedEntityList(contractId).stream().map(InvoiceVO::buildFrom).collect(Collectors.toList());
    }

//    /**
//     * 将发票进行按月整理，并按月整理
//     */
//    public Multimap<String, InvoiceEntity> queryRelatedMap(Long contractId) {
//        Multimap<String, InvoiceEntity> entityMap = ArrayListMultimap.create();
//        List<InvoiceEntity> invoiceLst = queryRelatedEntityList(contractId);
//        for (InvoiceEntity invoice: invoiceLst) {
//            String monthKey = DateUtils.toString(invoice.getBillingDate(), "yyyyMM");
//            entityMap.put(monthKey, invoice);
//        }
//        return entityMap;
//    }

    public void saveInvoice(InvoiceEntity toEntity) {
        this.invoiceRepository.save(toEntity);
    }
}
