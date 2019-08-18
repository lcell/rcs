package com.liguang.rcs.admin.service;

import com.google.common.collect.Maps;
import com.liguang.rcs.admin.common.Constant;
import com.liguang.rcs.admin.common.enumeration.ActionPlanEnum;
import com.liguang.rcs.admin.common.enumeration.ContractTypeEnum;
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
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public CustomHwOutput queryCustomHw(ContractEntity contract) throws BaseException {
        CustomHwOutput output = new CustomHwOutput();
        output.setContractId(contract.getId().toString());
        output.setContractNo(contract.getContractNo());
        fillHwOutput(contract, output);
        return output;
    }

    /**
     * 硬件分期 模版
     */
    private void fillHwOutput(ContractEntity contract, CustomHwOutput output) throws BaseException {
        UnAppliedCashEntity entity = queryUnAppliedCash(contract.getId());
        List<CustomReceivableVO> hwList = buildHwVos(contract);
        CustomReceivableVO netTotal = fillVoList(hwList, entity);
        output.setHwReceivableVOS(hwList);
        output.setActionPlan(ActionPlanEnum.getActionPlan(netTotal).getCode());

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

    private List<CustomReceivableVO> buildHwVos(ContractEntity contract) throws BaseException {
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

    public CustomCommissionOutput queryCustomCommission(ContractEntity contract) throws BaseException {
        CustomCommissionOutput output = new CustomCommissionOutput();
        output.setContractId(contract.getId().toString());
        output.setContractNo(contract.getContractNo());
        fillCommissionOutput(contract, output);
        return output;
    }


    private void fillCommissionOutput(ContractEntity contract, CustomCommissionOutput output) throws BaseException{
        UnAppliedCashEntity entity = queryUnAppliedCash(contract.getId());
        List<CustomReceivableVO> serviceKList = buildServiceVos(contract);
        CustomReceivableVO netTotal = fillVoList(serviceKList, entity);
        output.setCommissionReceivableVOS(serviceKList);
        output.setActionPlan(ActionPlanEnum.getActionPlan(netTotal).getCode());
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

    private List<CustomReceivableVO> buildServiceVos(ContractEntity contract) throws BaseException {
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

    private UnAppliedCashEntity queryUnAppliedCash(Long contractId) {
        Timestamp now = DateUtils.softToTimestamp(
                DateUtils.toString(Calendar.getInstance().getTime(), "yyyyMMdd"), "yyyyMMdd");
        return unAppliedCashRepository.findByRefContractIdAndCreateDateAfter(contractId, now);
    }

    private Map<Long, UnAppliedCashEntity> queryUnAppliedCash(List<Long> contractIds) {
        Map<Long, UnAppliedCashEntity> entityMap = Maps.newHashMap();
        Timestamp now = DateUtils.softToTimestamp(
                DateUtils.toString(Calendar.getInstance().getTime(), "yyyyMMdd"), "yyyyMMdd");
        List<UnAppliedCashEntity> lst = unAppliedCashRepository.findByRefContractIdInAndCreateDateAfter(contractIds, now);
        for (UnAppliedCashEntity entity : lst) {
            entityMap.putIfAbsent(entity.getRefContractId(), entity);
        }
        return entityMap;
    }

    public void saveUnAppliedCash(UnAppliedCashEntity entity) {
        unAppliedCashRepository.save(entity);
    }

    public List<ReceivableSummaryVo> queryReceivableSummary(QuerySummaryParam param, @Nullable ContractTypeEnum type) throws BaseException {
        List<ContractEntity> contractList = contractService.queryAll(buildContractParams(param, type));
        SummaryKeyStrategy keyStrategy = new SummaryKeyStrategy();
        SummaryConverter converter = new SummaryConverter();
        Template<CustomReceivableVO, ReceivableSummaryVo> template = new Template<>(keyStrategy, converter);
        fillSummaryTemplate(contractList, template);
        return template.buildDataList(key -> {
            ReceivableSummaryVo vo = new ReceivableSummaryVo();
            vo.setNPer(key);
            return vo;
        });
    }

    private void fillSummaryTemplate(List<ContractEntity> contractList, Template<CustomReceivableVO, ?> template) throws BaseException {
        Map<Long, UnAppliedCashEntity> unAppliedCashMap = queryUnAppliedCash(contractList.stream().map(ContractEntity::getId).collect(Collectors.toList()));
        SummaryKeyStrategy keyStrategy = template.getKeyStrategy();
        for (ContractEntity contract : contractList) {
            List<CustomReceivableVO> customVos = buildList(contract, unAppliedCashMap.get(contract.getId()));
            keyStrategy.setContract(contract);
            for(CustomReceivableVO vo : customVos) {
                template.addSingleData(vo);
            }
        }

    }

    public List<ReceivableDetailVo> queryReceivableDetail(QuerySummaryParam param, @NotNull  ContractTypeEnum type) throws BaseException {
        List<ContractEntity> contractList = contractService.queryAll(buildContractParams(param, type));
        DetailKeyStrategy keyStrategy = new DetailKeyStrategy();
        DetailConverter converter = new DetailConverter();
        Template<CustomReceivableVO, ReceivableDetailVo> template = new Template<>(keyStrategy, converter);
        Map<Long, UnAppliedCashEntity> unAppliedCashMap = queryUnAppliedCash(contractList.stream().map(ContractEntity::getId).collect(Collectors.toList()));
        for (ContractEntity contract : contractList) {
            List<CustomReceivableVO> customVos = buildList(contract, unAppliedCashMap.get(contract.getId()));
            keyStrategy.setContract(contract);
            converter.setContract(contract);
            converter.setActionPlan(ActionPlanEnum.getActionPlan(customVos.get(customVos.size() -1)));
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

    private List<CustomReceivableVO> buildList(ContractEntity contract, UnAppliedCashEntity unAppliedCash) throws BaseException {
        List<CustomReceivableVO> customVos;
        if (contract.getType() == ContractTypeEnum.HARDWARE) {
            customVos = buildHwVos(contract);
        } else {
            customVos = buildServiceVos(contract);
        }
        fillVoList(customVos, unAppliedCash);
        return customVos;
    }

    private QueryParams buildContractParams(QuerySummaryParam params, ContractTypeEnum contractType) {
        QueryParams queryParams = new QueryParams();
        queryParams.setStatus(params.getContractStatus());
        queryParams.setProductType(params.getProductType());
        queryParams.setSalesName(params.getSalesName());
        if (contractType != null) {
            queryParams.setType(contractType.getCode());
        }
        if (Strings.isNotBlank(params.getBeginDate())) {
            queryParams.setStartDate(params.getBeginDate());
            queryParams.setEndDate(DateUtils.toString(Calendar.getInstance().getTime(), "yyyy-MM-dd"));
        }
        return queryParams;
    }
}
