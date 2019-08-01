package com.liguang.rcs.admin.service;

import com.google.common.collect.Maps;
import com.liguang.rcs.admin.common.Constant;
import com.liguang.rcs.admin.common.enumeration.ActionPlanEnum;
import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
import com.liguang.rcs.admin.common.template.Template;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.db.domain.UnAppliedCashEntity;
import com.liguang.rcs.admin.db.repository.UnAppliedCashRepository;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.template.receivable.custom.CustomHWConverter;
import com.liguang.rcs.admin.template.receivable.custom.CustomHWKeyStrategy;
import com.liguang.rcs.admin.template.receivable.custom.CustomServiceConverter;
import com.liguang.rcs.admin.template.receivable.custom.CustomServiceKeyStrategy;
import com.liguang.rcs.admin.template.receivable.detail.DetailConverter;
import com.liguang.rcs.admin.template.receivable.detail.DetailKeyStrategy;
import com.liguang.rcs.admin.template.receivable.summary.SummaryConverter;
import com.liguang.rcs.admin.template.receivable.summary.SummaryKeyStrategy;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.web.contract.QueryParams;
import com.liguang.rcs.admin.web.receivable.*;
import com.liguang.rcs.admin.web.writeoff.CommissionFeeSettlementVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReceivableService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private WriteOffService writeOffService;

    @Autowired
    private UnAppliedCashRepository unAppliedCashRepository;

    /**
     * 1. 根据条件查询出所有的合同数据
     * 2. 根据每个合同查询核销记录数据
     * 3. 生成对应的报表统计数据
     */
    public CustomHwOutput queryCustomHw(CustomQueryConditionParam param) throws BaseException {
        CustomHwOutput output = new CustomHwOutput();
        ContractEntity contract = findOneContract(param);
        output.setContractId(contract.getId().toString());
        output.setContractNo(contract.getContractNo());
        fillHwOutput(contract, output);
        return output;
    }

    /**
     * 硬件分期 模版
     */
    private void fillHwOutput(ContractEntity contract, CustomHwOutput output) throws BaseException {
        UnAppliedCashEntity entity = queryUnAppliedCash(contract.getId(), WriteOffTypeEnum.HARDWARE);
        List<CustomReceivableVO> hwList = buildHwVos(contract);
        CustomReceivableVO netTotal = fillVoList(hwList, entity);
        output.setHwReceivableVOS(hwList);
        output.setActionPlan(ActionPlanEnum.getActionPlan(netTotal, WriteOffTypeEnum.HARDWARE).getCode());

    }

    private CustomReceivableVO fillVoList(List<CustomReceivableVO> hwList, UnAppliedCashEntity entity) throws BaseException {
        CustomReceivableVO totalPre = hwList.get(hwList.size() - 1);
        if (!Constant.TOTAL_BY_DUE_DATE_RANGE.equals(totalPre.getNPer())) {
            throw new BaseException(ResponseCode.BAD_ARGUMENT);
        }
        CustomReceivableVO vo = new CustomReceivableVO();
        if (entity != null) {
            vo = CustomReceivableVO.buildFrom(entity);
        }
        vo.setNPer(Constant.UN_APPLIED_CASH);
        hwList.add(vo);
        CustomReceivableVO netTotal = buildNetTotal(totalPre, entity != null ? null : vo);
        hwList.add(netTotal);
        return netTotal;
    }

    public List<CustomReceivableVO> buildHwVos(ContractEntity contract) throws BaseException {
        List<WriteOffSettlementVO> settlementList = writeOffService.querySettlement(contract); //按照时间升序排
        CustomHWKeyStrategy keyStrategy = new CustomHWKeyStrategy(getFirstMonth(settlementList));
        CustomHWConverter converter = new CustomHWConverter();
        Template<WriteOffSettlementVO, CustomReceivableVO> template = new Template<>(keyStrategy, converter);
        for(WriteOffSettlementVO vo : settlementList) {
            template.addSingleData(vo);
        }
        return template.buildDataList((key) ->{
            CustomReceivableVO vo = new CustomReceivableVO();
            vo.setNPer(key);
            return vo;
        });
    }

    private Date getFirstMonth(List<WriteOffSettlementVO> settlementList) {
        if (settlementList == null || settlementList.isEmpty()) {
            return null;
        }
        return DateUtils.softToDate(settlementList.get(0).getPayMonth(), "yyyyMM");
    }

    public CustomCommissionOutput queryCustomCommission(CustomQueryConditionParam param) throws BaseException {
        CustomCommissionOutput output = new CustomCommissionOutput();
        ContractEntity contract = findOneContract(param);
        output.setContractId(contract.getId().toString());
        output.setContractNo(contract.getContractNo());
        fillCommissionOutput(contract, output);
        return output;
    }

    private ContractEntity findOneContract(CustomQueryConditionParam param) throws BaseException {
        QueryParams queryContractParam = buildContractParams(param);
        List<ContractEntity> contractList = contractService.queryAll(queryContractParam);
        if (contractList == null || contractList.isEmpty()) {
            log.error("[Receivable] contract is not exist, params:{}", param);
            throw new BaseException(ResponseCode.NOT_EXIST);
        }
        if (contractList.size() > 1) {
            log.warn("[Receivable] contract is more than one, just chose firstOne, contractList:{}", contractList);
        }
        return contractList.get(0);
    }

    private void fillCommissionOutput(ContractEntity contract, CustomCommissionOutput output) throws BaseException{
        UnAppliedCashEntity entity = queryUnAppliedCash(contract.getId(), WriteOffTypeEnum.SERVICE);
        List<CustomReceivableVO> serviceKList = buildServiceVos(contract);
        CustomReceivableVO netTotal = fillVoList(serviceKList, entity);
        output.setCommissionReceivableVOS(serviceKList);
        output.setActionPlan(ActionPlanEnum.getActionPlan(netTotal, WriteOffTypeEnum.SERVICE).getCode());
    }

    private CustomReceivableVO buildNetTotal(CustomReceivableVO totalPre,
                                             CustomReceivableVO unAppliedCash) {
        CustomReceivableVO vo = totalPre.clone();
        vo.setNPer(Constant.NET_TOTAL_BY_DUE_DATE);
        if (unAppliedCash == null) {
            return vo;
        }
        //合集
        vo.minusOther(unAppliedCash);
        return vo;
    }

    public List<CustomReceivableVO> buildServiceVos(ContractEntity contract) throws BaseException {
        List<CommissionFeeSettlementVO> commissionVOS = writeOffService.queryCommissionByContractId(contract);
        CustomServiceKeyStrategy keyStrategy = new CustomServiceKeyStrategy(getFirstDay(commissionVOS));
        CustomServiceConverter converter = new CustomServiceConverter();
        Template<CommissionFeeSettlementVO, CustomReceivableVO> template = new Template<>(keyStrategy, converter);
        for (CommissionFeeSettlementVO vo : commissionVOS) {
            template.addSingleData(vo);
        }
        return template.buildDataList((key) -> {
            CustomReceivableVO vo = new CustomReceivableVO();
            vo.setNPer(key);
            return vo;
        });
    }

    private String getFirstDay(List<CommissionFeeSettlementVO> commissionVOS) {
        if (commissionVOS == null || commissionVOS.isEmpty()) {
            return null;
        }
        return commissionVOS.get(0).getPayDate();
    }

    public UnAppliedCashEntity queryUnAppliedCash(Long contractId, WriteOffTypeEnum writeOffTypeEnum) {
        Timestamp now = DateUtils.softToTimestamp(
                DateUtils.toString(Calendar.getInstance().getTime(), "yyyyMMdd"), "yyyyMMdd");
        return unAppliedCashRepository.findByRefContractIdAndWriteOffTypeAndCreateDateAfter(contractId, writeOffTypeEnum, now);
    }

    public Map<Long, UnAppliedCashEntity> queryUnAppliedCash(List<Long> contractIds, @NotNull WriteOffTypeEnum writeOffTypeEnum) {
        Map<Long, UnAppliedCashEntity> entityMap = Maps.newHashMap();
        Timestamp now = DateUtils.softToTimestamp(
                DateUtils.toString(Calendar.getInstance().getTime(), "yyyyMMdd"), "yyyyMMdd");
        List<UnAppliedCashEntity> lst = unAppliedCashRepository.findByRefContractIdInAndWriteOffTypeAndCreateDateAfter(contractIds, writeOffTypeEnum, now);
        for (UnAppliedCashEntity entity : lst) {
            entityMap.putIfAbsent(entity.getRefContractId(), entity);
        }
        return entityMap;
    }

    private QueryParams buildContractParams(CustomQueryConditionParam params) {
        QueryParams queryParams = new QueryParams();
        //TODO
        queryParams.setContractNo(params.getContractNo());
        queryParams.setCustomId(params.getCustomNo());
        queryParams.setStartDate(params.getBeginDate());
        queryParams.setEndDate(DateUtils.toString(Calendar.getInstance().getTime(), "yyyy-MM-dd"));
        return queryParams;

    }

    public void saveUnAppliedCash(UnAppliedCashEntity entity) {
        unAppliedCashRepository.save(entity);
    }

    public List<ReceivableSummaryVo> queryReceivableSummary(QuerySummaryParam param, @Nullable WriteOffTypeEnum type) throws BaseException {
        List<ContractEntity> contractList = contractService.queryAll(buildContractParams(param));
        SummaryKeyStrategy keyStrategy = new SummaryKeyStrategy();
        SummaryConverter converter = new SummaryConverter();
        Template<CustomReceivableVO, ReceivableSummaryVo> template = new Template<>(keyStrategy, converter);
        if (type == null || type == WriteOffTypeEnum.HARDWARE) {
            fillSummaryTemplate(contractList, template,  WriteOffTypeEnum.HARDWARE);
        }
        if (type == null || type == WriteOffTypeEnum.SERVICE) {
            fillSummaryTemplate(contractList, template,  WriteOffTypeEnum.SERVICE);
        }
        return template.buildDataList(key -> {
            ReceivableSummaryVo vo = new ReceivableSummaryVo();
            vo.setNPer(key);
            return vo;
        });
    }

    private void fillSummaryTemplate(List<ContractEntity> contractList, Template<CustomReceivableVO, ?> template,
                                     @NotNull WriteOffTypeEnum type) throws BaseException {
        Map<Long, UnAppliedCashEntity> unAppliedCashMap = queryUnAppliedCash(contractList.stream().map(ContractEntity::getId).collect(Collectors.toList()), type);
        SummaryKeyStrategy keyStrategy = template.getKeyStrategy();
        for (ContractEntity contract : contractList) {
            List<CustomReceivableVO> customVos = buildList(contract, type, unAppliedCashMap.get(contract.getId()));
            keyStrategy.setContract(contract);
            for(CustomReceivableVO vo : customVos) {
                template.addSingleData(vo);
            }
        }

    }



    public List<ReceivableDetailVo> queryReceivableDetail(QuerySummaryParam param, @NotNull  WriteOffTypeEnum type) throws BaseException {
        List<ContractEntity> contractList = contractService.queryAll(buildContractParams(param));
        DetailKeyStrategy keyStrategy = new DetailKeyStrategy();
        DetailConverter converter = new DetailConverter();
        Template<CustomReceivableVO, ReceivableDetailVo> template = new Template<>(keyStrategy, converter);
        Map<Long, UnAppliedCashEntity> unAppliedCashMap = queryUnAppliedCash(contractList.stream().map(ContractEntity::getId).collect(Collectors.toList()), type);
        for (ContractEntity contract : contractList) {
            List<CustomReceivableVO> customVos = buildList(contract, type, unAppliedCashMap.get(contract.getId()));
            keyStrategy.setContract(contract);
            converter.setContract(contract);
            converter.setActionPlan(ActionPlanEnum.getActionPlan(customVos.get(customVos.size() -1), type));
            for(CustomReceivableVO vo : customVos) {
                template.addSingleData(vo);
            }
        }

        return template.buildDataList( key -> {
            ReceivableDetailVo vo = new ReceivableDetailVo();
            vo.setNPer(key);
            return vo;
        });
    }

    private List<CustomReceivableVO> buildList(ContractEntity contract, WriteOffTypeEnum type, UnAppliedCashEntity unAppliedCash) throws BaseException {
        List<CustomReceivableVO> customVos;
        if (type == WriteOffTypeEnum.SERVICE) {
            customVos = buildServiceVos(contract);
        } else {
            customVos = buildHwVos(contract);
        }
        fillVoList(customVos, unAppliedCash);
        return customVos;
    }

    private QueryParams buildContractParams(QuerySummaryParam params) {
        QueryParams queryParams = new QueryParams();
        //TODO
        queryParams.setStartDate(params.getBeginDate());
        queryParams.setEndDate(DateUtils.toString(Calendar.getInstance().getTime(), "yyyy-MM-dd"));
        return queryParams;
    }
}
