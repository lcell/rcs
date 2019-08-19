package com.liguang.rcs.admin.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.liguang.rcs.admin.common.enumeration.OverdueDateEnum;
import com.liguang.rcs.admin.db.domain.ContractEntity;
import com.liguang.rcs.admin.db.domain.InvoiceEntity;
import com.liguang.rcs.admin.db.domain.WriteOffEntity;
import com.liguang.rcs.admin.db.repository.WriteOffRepository;
import com.liguang.rcs.admin.exception.BaseException;
import com.liguang.rcs.admin.util.DateUtils;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.web.writeoff.CommissionFeeSettlementVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.liguang.rcs.admin.common.enumeration.OverdueDateEnum.DAY0;
import static com.liguang.rcs.admin.util.NumericUtils.minus;
import static com.liguang.rcs.admin.util.NumericUtils.plus;

@Slf4j
@Service
public class WriteOffService {

    @Autowired
    private WriteOffRepository writeOffRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ContractService contractService;


    @Transactional
    public void relationContract(ContractEntity contract, List<Long> writeOffIds, String settlementId) {
        writeOffRepository.relationContract(contract.getId(), settlementId, writeOffIds);
    }

    @Transactional
    public void unRelationContract(ContractEntity contract, List<Long> writeOffIds, String settlementId) {
        writeOffRepository.unRelationContract(contract.getId(), settlementId, writeOffIds);
    }


