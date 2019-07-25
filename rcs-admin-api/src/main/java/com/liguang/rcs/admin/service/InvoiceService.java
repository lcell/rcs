package com.liguang.rcs.admin.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import com.liguang.rcs.admin.db.repository.InvoiceRepository;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.web.contract.ContractVO;
import com.liguang.rcs.admin.web.invoice.InvoiceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        return entity.stream().map(InvoiceVO :: new).collect(Collectors.toList());
    }

    /**
     * 将发票关联到合同上
     * @param contractId
     * @param invoiceIds
     * @param writeOffType
     */
    public void relationToContract(Long contractId, List<Long> invoiceIds, WriteOffTypeEnum writeOffType) {
        ContractVO contract = contractService.queryById(contractId);
        if (contract == null) {
            //TODO
            throw new RuntimeException("contract not exist");
        }
        invoiceRepository.relationToContract(contractId, contract.getContractNo(), writeOffType.getCode(), invoiceIds);
    }

    public void unRelationToContract(long contractId, List<Long> invoiceIds, WriteOffTypeEnum writeOffType) {
        ContractVO contract = contractService.queryById(contractId);
        if (contract == null) {
            //TODO
            throw new RuntimeException("contract not exist");
        }
        invoiceRepository.unRelationToContract(contractId, contract.getContractNo(), writeOffType.getCode(), invoiceIds);
    }

    public List<InvoiceEntity> queryRelatedEntityList(Long contractId, WriteOffTypeEnum type) {
        List<InvoiceEntity> invoiceEntityLst = invoiceRepository.findByContractIdAndWriteOffTypeOrderByBillingDateDesc(contractId, type);
        if (invoiceEntityLst == null || invoiceEntityLst.isEmpty()) {
            return Collections.emptyList();
        }
        return invoiceEntityLst;
    }

    public List<InvoiceVO> queryRelatedList(Long contractId, WriteOffTypeEnum type) {
        return queryRelatedEntityList(contractId, type).stream().map(InvoiceVO::new).collect(Collectors.toList());
    }

    /**
     * 将发票进行按月整理，并按月整理
     */
    public Multimap<String, InvoiceEntity> queryRelatedMap(Long contractId, WriteOffTypeEnum type) {
        Multimap<String, InvoiceEntity> entityMap = ArrayListMultimap.create();
        List<InvoiceEntity> invoiceLst = queryRelatedEntityList(contractId, type);
        for (InvoiceEntity invoice: invoiceLst) {
            String monthKey = DateUtils.toString(invoice.getBillingDate(), "yyyyMM");
            entityMap.put(monthKey, invoice);
        }
        return entityMap;
    }
}
