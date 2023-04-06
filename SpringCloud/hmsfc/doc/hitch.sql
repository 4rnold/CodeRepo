/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 116.62.213.90:3306
 Source Schema         : hitch

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 02/08/2021 14:04:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `username` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `useralias` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户姓名',
  `password` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '密码',
  `phone` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '手机号码',
  `role` tinyint(4) NOT NULL COMMENT '角色 乘客：0 司机：1',
  `avatar` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户头像',
  `status` tinyint(4) DEFAULT NULL COMMENT '实名认证:0 未认证,1:已认证',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户表';

-- ----------------------------
-- Table structure for t_attachment
-- ----------------------------
DROP TABLE IF EXISTS `t_attachment`;
CREATE TABLE `t_attachment` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '文件名称',
  `url` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '文件URL',
  `lenght` int(9) NOT NULL COMMENT '文件大小',
  `ext` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '文件扩展名',
  `md5` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件签名',
  `status` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_authentication
-- ----------------------------
DROP TABLE IF EXISTS `t_authentication`;
CREATE TABLE `t_authentication` (
  `id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `useralias` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `phone` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '手机号码',
  `birth` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '出生年月',
  `personal_photo` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '个人照片',
  `card_id` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '身份证号码',
  `card_id_front_photo` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '身份证正面照片',
  `card_id_back_photo` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '身份证背面照片',
  `status` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '认证状态 未认证：0认证成功：1认证失败：2',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户认证';

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `passenger_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '乘客ID',
  `passenger_stroke_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '乘客行程ID',
  `driver_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '司机ID',
  `driver_stroke_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '司机行程ID',
  `distance` int(11) NOT NULL COMMENT '米',
  `estimated_time` int(11) NOT NULL COMMENT '秒',
  `cost` float NOT NULL COMMENT '价格',
  `status` tinyint(4) NOT NULL COMMENT '状态',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `driver_passenger_union` (`driver_stroke_id`,`passenger_stroke_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='订单表';

-- ----------------------------
-- Table structure for t_payment
-- ----------------------------
DROP TABLE IF EXISTS `t_payment`;
CREATE TABLE `t_payment` (
  `id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `order_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '订单号',
  `prepay_id` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '预支付ID',
  `pay_info` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '支付信息',
  `amount` float NOT NULL COMMENT '金额',
  `channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道 支付宝：1微信：2',
  `transaction_order_num` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '交易订单号',
  `status` tinyint(4) DEFAULT NULL COMMENT '支付状态 未支付：0\r\n已支付：1\r\n未确认：2',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='支付交易';

-- ----------------------------
-- Table structure for t_stroke
-- ----------------------------
DROP TABLE IF EXISTS `t_stroke`;
CREATE TABLE `t_stroke` (
  `id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `publisher_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '发布人ID',
  `role` tinyint(4) NOT NULL COMMENT '发布人角色 乘客：1\r\n司机：2',
  `start_geo_lng` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '起点GEO 经度',
  `start_geo_lat` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '起点GEO 维度',
  `end_geo_lng` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '终点GEO 经度',
  `end_geo_lat` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '终点GEO 纬度',
  `start_addr` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT '起点地址',
  `end_addr` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT '终点地址',
  `quantity` tinyint(4) NOT NULL COMMENT '数量 对于乘客来说是-同行人数\r\n对于司机来说是-空座数',
  `departure_time` datetime NOT NULL COMMENT '出发时间',
  `quick_confirm` tinyint(4) DEFAULT NULL COMMENT '快速确认 是否开启快速确认\r\n对于乘客就是-闪电确认\r\n对于司机就是-自动接单',
  `status` tinyint(4) DEFAULT NULL COMMENT '行程状态 未接单：0\r\n已接单：1\r\n已出发：2\r\n行程中：3\r\n已结束：4\r\n已超时：5',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='行程表';

-- ----------------------------
-- Table structure for t_vehicle
-- ----------------------------
DROP TABLE IF EXISTS `t_vehicle`;
CREATE TABLE `t_vehicle` (
  `id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `car_number` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '车牌号',
  `car_front_photo` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '车牌前部照片',
  `car_back_photo` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '车牌背部照片',
  `car_side_photo` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '车牌侧部照片',
  `purchase_date` date DEFAULT NULL COMMENT '购车日期',
  `phone` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '所属人手机号码',
  `status` tinyint(4) DEFAULT NULL COMMENT '认证状态 未认证：0\r\n认证成功：1\r\n认证失败：2',
  `REVISION` int(11) DEFAULT NULL COMMENT '乐观锁',
  `CREATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `CREATED_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATED_BY` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `UPDATED_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='车辆信息表';

SET FOREIGN_KEY_CHECKS = 1;
