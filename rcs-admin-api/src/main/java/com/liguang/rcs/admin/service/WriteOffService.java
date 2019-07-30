package com.liguang.rcs.admin.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.liguang.rcs.admin.common.enumeration.OverdueDateEnum;
import com.liguang.rcs.admin.common.enumeration.WriteOffTypeEnum;
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
import java.util.*;
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


    @Transactional
    public void relationContract(ContractEntity contract, List<Long> writeOffIds, WriteOffTypeEnum type, String settlementId) {
        writeOffRepository.relationContract(contract.getId(), type.getCode(), settlementId, writeOffIds);
    }

    @Transactional
    public void unRelationContract(ContractEntity contract, List<Long> writeOffIds, WriteOffTypeEnum type, String settlementId) {
        writeOffRepository.unRelationContract(contract.getId(), type.getCode(), settlementId, writeOffIds);
    }


    public void unAllRelationContract(ContractEntity contract, WriteOffTypeEnum type, String settlementId) {
        writeOffRepository.unAllRelationContract(contract.getId(), type.getCode(), settlementId);
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


    public Multimap<String, WriteOffEntity> queryWriteOffEntity(Long contractId, WriteOffTypeEnum type) {
        Multimap<String, WriteOffEntity> entityMultimap = ArrayListMultimap.create();
        List<WriteOffEntity> writeOffEntitys = writeOffRepository.findByRefContractIdAndType(contractId, type);
        if (writeOffEntitys == null || writeOffEntitys.isEmpty()) {
            return entityMultimap;
        }
        for (WriteOffEntity entity : writeOffEntitys) {
            entityMultimap.put(entity.getSettlementId(), entity);
        }
        return entityMultimap;
    }

    //TODO 修改成从缓存里面读取
    //合同生效日期为，第一个发票日期
    //1. 如果发票金额少于首付，不开始催收
    //2. settlemenetId 生成规则 0，1，2，3，4，5，6 ...
    public List<WriteOffSettlementVO> querySettlement(ContractEntity contract) throws BaseException {

        Multimap<String, InvoiceEntity> invoiceMap = invoiceService.queryRelatedMap(contract.getId(), WriteOffTypeEnum.HARDWARE);
        //
        if (invoiceMap == null || invoiceMap.isEmpty()) {
            log.error("[WriteOff] there is no invoice related to contarct, contract:{}", contract);
            return Collections.emptyList();
            //throw new BaseException(ResponseCode.BAD_ARGUMENT_VALUE);
        }
        //查询核销记录
        Multimap<String, WriteOffEntity> writeOffMaps = queryWriteOffEntity(contract.getId(), WriteOffTypeEnum.HARDWARE);
        List<WriteOffSettlementVO> settlementList = Lists.newArrayList();

        Calendar payDay = calcFirstPayDate(invoiceMap.keySet());

        Double firstMonthPlanPay = calcTotalAmount(invoiceMap.get(DateUtils.toString(payDay.getTime(), "yyyyMM")));
        Double periodPayment = minus(firstMonthPlanPay, contract.getFirstPayment());
        Double invoiceAmountTotal = calcTotalAmount(invoiceMap.values()); //发票总额
        Double actualPayAmountTotal = 0d; // 累计支付
        //如果发票的数据不够首付，则以发票的金额为应收首付
        if (periodPayment <= 0) {
            periodPayment = 0d;
        } else {
            firstMonthPlanPay = contract.getFirstPayment();
            periodPayment = periodPayment / contract.getReceivableNum();
        }
        WriteOffSettlementVO vo = buildHWSettlement(contract, payDay, firstMonthPlanPay, writeOffMaps.get("0"), "0");
        actualPayAmountTotal = plus(actualPayAmountTotal, vo.getActualPayAmount());
        //设置累计支付，剩余应收
        vo.setAccumulatedPayAmount(actualPayAmountTotal);
        vo.setReceivableReasonable(minus(invoiceAmountTotal, actualPayAmountTotal));
        settlementList.add(vo);

        for (int i = 1; i <= contract.getReceivableNum(); i++) {
            //更新每期应付
            payDay.add(Calendar.MONTH, 1); // 每次累加1
            double invoiceTotal = calcTotalAmount(invoiceMap.get(DateUtils.toString(payDay.getTime(), "yyyyMM")));
            periodPayment += (invoiceTotal / (contract.getReceivableNum() - i + 1));
            WriteOffSettlementVO tmpVo = buildHWSettlement(contract, payDay, periodPayment, writeOffMaps.get(String.valueOf(i)), String.valueOf(i));
            //设置累计支付和应收余额
            actualPayAmountTotal = plus(actualPayAmountTotal, tmpVo.getActualPayAmount());
            tmpVo.setAccumulatedPayAmount(actualPayAmountTotal);
            tmpVo.setReceivableReasonable(minus(invoiceAmountTotal, actualPayAmountTotal));
            settlementList.add(tmpVo);
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
            Double overdueAmount = 0d;
            //计算逾期
            if ((overdueAmount = minus(vo.getPlanPayAmount(), vo.getActualPayAmount())) > 0) {
                int deltaMonth = DateUtils.dateMinusForMonth(Calendar.getInstance().getTime(), vo.getPayMonth(), "yyyyMM");
                overdueType = OverdueDateEnum.convertToEnumByMonth(deltaMonth);
            } else {
                int deltaMonth = DateUtils.dateMinusForMonth(DateUtils.toDate(payDay), vo.getPayMonth(), "yyyyMM");
                overdueType = OverdueDateEnum.convertToEnumByMonth(deltaMonth);
            }
            if (overdueType != DAY0) {
                vo.setOverdueNumOfDate(overdueType.getColumn());
                if (overdueAmount > 0) {
                    vo.setOverdueAmount(overdueAmount);
                }
            }
        }
    }

    private Calendar calcFirstPayDate(Set<String> monthStrs) {
        Calendar instance = Calendar.getInstance();
        Date firstPayDay = null;
        for (String monthStr : monthStrs) {
            Date tmpDate = DateUtils.softToDate(monthStr, "yyyyMM");
            if (tmpDate == null) {
                continue;
            }
            if (firstPayDay == null || firstPayDay.getTime() > tmpDate.getTime()) {
                firstPayDay = tmpDate;
            }
        }
        instance.setTime(firstPayDay);
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
        List<InvoiceEntity> entityList = invoiceService.queryRelatedEntityList(contract.getId(), WriteOffTypeEnum.SERVICE);
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            Multimap<String, WriteOffEntity> writeOffMap = queryWriteOffEntity(contract.getId(), WriteOffTypeEnum.SERVICE);
            List<CommissionFeeSettlementVO> result = Lists.newArrayListWithCapacity(entityList.size());
            Double actualPayTotal = null;
            for (int i = 0; i < entityList.size(); i++) {
                InvoiceEntity entity = entityList.get(i);
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
    private void setActualPayInfo(CommissionFeeSettlementVO vo, Collection<WriteOffEntity> writeOffEntities) throws ParseException {
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
            vo.setActualPayDate(DateUtils.toString(payDay, "yyyyMMdd"));
            vo.setActualPayAmount(totalActualPay);
        } finally {
            OverdueDateEnum overdueType = DAY0;
            Double overdueAmount = minus(vo.getPayAmount(), vo.getActualPayAmount());
            vo.setReceivableReasonable(overdueAmount); //设置应收金额
            //计算逾期 如果应收金额大于
            if (vo.getReceivableReasonable() > 0) {
                int deltaDay = DateUtils.dateMinusToNow(vo.getPayDate(), "yyyyMMdd");
                overdueType = OverdueDateEnum.convertToEnum(deltaDay);
            } else {
                int deltaDay = DateUtils.dateMinusForMonth(payDay,  vo.getPayDate(), "yyyyMMdd");
                overdueType = OverdueDateEnum.convertToEnum(deltaDay);
            }
            if (overdueType != DAY0) {
                vo.setOverdueNumOfDate(overdueType.getColumn());
                vo.setOverdueAmount(overdueAmount);
            }
        }

    }

    /**
     * 服务费是开了发票之后才开始算得，只会存在当月开当月的服务费发票
     */
    private void setOverdueInfo(CommissionFeeSettlementVO vo, List<WriteOffEntity> writeOffEntityList, boolean isLast) throws ParseException {
        double payment = vo.getPayAmount();
        WriteOffEntity lastEntity = null;
        for (WriteOffEntity entity : writeOffEntityList) {
            if (entity.getPaymentAmount() > 0) {
                lastEntity = entity;
            }
            if (payment <= entity.getPaymentAmount() && !isLast) {
                entity.setPaymentAmount(entity.getPaymentAmount() - payment);
                payment = 0;
                break;
            }
            payment -= entity.getPaymentAmount();
            entity.setPaymentAmount(0d);
        }
        //设置实际支付
        vo.setActualPayAmount(vo.getPayAmount() - payment);
        //设置支付日期
        if (lastEntity != null) {
            //支付日期
            vo.setPayDate(DateUtils.toString(lastEntity.getPaymentDate(), "yyyyMMdd"));
        } else {
            vo.setPayDate("-");

        }
        //设置逾期日期和金额
        if (payment <= 0) {
            //和实际支付日期进行比较
            int deltaDay = DateUtils.dateMinus(vo.getPayDate(), vo.getActualPayDate(), "yyyyMMdd") - 30;
            //逾期天数
            vo.setOverdueNumOfDate(OverdueDateEnum.convertToEnum(deltaDay).getColumn());
            vo.setOverdueAmount(0d);
        } else {
            //和当前时间比较
            int deltaDay = DateUtils.dateMinusToNow(vo.getPayDate(), "yyyyMMdd") - 30;
            OverdueDateEnum overdueDate = OverdueDateEnum.convertToEnum(deltaDay);
            if (overdueDate == DAY0) {
                //没有超期
                vo.setOverdueAmount(0d);
            } else {
                vo.setOverdueAmount(payment);
            }
            vo.setOverdueNumOfDate(overdueDate.getColumn());
        }
        //设置应收余额
        vo.setReceivableReasonable(payment);
        //设置累计收款
        vo.setAccumulatedPayAmount(vo.getActualPayAmount());
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
