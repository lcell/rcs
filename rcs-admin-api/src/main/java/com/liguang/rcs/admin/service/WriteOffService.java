package com.liguang.rcs.admin.service;

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
import com.liguang.rcs.admin.util.NumericUtils;
import com.liguang.rcs.admin.util.ResponseCode;
import com.liguang.rcs.admin.web.writeoff.CommissionFeeSettlementVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffSettlementVO;
import com.liguang.rcs.admin.web.writeoff.WriteOffVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WriteOffService {

    @Autowired
    private WriteOffRepository writeOffRepository;

    @Autowired
    private InvoiceService invoiceService;


    @Transactional
    public void relationContract(ContractEntity contract, List<Long> writeOffIds, WriteOffTypeEnum type, String settlementId) {
        writeOffRepository.relationContract(contract.getId(),  type.getCode(), settlementId, writeOffIds);
    }

    @Transactional
    public void unRelationContract(ContractEntity contract, List<Long> writeOffIds, WriteOffTypeEnum type, String settlementId) {
        writeOffRepository.unRelationContract(contract.getId(), type.getCode(),settlementId,  writeOffIds);
    }


    public void unAllRelationContract(ContractEntity contract, WriteOffTypeEnum type, String settlementId) {
        writeOffRepository.unAllRelationContract(contract.getId() , type.getCode(),settlementId);
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

    //合同生效日期为，第一个发票日期
    //1. 如果发票金额少于首付，不开始催收
    //2.
    public List<WriteOffSettlementVO> querySettlement(ContractEntity contract) throws BaseException {
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
        Multimap<String, InvoiceEntity> invoiceMap = invoiceService.queryRelatedMap(contract.getId(), WriteOffTypeEnum.HARDWARE);
        Set<String> dateSets = invoiceMap.keySet();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(effectiveDate.getTime());
        //当月的所有发票
        //if ()

        //double totalAmount = calcTotalAmountByMonth(instance.getTime(), invoiceMap);
        //double periodPayment = (totalAmount - firstPayment)/receivableNum; // 每期应收的累计
        for (int i = 0 ; i <= receivableNum; i++) {

        }

        return null;
    }



    private WriteOffTypeEnum buildFirstSettlement(String monthKey, Multimap<String, InvoiceEntity> invoiceMap,
                                                  List<WriteOffEntity> writeOffList,
                                                  int remainNum, double firstPayment) {
        WriteOffSettlementVO settlementVO = new WriteOffSettlementVO();

        settlementVO.setPlanPayAmount(firstPayment);
        settlementVO.setPlanNumOfPeriods("首期");
        settlementVO.setPayMonth(monthKey);
        //settlementVO.setActualPayAmount(calcActualPayment(writeOffList), );
        //settlementVO.setAccumulatedPayAmount();
        return null;
    }

    /**
     * 使用核销冲抵发票金额
     * @param compareWithMonthFLag -1--过去 0--当前月  1--以后
     */
    private void setActualPayment(WriteOffSettlementVO settlementVO, List<WriteOffEntity> writeOffList, int compareWithMonthFLag) throws ParseException {
        //确定处理的期数是否为当月， 如果是当月的话，就需要将所有的核销算在这期上
        WriteOffEntity lastEntity = null;
        double tmpRemainAmount = settlementVO.getPlanPayAmount();
        for(WriteOffEntity entity : writeOffList) {
            lastEntity = entity;
            if (tmpRemainAmount  <= entity.getPaymentAmount() && compareWithMonthFLag != 0) {
                entity.setPaymentAmount(entity.getPaymentAmount() - tmpRemainAmount);
                tmpRemainAmount = 0;
                break;
            }
            tmpRemainAmount -= entity.getPaymentAmount();
            entity.setPaymentAmount(0d);
        }
        if (lastEntity == null) {
            settlementVO.setActualPayAmount(settlementVO.getPlanPayAmount() - tmpRemainAmount);
            settlementVO.setActualPayDate(DateUtils.toString(lastEntity.getPaymentDate(), "yyyyMMdd"));
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(DateUtils.toDate(settlementVO.getPayMonth(), "yyyyMM"));
        instance.add(Calendar.MONTH, 1);
        int deltaDay = DateUtils.dateMinus(instance.getTime(), lastEntity.getPaymentDate());
        settlementVO.setOverdueAmount(tmpRemainAmount);
        //计算逾期天数
        if (compareWithMonthFLag >= 0 || deltaDay >= 0) {
            //1. 如果期数为当前月或者以后的月，或者支付日期大于期数，则不存在逾期
            settlementVO.setOverdueNumOfDate("-");
        } else {
            //设置逾期
            settlementVO.setOverdueNumOfDate(OverdueDateEnum.convertToEnum(deltaDay).getColumn());
        }
    }

    private WriteOffSettlementVO buildSettlement(String monthKey, Multimap<String, InvoiceEntity> invoiceMap, int num, int remainNum) {
        WriteOffSettlementVO settlementVO = new WriteOffSettlementVO();
        settlementVO.setPayMonth(monthKey);
        //settlementVO.set


        return settlementVO;
    }


    /**
     * 计算每期的值
     */
    private double calcAverageAmount(Date date, Multimap<String, InvoiceEntity> invoiceMap, int num) {
        return 0d;//TODO //calcTotalAmountByMonth(date, invoiceMap) / num;
    }
    //计算出当月所开的发票总金额
    private double calcTotalAmountByMonth(String dateKey, Multimap<String, InvoiceEntity> invoiceMap) {
        Collection<InvoiceEntity> entities = invoiceMap.get(dateKey);
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        double result = 0d;
        for(InvoiceEntity entity : entities) {
            result += entity.getAmount();
        }
        return result;
    }

    /**
     * 发票服务费统计
     * @param contract
     * @return
     */
    public List<CommissionFeeSettlementVO> queryCommissionByContractId(ContractEntity contract) throws BaseException {
        List<InvoiceEntity> entityList = invoiceService.queryRelatedEntityList(contract.getId(), WriteOffTypeEnum.SERVICE);
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        try {

            List<WriteOffEntity> writeOffList = writeOffRepository.findByRefContractIdAndType(contract.getId(), WriteOffTypeEnum.SERVICE);
            List<CommissionFeeSettlementVO> result = Lists.newArrayListWithCapacity(entityList.size());
            CommissionFeeSettlementVO preVo = null;
            for(int i = 0 ; i < entityList.size(); i++) {
                InvoiceEntity entity = entityList.get(i);
                CommissionFeeSettlementVO vo = new CommissionFeeSettlementVO();
                vo.setPayDate(DateUtils.toString(entity.getBillingDate(), "yyyyMMdd"));
                vo.setPayAmount(entity.getAmount());
                vo.setInvoiceNo(entity.getInvoiceNo());
                setOverdueInfo(vo, writeOffList, i == entityList.size() - 1);
                if (preVo != null) {
                    //计算累计收款
                    vo.setAccumulatedPayAmount(preVo.getAccumulatedPayAmount() + vo.getActualPayAmount());
                }
                preVo = vo;
            }
            //TODO
            return result;
        }catch (Exception e) {
            throw new BaseException(ResponseCode.SYS_INNER_ERR);
        }
    }

    /**
     * 服务费是开了发票之后才开始算得，只会存在当月开当月的服务费发票
     *
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
            if (overdueDate == OverdueDateEnum.DAY0) {
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
