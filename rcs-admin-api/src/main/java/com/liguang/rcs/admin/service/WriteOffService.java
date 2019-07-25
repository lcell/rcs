package com.liguang.rcs.admin.service;

import com.google.common.collect.Multimap;
import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import com.liguang.rcs.admin.db.domain.WriteOffEntity;
import com.liguang.rcs.admin.db.repository.WriteOffRepository;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WriteOffService {

    @Autowired
    private WriteOffRepository writeOffRepository;

    @Autowired
    private InvoiceService invoiceService;


    public void relationContract(Long contractId, String contractNo, List<Long> writeOffIds, WriteOffTypeEnum type) {
        writeOffRepository.relationContract(contractId, contractNo, type.getCode(), writeOffIds);
    }

    public void unRelationContract(Long contractId, String contractNo, List<Long> writeOffIds, WriteOffTypeEnum type) {
        writeOffRepository.unrelationContract(contractId, contractNo, type.getCode(), writeOffIds);
    }

    public void deleteWriteOffById(Long writeOffIdLong) {
        writeOffRepository.deleteById(writeOffIdLong);
    }

    public void createWriteOff(WriteOffVO writeOff) throws BaseException {
        WriteOffEntity entity = writeOff.toEntity();
        if (entity == null) {
            throw new BaseException(ResponseCode.SYS_INNER_ERR);
        }
        writeOffRepository.save(entity);
    }

    public List<WriteOffVO> queryWriteOffRecord(String customId, Timestamp effectTime) {
        List<WriteOffEntity> entityList = writeOffRepository.queryByCustomAndEffectTime(customId, effectTime);
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        return entityList.stream().map(WriteOffVO::buildFrom).collect(Collectors.toList());
    }

    public List<WriteOffSettlementVO> querySettlement(ContractEntity contract, WriteOffTypeEnum type) throws BaseException {
        //生效日期首付和期数
        if (contract.getEffectiveDate() == null || contract.getFirstPayment() == null
                || contract.getReceivableNum() == null) {
            log.error("[WriteOff] contract is invalid, contract:{}.", contract);
            throw new BaseException(ResponseCode.SYS_INNER_ERR);
        }
        Timestamp effectiveDate = contract.getEffectiveDate();
        double firstPayment = contract.getFirstPayment();
        int receivableNum = contract.getReceivableNum();
        //已经根据开票时间进行排序
        Multimap<String, InvoiceEntity> invoiceMap = invoiceService.queryRelatedMap(contract.getId(), type);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(effectiveDate.getTime());
        double periodPayment = 0; // 每期应收的累计
        for (int i = 0 ; i <= receivableNum; i++) {

        }

        return null;
    }
    //计算出每期应收的差值
    private double calcPeriodPayment(Date date, Multimap<String, InvoiceEntity> invoiceMap) {
        //DateUtils.toString(da)
        return 0;
    }


    /**
     * 计算每期的应收
     * 首付月 --- 当月发票 - 首付 / 期数
     * 其他期   ----- 当月发票 / （期数 - 档期 + 1） 算上本期 + 当前的分期值
     *
     */


    /**
     * 计算每期实际到账
     * 从前往后计算
     */

}
