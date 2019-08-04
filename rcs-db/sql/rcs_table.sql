--
-- 用户基本信息表
--
DROP TABLE IF EXISTS `rcs_user`;

CREATE TABLE `rcs_user`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(63) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(63) NOT NULL DEFAULT '' COMMENT '用户密码',
  `avatar` varchar(255) DEFAULT '''' COMMENT '头像图片',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  `role_ids` varchar(127) DEFAULT '[]' COMMENT '角色列表',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


--
-- 角色表 //目前可以提供死的接口
--

DROP TABLE IF EXISTS `rcs_role`;
CREATE TABLE `rcs_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(63) NOT NULL COMMENT '角色名称',
  `desc` varchar(1023) DEFAULT NULL COMMENT '角色描述',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';


--
-- 权限表
--
DROP TABLE IF EXISTS `rcs_permission`;
CREATE TABLE `rcs_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `permission` varchar(63) DEFAULT NULL COMMENT '权限',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COMMENT='权限表';



--
-- 区域表结构
--
DROP TABLE IF EXISTS `rcs_region`;
CREATE TABLE `rcs_region` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT '0' COMMENT '行政区域父ID，例如区县的pid指向市，市的pid指向省，省的pid则是0',
  `name` varchar(120) NOT NULL DEFAULT '' COMMENT '行政区域名称',
  `type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '行政区域类型，1--省, 2--市,3--区',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`pid`),
  KEY `region_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=3232 DEFAULT CHARSET=utf8mb4 COMMENT='行政区域表';



--
-- 系统操作日志表，用于记录用户操作信息，便于统计
--

DROP TABLE IF EXISTS `rcs_sys_log`;

CREATE TABLE `rcs_sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作员',
  `user_id` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作员ID',
  `ip` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作员的ip地址',
  `type` int(11) DEFAULT NULL COMMENT '操作类型',
  `action` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作动作',
  `comment` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作信息',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';





DROP TABLE IF EXISTS `rcs_invoice`;

CREATE TABLE `rcs_invoice` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `identifier_code` varchar(50)  DEFAULT NULL COMMENT '发票识别码',
  `invoice_no` varchar(50)  DEFAULT NULL COMMENT '发表编号',
  `subject` varchar(45)  DEFAULT NULL COMMENT '发票科目',
  `custom_id` varchar(50) DEFAULT NULL COMMENT '客户ID',
  `custom_name` varchar(50)  DEFAULT NULL COMMENT '客户名称',
  `amount` double(16,2)  DEFAULT NULL COMMENT '合同金额',
  `billing_date` datetime DEFAULT NULL COMMENT '开票时间',
  `contract_id` int(32) DEFAULT NULL COMMENT '关联的合同ID',
  `write_off_type` varchar(10) DEFAULT NULL COMMENT '关联的核销类型',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发票记录表';



DROP TABLE IF EXISTS `rcs_write_off`;

CREATE TABLE `rcs_write_off` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `custom_id` varchar(50) DEFAULT NULL COMMENT '客户ID',
  `custom_name` varchar(50)  DEFAULT NULL COMMENT '客户名称',
  `payment_amount` double(16,2)  DEFAULT NULL COMMENT '支付金额',
  `payment_date` datetime DEFAULT NULL COMMENT '支付时间',
  `ref_contract_id` int(32) DEFAULT NULL COMMENT '关联的合同ID',
  `source` varchar(10) DEFAULT "0" COMMENT '记录来源',
  `settlement_id` varchar(50)  DEFAULT NULL COMMENT '关联核销结算ID，用于区分绑定到哪条记录上',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='核销记录表';


DROP TABLE IF EXISTS `rcs_contract`;
CREATE TABLE `rcs_contract` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `contract_no` varchar(50)  DEFAULT NULL COMMENT '发表编号',
  `custom_id` varchar(50) DEFAULT NULL COMMENT '客户ID',
  `custom_name` varchar(50)  DEFAULT NULL COMMENT '客户名称',
  `total_amount` double(16,2)  DEFAULT NULL COMMENT '合同金额',
  `first_payment` double(16,2)  DEFAULT NULL COMMENT '首付',
  `period_payment` double(16,2) DEFAULT NULL COMMENT '每期应付',
  `receivable_num` int(6) DEFAULT NULL COMMENT '期数',
  `effective_date` datetime DEFAULT NULL COMMENT '合同生效日期',

  `contract_type` varchar(10) DEFAULT NULL COMMENT '合同类型',
  `contract_status` varchar(10) DEFAULT NULL COMMENT '合同状态',
  `product_type` varchar(10) DEFAULT NULL COMMENT '产品类型',


  `contact_name` varchar(50)  DEFAULT NULL COMMENT '联系人',
  `custom_tel` varchar(20)  DEFAULT NULL COMMENT '联系手机号',
  `custom_email` varchar(50)  DEFAULT NULL COMMENT '邮箱',

  `sales_id` int(32) DEFAULT NULL COMMENT '销售经理ID',
  `sales_no` varchar(50)  DEFAULT NULL COMMENT '销售员工号',
  `sales_name` varchar(50) DEFAULT NULL COMMENT '销售名',
  `team_id` int(32) DEFAULT NULL COMMENT '团队ID',

  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同记录表';

