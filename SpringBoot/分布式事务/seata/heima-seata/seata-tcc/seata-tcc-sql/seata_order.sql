/*
Navicat MySQL Data Transfer

Source Server         : local_connection
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : hmily_order

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2020-07-21 11:24:11
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` varchar(100) COLLATE utf8mb4_bin NOT NULL,
  `create_time` datetime NOT NULL,
  `number` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  `status` tinyint(4) NOT NULL,
  `product_id` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `total_amount` decimal(10,0) NOT NULL,
  `count` int(4) NOT NULL,
  `user_id` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('1', '2020-07-15 15:47:21', '1283307146968535040', '3', '1', '1', '1', '10000');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
