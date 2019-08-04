

CREA  TE TABLE `daily_alert_info` {
    `id` int(32) NOT NULL AUTO_INCREMENT,
    `alert_no` varchar(31) DEFAULT NULL COMMENT '警情编号',
    `reporter` varchar(63) DEFAULT NULL COMMENT '报案人',
    `case_detail_info` VARCHAR (2047) DEFAULT NULL COMMENT '主要案情',
    `case_detail_address` VARCHAR (1023) DEFAULT NULL COMMENT '案发地址',
    `contact_tel_num` VARCHAR (12) DEFAULT NULL COMMENT '联系电话',
    `district_police_station` VARCHAR (255) DEFAULT NULL COMMENT '辖区所',
    `accept_case_time` TIMESTAMP DEFAULT NULL COMMENT '案件受理时间',
    `revisit_detail` VARCHAR (2047) DEFAULT NULL COMMENT '回访情况',
    `collector` VARCHAR (63) DEFAULT NULL COMMENT '采集人',
    `date_of_collection` TIMESTAMP DEFAULT NULL COMMENT '采集日期',
    `main_processor` VARCHAR (63) DEFAULT NULL COMMENT '主办人',
    `date_created` TIMESTAMP DEFAULT NULL COMMENT '创建日期',
    `case_type` VARCHAR (11) DEFAULT NULL COMMENT '案件类型',
    `case_category`VARCHAR (11) DEFAULT NULL COMMENT '案件类别',
    `case_no` VARCHAR(31) DEFAULT NULL COMMENT '案件编号',
    `monitoring_flag` VARCHAR (11) DEFAULT NULL COMMENT '有无监控',
    `track_flag` VARCHAR (11) DEFAULT NULL COMMENT '有无追踪',
    `finshed_flag` VARCHAR (11) DEFAULT NULL COMMENT '是否完成报告',
    `closed_date` TIMESTAMP DEFAULT NULL COMMENT '结案日期',


}