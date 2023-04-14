/*
Navicat MySQL Data Transfer

Source Server         : local_connection
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : hmily_account

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2020-07-21 11:24:02
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `balance` decimal(10,0) NOT NULL COMMENT '用户余额',
  `freeze_amount` decimal(10,0) NOT NULL COMMENT '冻结金额，扣款暂存余额',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', '10000', '983', '0', '2017-09-18 14:54:22', '2020-07-21 11:22:46');

-- ----------------------------
-- Table structure for `undo_log`
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  `context` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_unionkey` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
