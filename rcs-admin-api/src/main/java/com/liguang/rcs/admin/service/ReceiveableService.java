package com.liguang.rcs.admin.service;

import com.google.common.collect.Lists;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.web.contract.QueryParams;
import com.liguang.rcs.admin.web.receivable.CustomCommissionOutput;
import com.liguang.rcs.admin.web.receivable.CustomHwOutput;
import com.liguang.rcs.admin.web.receivable.CustomQueryConditionParam;
import com.liguang.rcs.admin.web.writeoff.CommissionFeeSettlementVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ReceiveableService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private WriteOffService writeOffService;

    /**
     * 1. 根据条件查询出所有的合同数据
     * 2. 根据每个合同查询核销记录数据
     * 3. 生成对应的报表统计数据
     */
    public CustomHwOutput queryCustomHw(CustomQueryConditionParam param) throws BaseException {
        CustomHwOutput output = new CustomHwOutput();
        QueryParams queryContractParam = buildContractParams(param);
        List<ContractEntity> contractList = contractService.queryAll(queryContractParam);
        if (contractList == null || contractList.isEmpty()) {
            log.error("[Receivable] contract is not exist, params:{}", param);
            return output;
        }
        List<CustomHwOutput> outputs = Lists.newArrayListWithCapacity(contractList.size());
        for(ContractEntity contract: contractList) {
            output.setContractId(contract.getId().toString());
            output.setContractNo(contract.getContractNo());
            buildHwOutput(contract, output);
            break;
            //outputs.add(output);
        }
        return output;
    }

    /**
     * 硬件分期 模版
     *
     */
    private void buildHwOutput(ContractEntity contract, CustomHwOutput output) throws BaseException {
        List<WriteOffSettlementVO> settlementList = writeOffService.querySettlement(contract); //按照时间升序排
        //TODO
        if (settlementList == null || settlementList.isEmpty()) {
            log.warn("[Invoice] there is no settlement record, contract:{}", contract.getId());
            return;
        }
        for (WriteOffSettlementVO vo : settlementList) {

        }


    }

    public CustomCommissionOutput queryCustomCommission(CustomQueryConditionParam param) throws BaseException {
        QueryParams queryContractParam = buildContractParams(param);
        CustomCommissionOutput output = new CustomCommissionOutput();
        List<ContractEntity> contractList = contractService.queryAll(queryContractParam);
        if (contractList == null || contractList.isEmpty()) {
            log.error("[Receivable] contract is not exist, params:{}", param);
            return output;
        }
        for(ContractEntity contract: contractList) {
            output.setContractId(contract.getId().toString());
            output.setContractNo(contract.getContractNo());
            buildCommissionOutput(contract, output);
            break;
        }
        return output;
    }

    private void buildCommissionOutput(ContractEntity contract, CustomCommissionOutput output) throws BaseException{
        List<CommissionFeeSettlementVO> commissionVOS = writeOffService.queryCommissionByContractId(contract);
        //TODO 完善流程
    }


    private QueryParams buildContractParams(CustomQueryConditionParam params) {
        QueryParams queryParams = new QueryParams();
        //TODO

        return queryParams;

    }
}
