/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : shardingjdbc

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 17/08/2023 23:44:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gsp_order_1
-- ----------------------------
DROP TABLE IF EXISTS `gsp_order_1`;
CREATE TABLE `gsp_order_1` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gsp_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'gsp订单编号',
  `confirm_status` tinyint unsigned NOT NULL COMMENT '内部确认状态1：待确认，2：部分确认，3：全部确认',
  `gsp_confirm_status` tinyint unsigned NOT NULL COMMENT 'gsp确认状态1：待确认，3：已确认  4：已拒绝，5：已取消',
  `requirement_company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '采购公司名称',
  `requirement_company_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '采购公司编码',
  `purchase_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '采购组织',
  `purchase_group_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '采购组名称',
  `purchase_group_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '采购组编码',
  `cert_date` datetime(3) DEFAULT NULL COMMENT '凭证日期',
  `confirm_deadline` datetime(3) DEFAULT NULL COMMENT '确认截至时间',
  `customer_consignee` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '收货联系人',
  `gsp_audit_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'gsp审批状态',
  `create_oms` bit(4) DEFAULT b'0' COMMENT '是否创建OMS订单',
  `gsp_cancel_time` datetime(3) DEFAULT NULL COMMENT 'gsp取消时间',
  `gsp_confirm_time` datetime(3) DEFAULT NULL COMMENT 'gsp确认时间',
  `gsp_reject_time` datetime(3) DEFAULT NULL COMMENT 'gsp拒绝时间',
  `reject_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'gsp订单拒绝原因',
  `sales_assistant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '负责的销售助理',
  `sales_assistant_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '销售助理userCode',
  `gsp_order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'gsp订单类型',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'GSP' COMMENT '数据来源：GSP、GSP_V2、GSP_ZN、OFFLINE_IMPORT',
  `version` smallint unsigned NOT NULL COMMENT '版本号，每次GSP订单变更确认之后会触发这里变更',
  `active` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '逻辑删除：0删除，1有效。',
  `creator` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `creator_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updater` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  `updater_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_gsp_order_no` (`gsp_order_no`) USING BTREE,
  KEY `requirement_company_code` (`requirement_company_code`) USING BTREE,
  KEY `create_time_confirm_status` (`confirm_status`,`create_time`) USING BTREE,
  KEY `confirm_status_requirement_company_code` (`confirm_status`,`requirement_company_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=898328140558893057 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='gsp订单';

-- ----------------------------
-- Table structure for gsp_order_2
-- ----------------------------
DROP TABLE IF EXISTS `gsp_order_2`;
CREATE TABLE `gsp_order_2` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gsp_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'gsp订单编号',
  `confirm_status` tinyint unsigned NOT NULL COMMENT '内部确认状态1：待确认，2：部分确认，3：全部确认',
  `gsp_confirm_status` tinyint unsigned NOT NULL COMMENT 'gsp确认状态1：待确认，3：已确认  4：已拒绝，5：已取消',
  `requirement_company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '采购公司名称',
  `requirement_company_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '采购公司编码',
  `purchase_org` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '采购组织',
  `purchase_group_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '采购组名称',
  `purchase_group_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '采购组编码',
  `cert_date` datetime(3) DEFAULT NULL COMMENT '凭证日期',
  `confirm_deadline` datetime(3) DEFAULT NULL COMMENT '确认截至时间',
  `customer_consignee` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '收货联系人',
  `gsp_audit_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'gsp审批状态',
  `create_oms` bit(4) DEFAULT b'0' COMMENT '是否创建OMS订单',
  `gsp_cancel_time` datetime(3) DEFAULT NULL COMMENT 'gsp取消时间',
  `gsp_confirm_time` datetime(3) DEFAULT NULL COMMENT 'gsp确认时间',
  `gsp_reject_time` datetime(3) DEFAULT NULL COMMENT 'gsp拒绝时间',
  `reject_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'gsp订单拒绝原因',
  `sales_assistant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '负责的销售助理',
  `sales_assistant_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '销售助理userCode',
  `gsp_order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'gsp订单类型',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'GSP' COMMENT '数据来源：GSP、GSP_V2、GSP_ZN、OFFLINE_IMPORT',
  `version` smallint unsigned NOT NULL COMMENT '版本号，每次GSP订单变更确认之后会触发这里变更',
  `active` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '逻辑删除：0删除，1有效。',
  `creator` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `creator_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updater` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  `updater_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_gsp_order_no` (`gsp_order_no`) USING BTREE,
  KEY `requirement_company_code` (`requirement_company_code`) USING BTREE,
  KEY `create_time_confirm_status` (`confirm_status`,`create_time`) USING BTREE,
  KEY `confirm_status_requirement_company_code` (`confirm_status`,`requirement_company_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=898328140533727234 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='gsp订单';

-- ----------------------------
-- Table structure for hub_order
-- ----------------------------
DROP TABLE IF EXISTS `hub_order`;
CREATE TABLE `hub_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内部订单号',
  `platform_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方订单号',
  `platform_status` tinyint NOT NULL COMMENT '平台方订单状态',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方渠道编码，标识具体平台方',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注说明',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `discount` decimal(10,6) DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接入平台方订单数据对应表';

-- ----------------------------
-- Table structure for sharding_mod_0
-- ----------------------------
DROP TABLE IF EXISTS `sharding_mod_0`;
CREATE TABLE `sharding_mod_0` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内部订单号',
  `platform_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方订单号',
  `platform_status` tinyint NOT NULL COMMENT '平台方订单状态',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方渠道编码，标识具体平台方',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注说明',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `discount` decimal(10,6) DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=899053649483792386 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接入平台方订单数据对应表';

-- ----------------------------
-- Table structure for sharding_mod_1
-- ----------------------------
DROP TABLE IF EXISTS `sharding_mod_1`;
CREATE TABLE `sharding_mod_1` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内部订单号',
  `platform_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方订单号',
  `platform_status` tinyint NOT NULL COMMENT '平台方订单状态',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方渠道编码，标识具体平台方',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注说明',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `discount` decimal(10,6) DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=899053649685118977 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接入平台方订单数据对应表';

-- ----------------------------
-- Table structure for sharding_mod_2
-- ----------------------------
DROP TABLE IF EXISTS `sharding_mod_2`;
CREATE TABLE `sharding_mod_2` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内部订单号',
  `platform_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方订单号',
  `platform_status` tinyint NOT NULL COMMENT '平台方订单状态',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方渠道编码，标识具体平台方',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注说明',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `discount` decimal(10,6) DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=899053649764810753 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接入平台方订单数据对应表';

-- ----------------------------
-- Table structure for sharding_mod_3
-- ----------------------------
DROP TABLE IF EXISTS `sharding_mod_3`;
CREATE TABLE `sharding_mod_3` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内部订单号',
  `platform_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方订单号',
  `platform_status` tinyint NOT NULL COMMENT '平台方订单状态',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方渠道编码，标识具体平台方',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注说明',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `discount` decimal(10,6) DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=899053649638981634 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接入平台方订单数据对应表';

-- ----------------------------
-- Table structure for sharding_mod_4
-- ----------------------------
DROP TABLE IF EXISTS `sharding_mod_4`;
CREATE TABLE `sharding_mod_4` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内部订单号',
  `platform_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方订单号',
  `platform_status` tinyint NOT NULL COMMENT '平台方订单状态',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '平台方渠道编码，标识具体平台方',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注说明',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `discount` decimal(10,6) DEFAULT NULL COMMENT '折扣',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=899053649718673410 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接入平台方订单数据对应表';

SET FOREIGN_KEY_CHECKS = 1;