    public void unAllRelationContract(ContractEntity contract, String settlementId) {
        writeOffRepository.unAllRelationContract(contract.getId(), settlementId);
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

    public List<WriteOffVO> queryWriteOffRecord(String customId) {
        List<WriteOffEntity> entityList = writeOffRepository.queryByCustom(customId);
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        return entityList.stream().map(WriteOffVO::buildFrom).collect(Collectors.toList());
    }


    public Multimap<String, WriteOffEntity> queryWriteOffEntity(Long contractId) {
        Multimap<String, WriteOffEntity> entityMultimap = ArrayListMultimap.create();
        List<WriteOffEntity> writeOffEntitys = writeOffRepository.findByRefContractId(contractId);
        if (writeOffEntitys == null || writeOffEntitys.isEmpty()) {
            return entityMultimap;
        }
        for (WriteOffEntity entity : writeOffEntitys) {
            entityMultimap.put(entity.getSettlementId(), entity);
        }
        return entityMultimap;
    }

    //TODO 修改成从缓存里面读取
    //合同生效日期为，第一个发票日期 //暂时不考虑，每期发票开不够后续补齐的场景
    //1. 分期策略 发票金额 > 当期金额 ？ 合同当前金额 ：发票金额
    //                剩余发票金额 = 发票金额 - 合同当前金额；
    //2. settlementId 生成规则 0，1，2，3，4，5，6 ...
    public List<WriteOffSettlementVO> querySettlement(ContractEntity contract) throws BaseException {

        List<InvoiceEntity> invoiceList = invoiceService.queryRelatedEntityList(contract.getId());
        if (invoiceList == null || invoiceList.isEmpty()) {
            log.error("[WriteOff] there is no invoice related to contarct, contract:{}", contract);
            return Collections.emptyList();
        }
        List<WriteOffSettlementVO> settlementList = Lists.newArrayListWithCapacity(contract.getReceivableNum());
        //查询核销记录
        Multimap<String, WriteOffEntity> writeOffMaps = queryWriteOffEntity(contract.getId());
        if (contract.getEffectiveDate() == null || contract.getEffectiveDate().equals(invoiceList.get(0).getBillingDate())) {
            contractService.updateEffectTime(contract.getId(), invoiceList.get(0).getBillingDate());
        }
        Calendar payDay = calcFirstPayDate(invoiceList.get(0));

        //检查是否和合同的生效日期一直

        double totalInvoiceAmount = calcTotalAmount(invoiceList);
        Double actualPayAmountTotal = 0d; // 累计支付
        for (int i = 0 ; i <= contract.getReceivableNum(); i++) {
            double planPay = contract.getFirstPayment();
            if (i > 0) {
                payDay.add(Calendar.MONTH, 1); // 每次累加1
                planPay = contract.getPeriodPayment();
            }
            if (totalInvoiceAmount < planPay) {
                planPay = totalInvoiceAmount;
            }
            totalInvoiceAmount -= planPay;
            String settlementId = String.valueOf(i);
            WriteOffSettlementVO vo = buildHWSettlement(contract, payDay, planPay, writeOffMaps.get(settlementId), settlementId);
            actualPayAmountTotal = plus(actualPayAmountTotal, vo.getActualPayAmount());
            vo.setAccumulatedPayAmount(actualPayAmountTotal);
            vo.setReceivableReasonable(totalInvoiceAmount);
            settlementList.add(vo);
        }
        return settlementList;
    }

    private WriteOffSettlementVO buildHWSettlement(ContractEntity contract, Calendar firstPayDay, double firstMonthPlanPay,
                                                      Collection<WriteOffEntity> writeOffEntities, String settlementId) throws BaseException {
        try {
            WriteOffSettlementVO vo = new WriteOffSettlementVO();
            vo.setSettlementId(settlementId);
            vo.setPlanNumOfPeriods(buildNumOfPeriods(settlementId));
            vo.setContractId(contract.getId().toString());
            vo.setPlanPayAmount(firstMonthPlanPay);
            vo.setPayMonth(DateUtils.toString(firstPayDay.getTime(), "yyyyMM"));
            setActualPayInfo(vo, writeOffEntities);
            return vo;
        } catch (Exception ex) {
            log.error("[WriteOff] Inner Err, Exception:{}", ex);
            throw new BaseException(ResponseCode.SYS_INNER_ERR);
        }
    }

    private String buildNumOfPeriods(String settlementId) {
        if ("0".equals(settlementId)) {
            return "首期";
        }
        return "第" + settlementId + "期";
    }

    private void setActualPayInfo(WriteOffSettlementVO vo, Collection<WriteOffEntity> writeOffEntities) throws ParseException {
        Timestamp payDay = null;
        try {
            if (writeOffEntities == null || writeOffEntities.isEmpty()) {
                return;
            }
            Double totalActualPay = 0d;
            for (WriteOffEntity entity : writeOffEntities) {
                totalActualPay = plus(totalActualPay, entity.getPaymentAmount());
                payDay = entity.getPaymentDate();
            }
            vo.setActualPayDate(DateUtils.toString(payDay, "yyyyMMdd"));
            vo.setActualPayAmount(totalActualPay);
        } finally {
            OverdueDateEnum overdueType = DAY0;
            Double overdueAmount;
            //计算逾期
            if ((overdueAmount = minus(vo.getPlanPayAmount(), vo.getActualPayAmount())) > 0) {
                int deltaMonth = DateUtils.dateMinusForMonth(Calendar.getInstance().getTime(), vo.getPayMonth(), "yyyyMM");
                overdueType = OverdueDateEnum.convertToEnumByMonth(deltaMonth);
            } else if(payDay != null){
                int deltaMonth = DateUtils.dateMinusForMonth(DateUtils.toDate(payDay), vo.getPayMonth(), "yyyyMM");
                overdueType = OverdueDateEnum.convertToEnumByMonth(deltaMonth);
            }
            if (overdueType != DAY0) {
                vo.setOverdueNumOfDate(overdueType.getCode());
                if (overdueAmount > 0) {
                    vo.setOverdueAmount(overdueAmount);
                }
            }
        }
    }

    private Calendar calcFirstPayDate(InvoiceEntity invoice) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(invoice.getBillingDate());
        instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1, 0);
        return instance;
    }

    //计算出当月所开的发票总金额
    private double calcTotalAmount(Collection<InvoiceEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        double result = 0d;
        for (InvoiceEntity entity : entities) {
            result += entity.getAmount();
        }
        return result;
    }

    /**
     * 发票服务费统计
     *  settlementId 为发票的ID
     * @param contract
     * @return
     */
    public List<CommissionFeeSettlementVO> queryCommissionByContractId(ContractEntity contract) throws BaseException {
        List<InvoiceEntity> entityList = invoiceService.queryRelatedEntityList(contract.getId());
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            Multimap<String, WriteOffEntity> writeOffMap = queryWriteOffEntity(contract.getId());
            List<CommissionFeeSettlementVO> result = Lists.newArrayListWithCapacity(entityList.size());
            Double actualPayTotal = null;
            for (int i = 0; i < entityList.size(); i++) {
                InvoiceEntity entity = entityList.get(i);
                if (i == 0 && (contract.getEffectiveDate() == null
                        || contract.getEffectiveDate().equals(entity.getBillingDate()))) {
                    contractService.updateEffectTime(contract.getId(), entity.getBillingDate());
                }
                CommissionFeeSettlementVO vo = new CommissionFeeSettlementVO();
                vo.setSettlementId(entity.getId().toString());
                vo.setPayDate(DateUtils.toString(entity.getBillingDate(), "yyyyMMdd"));
                vo.setPayAmount(entity.getAmount());
                vo.setInvoiceNo(entity.getInvoiceNo());
                setActualPayInfo(vo, writeOffMap.get(entity.getId().toString()));
                actualPayTotal = plus(actualPayTotal, vo.getActualPayAmount());
                vo.setAccumulatedPayAmount(actualPayTotal);
                result.add(vo);
            }
            return result;
        } catch (Exception e) {
            log.error("[WriteOff] Inner Err, Exception:{}", e);
            throw new BaseException(ResponseCode.SYS_INNER_ERR);
        }
    }

    //FIXME 后续可以考虑将它和分期的计算合并在一起
    private void setActualPayInfo(CommissionFeeSettlementVO serviceVo, Collection<WriteOffEntity> writeOffEntities) throws ParseException {
        Timestamp payDay = null;
        try {
            if (writeOffEntities == null || writeOffEntities.isEmpty()) {
                return;
            }
            Double totalActualPay = 0d;
            for(WriteOffEntity entity : writeOffEntities) {
                totalActualPay = plus(totalActualPay, entity.getPaymentAmount());
                payDay = entity.getPaymentDate();
            }
            serviceVo.setActualPayDate(DateUtils.toString(payDay, "yyyyMMdd"));
            serviceVo.setActualPayAmount(totalActualPay);
        } finally {
            OverdueDateEnum overdueType = DAY0;
            Double overdueAmount = minus(serviceVo.getPayAmount(), serviceVo.getActualPayAmount());
            serviceVo.setReceivableReasonable(overdueAmount); //设置应收金额
            //计算逾期 如果应收金额大于
            if (serviceVo.getReceivableReasonable() > 0) {
                long deltaDay = DateUtils.dateMinusToNow(serviceVo.getPayDate(), "yyyyMMdd") - 30;
                overdueType = OverdueDateEnum.convertToEnum(deltaDay);
            } else if (payDay != null){
                long deltaDay = DateUtils.dateMinus(DateUtils.toString(payDay, "yyyyMMdd"),  serviceVo.getPayDate(), "yyyyMMdd") - 30;
                overdueType = OverdueDateEnum.convertToEnum(deltaDay);
            }
            if (overdueType != DAY0) {
                serviceVo.setOverdueNumOfDate(overdueType.getCode());
                serviceVo.setOverdueAmount(overdueAmount);
            }
        }
    }

    public boolean hasRelateToContracts(List<Long> contractIdList) {
        return writeOffRepository.countByContractIds(contractIdList) > 0;
    }
}
