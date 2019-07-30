
DROP TABLE IF EXISTS `rcs_unapplied_cache`;

CREATE TABLE `rcs_unapplied_cache` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `ref_contract_id` int(32)  DEFAULT NULL COMMENT '关联的合同Id',
  `receivable_amount` double(16,2)  DEFAULT NULL COMMENT '应收合计',
  `actual_pay_amount` double(16,2)  DEFAULT NULL COMMENT '实际支付',
  `unoverdue_amount` double(16,2) DEFAULT NULL COMMENT '未逾期',

  `day1_30` double(16,2)  DEFAULT NULL COMMENT '逾期1-30天',
  `day31_60` double(16,2)  DEFAULT NULL COMMENT '逾期31-60天',
  `day61_90` double(16,2) DEFAULT NULL COMMENT '逾期61-90天',
  `day91_180` double(16,2)  DEFAULT NULL COMMENT '逾期91-180天',
  `day181_365` double(16,2)  DEFAULT NULL COMMENT '逾期181-365天',
  `day_lt_365` double(16,2) DEFAULT NULL COMMENT '逾期超过365',

  `day_lt_total` double(16,2)  DEFAULT NULL COMMENT '逾期大于90天合计',
  `day_lt_90_total` double(16,2)  DEFAULT NULL COMMENT '逾期大于90天占比',
  `overdue_total` double(16,2) DEFAULT NULL COMMENT '逾期合计',
  `total` double(16,2)  DEFAULT NULL COMMENT '本期合计',

  `write_off_type` varchar(10) DEFAULT NULL COMMENT '关联的核销类型',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='手动调整应收记录';

