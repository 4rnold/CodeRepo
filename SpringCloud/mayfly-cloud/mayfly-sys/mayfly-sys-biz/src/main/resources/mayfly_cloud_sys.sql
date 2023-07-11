/*
 Navicat Premium Data Transfer

 Source Server         : self
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : mayfly_cloud_sys

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 09/07/2022 18:06:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` tinyint(2) NOT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `last_login_ip` varchar(32) DEFAULT NULL,
  `creator_id` bigint(20) NOT NULL DEFAULT '0',
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `modifier_id` bigint(20) NOT NULL DEFAULT '0',
  `modifier` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account
-- ----------------------------
BEGIN;
INSERT INTO `t_account` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, '2022-07-09 18:01:42', '0:0:0:0:0:0:0:1', 1, 'admin', 1, 'admin', '2020-03-05 07:41:18', '2022-07-09 18:01:42');
INSERT INTO `t_account` VALUES (9, 'test', 'e10adc3949ba59abbe56e057f20f883e', 0, '2022-04-06 16:49:22', NULL, 1, 'admin', 1, 'admin', '2019-08-21 11:30:33', '2022-06-17 22:46:28');
COMMIT;

-- ----------------------------
-- Table structure for t_account_role
-- ----------------------------
DROP TABLE IF EXISTS `t_account_role`;
CREATE TABLE `t_account_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `account_id` bigint(20) NOT NULL COMMENT '账号id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_account_role
-- ----------------------------
BEGIN;
INSERT INTO `t_account_role` VALUES (23, 3, 6, 'admin', 1, '2021-05-28 16:19:21');
INSERT INTO `t_account_role` VALUES (24, 2, 1, 'admin', 1, '2021-05-28 16:21:38');
INSERT INTO `t_account_role` VALUES (25, 1, 1, 'admin', 1, '2021-05-28 16:21:45');
INSERT INTO `t_account_role` VALUES (26, 4, 6, 'admin', 1, '2021-05-28 17:04:48');
INSERT INTO `t_account_role` VALUES (36, 9, 1, 'admin', 1, '2022-03-12 21:47:54');
INSERT INTO `t_account_role` VALUES (38, 9, 6, 'admin', 1, '2022-03-12 21:50:15');
COMMIT;

-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `type` tinyint(255) NOT NULL COMMENT '1：菜单路由；2：资源（按钮等）',
  `status` int(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '主要用于按钮等资源',
  `weight` int(11) DEFAULT NULL,
  `meta` varchar(255) DEFAULT NULL COMMENT '原数据',
  `creator_id` bigint(20) NOT NULL,
  `creator` varchar(255) NOT NULL,
  `modifier_id` bigint(20) NOT NULL,
  `modifier` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_resource
-- ----------------------------
BEGIN;
INSERT INTO `t_resource` VALUES (1, 0, 1, 1, '首页', '/home', 1, '{\"component\":\"Home\",\"icon\":\"HomeFilled\",\"isAffix\":true,\"isKeepAlive\":true,\"routeName\":\"Home\"}', 1, 'admin', 1, 'admin', '2021-05-25 16:44:41', '2021-05-27 09:12:56');
INSERT INTO `t_resource` VALUES (4, 0, 1, 1, '系统管理', '/sys', 4, '{\"icon\":\"Setting\",\"isKeepAlive\":true,\"redirect\":\"/sys/resources\",\"routeName\":\"sys\"}', 1, 'admin', 1, 'admin', '2021-05-26 15:20:20', '2021-06-08 14:20:34');
INSERT INTO `t_resource` VALUES (5, 4, 1, 1, '资源管理', 'resources', 3, '{\"component\":\"ResourceList\",\"icon\":\"Menu\",\"isKeepAlive\":true,\"routeName\":\"ResourceList\"}', 1, 'admin', 1, 'admin', '2021-05-26 15:23:07', '2021-06-08 11:27:55');
INSERT INTO `t_resource` VALUES (11, 4, 1, 1, '角色管理', 'roles', 2, '{\"component\":\"RoleList\",\"icon\":\"Menu\",\"isKeepAlive\":true,\"routeName\":\"RoleList\"}', 1, 'admin', 1, 'admin', '2021-05-27 11:15:35', '2021-06-03 09:59:41');
INSERT INTO `t_resource` VALUES (14, 4, 1, 1, '账号管理', 'accounts', 1, '{\"component\":\"AccountList\",\"icon\":\"Menu\",\"isKeepAlive\":true,\"routeName\":\"AccountList\"}', 1, 'admin', 1, 'admin', '2021-05-28 14:56:25', '2021-06-03 09:39:22');
INSERT INTO `t_resource` VALUES (19, 14, 2, 1, '角色分配按钮', 'account:saveRoles', 1, NULL, 1, 'admin', 1, 'admin', '2021-05-31 17:50:51', '2022-01-07 11:23:22');
INSERT INTO `t_resource` VALUES (20, 11, 2, 1, '分配菜单&权限按钮', 'role:saveResources', 1, NULL, 1, 'admin', 1, 'admin', '2021-05-31 17:51:41', '2021-05-31 19:33:37');
INSERT INTO `t_resource` VALUES (21, 14, 2, 1, '账号删除按钮', 'account:del', 2, 'null', 1, 'admin', 1, 'admin', '2021-05-31 18:02:01', '2022-06-18 13:34:01');
INSERT INTO `t_resource` VALUES (22, 11, 2, 1, '角色删除按钮', 'role:del', 2, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:02:29', '2021-05-31 19:33:38');
INSERT INTO `t_resource` VALUES (23, 11, 2, 1, '角色新增按钮', 'role:add', 3, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:02:44', '2021-05-31 19:33:39');
INSERT INTO `t_resource` VALUES (24, 11, 2, 1, '角色编辑按钮', 'role:update', 4, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:02:57', '2021-05-31 19:33:40');
INSERT INTO `t_resource` VALUES (25, 5, 2, 1, '资源新增按钮', 'resource:add', 1, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:03:33', '2021-05-31 19:31:47');
INSERT INTO `t_resource` VALUES (26, 5, 2, 1, '资源删除按钮', 'resource:del', 2, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:03:47', '2021-05-31 19:29:40');
INSERT INTO `t_resource` VALUES (27, 5, 2, 1, '资源编辑按钮', 'resource:update', 3, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:04:03', '2021-05-31 19:29:40');
INSERT INTO `t_resource` VALUES (28, 5, 2, 1, '资源禁用启用按钮', 'resource:changeStatus', 4, NULL, 1, 'admin', 1, 'admin', '2021-05-31 18:04:33', '2021-05-31 18:04:33');
INSERT INTO `t_resource` VALUES (29, 14, 2, 1, '账号添加按钮', 'account:add', 3, NULL, 1, 'admin', 1, 'admin', '2021-05-31 19:23:42', '2021-05-31 19:23:42');
INSERT INTO `t_resource` VALUES (30, 14, 2, 1, '账号编辑修改按钮', 'account:update', 4, NULL, 1, 'admin', 1, 'admin', '2021-05-31 19:23:58', '2021-05-31 19:23:58');
INSERT INTO `t_resource` VALUES (31, 14, 2, 1, '账号管理基本权限', 'account', 0, NULL, 1, 'admin', 1, 'admin', '2021-05-31 21:25:06', '2022-06-18 13:33:57');
INSERT INTO `t_resource` VALUES (32, 5, 2, 1, '资源管理基本权限', 'resource', 0, NULL, 1, 'admin', 1, 'admin', '2021-05-31 21:25:25', '2021-05-31 21:25:25');
INSERT INTO `t_resource` VALUES (33, 11, 2, 1, '角色管理基本权限', 'role', 0, NULL, 1, 'admin', 1, 'admin', '2021-05-31 21:25:40', '2021-05-31 21:25:40');
INSERT INTO `t_resource` VALUES (34, 14, 2, 1, '账号启用禁用按钮', 'account:changeStatus', 5, NULL, 1, 'admin', 1, 'admin', '2021-05-31 21:29:48', '2021-05-31 21:29:48');
INSERT INTO `t_resource` VALUES (39, 0, 1, 1, '个人中心', '/personal', 2, '{\"component\":\"Personal\",\"icon\":\"User\",\"isKeepAlive\":true,\"routeName\":\"Personal\"}', 1, 'admin', 1, 'admin', '2021-06-03 14:25:35', '2021-06-11 10:50:45');
INSERT INTO `t_resource` VALUES (48, 4, 1, 1, '操作日志', 'logs', 4, '{\"routeName\":\"LogList\",\"isKeepAlive\":true,\"component\":\"LogList\",\"icon\":\"Tickets\"}', 1, 'admin', 1, 'admin', '2021-09-29 15:44:04', '2021-09-29 15:46:38');
INSERT INTO `t_resource` VALUES (49, 48, 2, 1, '日志查看', 'log:list', 1, NULL, 1, 'admin', 1, 'admin', '2021-09-29 15:44:39', '2021-09-29 15:47:55');
INSERT INTO `t_resource` VALUES (50, 4, 1, 1, '服务管理', 'services', 5, '{\"routeName\":\"ServiceList\",\"isKeepAlive\":true,\"component\":\"ServiceList\",\"icon\":\"List\"}', 9, 'test', 1, 'admin', '2022-03-30 21:31:43', '2022-04-05 21:53:07');
INSERT INTO `t_resource` VALUES (51, 50, 2, 1, '基本权限', 'service:base', 1, NULL, 9, 'test', 1, 'admin', '2022-03-30 21:32:02', '2022-04-06 21:14:10');
INSERT INTO `t_resource` VALUES (52, 4, 1, 1, 'Api管理', 'apis', 6, '{\"routeName\":\"ApiList\",\"isKeepAlive\":true,\"component\":\"ApiList\",\"icon\":\"ScaleToOriginal\"}', 1, 'admin', 1, 'admin', '2022-04-05 21:47:55', '2022-04-05 21:53:18');
INSERT INTO `t_resource` VALUES (53, 52, 2, 1, '基本权限', 'api:base', 1, NULL, 1, 'admin', 1, 'admin', '2022-04-05 21:48:25', '2022-04-05 21:53:33');
COMMIT;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `code` varchar(36) NOT NULL COMMENT '角色标识',
  `status` tinyint(2) NOT NULL,
  `remark` varchar(45) DEFAULT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `modifier_id` bigint(20) unsigned NOT NULL,
  `modifier` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role` VALUES (1, '超级管理员', 'SUPER_ADMIN', 1, '具有全站所有权限', 1, 'admin', 1, 'admin', '2018-12-17 15:14:10', '2020-07-01 15:23:08');
INSERT INTO `t_role` VALUES (6, '系统管理员', 'SYS_ADMIN', 1, '拥有系统管理等权限...11', 1, 'admin', 1, 'admin', '2020-03-30 00:55:18', '2022-03-31 15:43:20');
COMMIT;

-- ----------------------------
-- Table structure for t_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_role_resource`;
CREATE TABLE `t_role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=429 DEFAULT CHARSET=utf8 COMMENT='角色资源表';

-- ----------------------------
-- Records of t_role_resource
-- ----------------------------
BEGIN;
INSERT INTO `t_role_resource` VALUES (1, 1, 1, 1, 'admin', '2021-05-27 15:07:39');
INSERT INTO `t_role_resource` VALUES (326, 1, 4, 1, 'admin', '2021-05-28 09:04:50');
INSERT INTO `t_role_resource` VALUES (327, 1, 5, 1, 'admin', '2021-05-28 09:04:50');
INSERT INTO `t_role_resource` VALUES (328, 1, 11, 1, 'admin', '2021-05-28 09:04:50');
INSERT INTO `t_role_resource` VALUES (335, 1, 14, 1, 'admin', '2021-05-28 17:42:21');
INSERT INTO `t_role_resource` VALUES (342, 6, 1, 1, 'admin', '2021-05-29 01:31:22');
INSERT INTO `t_role_resource` VALUES (357, 1, 19, 1, 'admin', '2021-05-31 17:52:08');
INSERT INTO `t_role_resource` VALUES (358, 1, 20, 1, 'admin', '2021-05-31 17:52:08');
INSERT INTO `t_role_resource` VALUES (359, 1, 21, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (360, 1, 22, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (361, 1, 23, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (362, 1, 24, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (363, 1, 25, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (364, 1, 26, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (365, 1, 27, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (366, 1, 28, 1, 'admin', '2021-05-31 18:05:04');
INSERT INTO `t_role_resource` VALUES (367, 1, 29, 1, 'admin', '2021-05-31 19:24:15');
INSERT INTO `t_role_resource` VALUES (368, 1, 30, 1, 'admin', '2021-05-31 19:24:15');
INSERT INTO `t_role_resource` VALUES (369, 1, 31, 1, 'admin', '2021-05-31 21:25:56');
INSERT INTO `t_role_resource` VALUES (370, 1, 32, 1, 'admin', '2021-05-31 21:25:56');
INSERT INTO `t_role_resource` VALUES (371, 1, 33, 1, 'admin', '2021-05-31 21:25:56');
INSERT INTO `t_role_resource` VALUES (372, 1, 34, 1, 'admin', '2021-05-31 21:30:06');
INSERT INTO `t_role_resource` VALUES (377, 1, 39, 1, 'admin', '2021-06-11 15:24:30');
INSERT INTO `t_role_resource` VALUES (386, 6, 39, 1, 'admin', '2021-06-11 15:25:36');
INSERT INTO `t_role_resource` VALUES (388, 6, 32, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (389, 6, 33, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (390, 6, 4, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (391, 6, 5, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (392, 6, 11, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (393, 6, 14, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (394, 6, 31, 1, 'admin', '2021-06-11 15:25:55');
INSERT INTO `t_role_resource` VALUES (399, 6, 19, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (400, 6, 20, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (401, 6, 21, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (402, 6, 22, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (403, 6, 23, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (404, 6, 24, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (407, 6, 27, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (408, 6, 28, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (409, 6, 29, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (410, 6, 30, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (411, 6, 34, 1, 'admin', '2021-06-11 15:27:13');
INSERT INTO `t_role_resource` VALUES (423, 1, 48, 1, 'admin', '2021-09-29 15:44:51');
INSERT INTO `t_role_resource` VALUES (424, 1, 49, 1, 'admin', '2021-09-29 15:44:51');
INSERT INTO `t_role_resource` VALUES (425, 1, 50, 9, 'test', '2022-03-30 21:32:11');
INSERT INTO `t_role_resource` VALUES (426, 1, 51, 9, 'test', '2022-03-30 21:32:11');
INSERT INTO `t_role_resource` VALUES (427, 1, 52, 1, 'admin', '2022-04-05 21:49:54');
INSERT INTO `t_role_resource` VALUES (428, 1, 53, 1, 'admin', '2022-04-05 21:49:54');
COMMIT;

-- ----------------------------
-- Table structure for t_service
-- ----------------------------
DROP TABLE IF EXISTS `t_service`;
CREATE TABLE `t_service` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `code` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '服务code',
  `name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '服务名',
  `status` tinyint(2) NOT NULL COMMENT '状态',
  `is_deleted` tinyint(2) NOT NULL COMMENT '是否删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `creator_id` bigint(64) NOT NULL,
  `update_time` datetime NOT NULL,
  `modifier` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `modifier_id` bigint(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务表';

-- ----------------------------
-- Records of t_service
-- ----------------------------
BEGIN;
INSERT INTO `t_service` VALUES (1, 'mayfly-sys', '系统服务', 1, 0, '2022-03-28 22:17:37', 'test', 9, '2022-06-18 13:41:03', 'admin', 1);
INSERT INTO `t_service` VALUES (2, 'mayfly-auth', '认证服务', 1, 0, '2022-03-28 22:21:01', 'test', 9, '2022-03-28 22:21:01', 'test', 9);
COMMIT;

-- ----------------------------
-- Table structure for t_service_api
-- ----------------------------
DROP TABLE IF EXISTS `t_service_api`;
CREATE TABLE `t_service_api` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `service_id` bigint(64) NOT NULL COMMENT '服务id',
  `service_code` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '服务code',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'api名称',
  `code_type` tinyint(2) DEFAULT NULL COMMENT 'code类型',
  `code` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'code',
  `method` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '请求方法',
  `uri` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '请求路径',
  `status` tinyint(4) NOT NULL COMMENT '状态',
  `is_deleted` tinyint(2) NOT NULL COMMENT '是否删除',
  `create_time` datetime NOT NULL,
  `creator_id` bigint(64) NOT NULL,
  `creator` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `update_time` datetime NOT NULL,
  `modifier_id` bigint(64) NOT NULL,
  `modifier` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务api信息';

-- ----------------------------
-- Records of t_service_api
-- ----------------------------
BEGIN;
INSERT INTO `t_service_api` VALUES (1, 1, 'mayfly-sys', '获取资源列表', 2, 'resource', 'GET', '/resources', 1, 0, '2022-03-30 17:12:14', 1, 'admin', '2022-06-09 17:30:08', 1, 'admin');
INSERT INTO `t_service_api` VALUES (2, 1, 'mayfly-sys', '获取角色列表', 2, 'role', 'GET', '/roles', 1, 0, '2022-03-30 20:28:25', 1, 'admin', '2022-06-09 14:44:46', 1, 'admin');
INSERT INTO `t_service_api` VALUES (3, 1, 'mayfly-sys', '修改角色信息', 2, 'role:update', 'PUT', '/roles/{roleId}', 1, 0, '2022-03-31 10:07:09', 1, 'admin', '2022-06-09 17:30:03', 1, 'admin');
INSERT INTO `t_service_api` VALUES (4, 1, 'mayfly-sys', '获取服务列表', 2, 'service:base', 'GET', '/services', 1, 0, '2022-04-06 21:16:44', 1, 'admin', '2022-06-09 17:29:57', 1, 'admin');
INSERT INTO `t_service_api` VALUES (5, 1, 'mayfly-sys', '获取api列表', 2, 'api:base', 'GET', '/service/apis', 1, 0, '2022-04-06 21:18:11', 1, 'admin', '2022-06-09 17:29:51', 1, 'admin');
INSERT INTO `t_service_api` VALUES (6, 1, 'mayfly-sys', '创建api', 2, 'api:base', 'POST', '/service/apis', 1, 0, '2022-04-06 21:34:02', 1, 'admin', '2022-06-09 17:29:39', 1, 'admin');
INSERT INTO `t_service_api` VALUES (7, 1, 'mayfly-sys', '修改api信息', 2, 'api:base', 'PUT', '/service/apis/{id}', 0, 0, '2022-04-06 21:35:00', 1, 'admin', '2022-06-09 17:29:01', 1, 'admin');
INSERT INTO `t_service_api` VALUES (8, 1, 'mayfly-sys', '删除api信息', 2, 'api:base', 'DELETE', '/service/apis/{id}', 1, 0, '2022-04-06 21:35:37', 1, 'admin', '2022-06-09 17:29:44', 1, 'admin');
INSERT INTO `t_service_api` VALUES (9, 1, 'mayfly-sys', '创建服务', 2, 'service:base', 'POST', '/services', 1, 0, '2022-04-06 22:02:16', 1, 'admin', '2022-06-09 17:29:21', 1, 'admin');
INSERT INTO `t_service_api` VALUES (10, 1, 'mayfly-sys', '修改服务信息', 2, 'service:base', 'PUT', '/services/{id}', 1, 0, '2022-04-06 22:03:06', 1, 'admin', '2022-06-09 17:29:14', 1, 'admin');
INSERT INTO `t_service_api` VALUES (11, 1, 'mayfly-sys', '删除服务信息', 2, 'service:base', 'DELETE', '/services/{id}', 1, 0, '2022-04-06 22:03:44', 1, 'admin', '2022-06-09 17:29:07', 1, 'admin');
INSERT INTO `t_service_api` VALUES (12, 1, 'mayfly-sys', '查看资源详情', 2, 'resource', 'GET', '/resources/{id}', 1, 0, '2022-04-06 22:25:06', 1, 'admin', '2022-06-09 17:28:55', 1, 'admin');
COMMIT;

-- ----------------------------
-- Table structure for t_sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_operation_log`;
CREATE TABLE `t_sys_operation_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(2) DEFAULT NULL COMMENT '1：正常操作；2：异常信息',
  `operation` varchar(1024) NOT NULL,
  `creator_id` bigint(20) unsigned DEFAULT NULL,
  `creator` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1218 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_sys_operation_log
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_operation_log` VALUES (1176, 4, '[uid=1, uname=admin][修改资源状态]\n--> mayfly.sys.biz.controller.ResourceController#changeStatus(arg0: 31, arg1: 0) -> 377ms', 1, 'admin', '2022-06-18 13:33:56');
INSERT INTO `t_sys_operation_log` VALUES (1177, 4, '[uid=1, uname=admin][修改资源状态]\n--> mayfly.sys.biz.controller.ResourceController#changeStatus(arg0: 31, arg1: 1) -> 17ms', 1, 'admin', '2022-06-18 13:33:57');
INSERT INTO `t_sys_operation_log` VALUES (1178, 4, '[uid=1, uname=admin][修改资源状态]\n--> mayfly.sys.biz.controller.ResourceController#changeStatus(arg0: 21, arg1: 0) -> 16ms', 1, 'admin', '2022-06-18 13:34:00');
INSERT INTO `t_sys_operation_log` VALUES (1179, 4, '[uid=1, uname=admin][修改资源状态]\n--> mayfly.sys.biz.controller.ResourceController#changeStatus(arg0: 21, arg1: 1) -> 16ms', 1, 'admin', '2022-06-18 13:34:01');
INSERT INTO `t_sys_operation_log` VALUES (1180, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 77ms', 1, 'admin', '2022-06-18 13:34:31');
INSERT INTO `t_sys_operation_log` VALUES (1181, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 33ms', 1, 'admin', '2022-06-18 13:34:32');
INSERT INTO `t_sys_operation_log` VALUES (1182, 4, '[uid=1, uname=admin][修改服务]\n--> mayfly.sys.biz.controller.ServiceController#update(arg0: 1, arg1: ServiceForm(code=mayfly-sys, name=系统服务, status=0)) -> 30ms\n----------change----------\nstatus: 1 -> 0\n--------------------------', 1, 'admin', '2022-06-18 13:41:03');
INSERT INTO `t_sys_operation_log` VALUES (1183, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 40ms', 1, 'admin', '2022-06-18 13:41:04');
INSERT INTO `t_sys_operation_log` VALUES (1184, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 43ms', 1, 'admin', '2022-06-18 13:52:02');
INSERT INTO `t_sys_operation_log` VALUES (1185, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 42ms', 1, 'admin', '2022-06-18 13:52:02');
INSERT INTO `t_sys_operation_log` VALUES (1186, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 29ms', 1, 'admin', '2022-06-18 13:52:14');
INSERT INTO `t_sys_operation_log` VALUES (1187, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 11ms', 1, 'admin', '2022-06-18 13:53:19');
INSERT INTO `t_sys_operation_log` VALUES (1188, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 32ms', 1, 'admin', '2022-06-18 13:53:21');
INSERT INTO `t_sys_operation_log` VALUES (1189, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 33ms', 1, 'admin', '2022-06-18 13:53:21');
INSERT INTO `t_sys_operation_log` VALUES (1190, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 27ms', 1, 'admin', '2022-06-18 13:54:35');
INSERT INTO `t_sys_operation_log` VALUES (1191, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 43ms', 1, 'admin', '2022-06-18 13:54:35');
INSERT INTO `t_sys_operation_log` VALUES (1192, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 45ms', 1, 'admin', '2022-06-18 13:54:35');
INSERT INTO `t_sys_operation_log` VALUES (1193, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 15ms', 1, 'admin', '2022-06-18 14:03:40');
INSERT INTO `t_sys_operation_log` VALUES (1194, 4, '[查询账号信息]\n--> mayfly.sys.biz.remote.AccountRemoteServiceImpl#getByQuery(arg0: AccountQueryDTO(username=admin)) -> 24ms', NULL, NULL, '2022-06-18 14:08:30');
INSERT INTO `t_sys_operation_log` VALUES (1195, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 14ms', 1, 'admin', '2022-06-18 14:09:32');
INSERT INTO `t_sys_operation_log` VALUES (1196, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 47ms', 1, 'admin', '2022-06-18 14:09:32');
INSERT INTO `t_sys_operation_log` VALUES (1197, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 50ms', 1, 'admin', '2022-06-18 14:09:32');
INSERT INTO `t_sys_operation_log` VALUES (1198, 4, '[查询账号信息]\n--> mayfly.sys.biz.remote.AccountRemoteServiceImpl#getByQuery(arg0: AccountQueryDTO(username=test)) -> 11ms', NULL, NULL, '2022-06-18 14:16:34');
INSERT INTO `t_sys_operation_log` VALUES (1199, 4, '[查询账号信息]\n--> mayfly.sys.biz.remote.AccountRemoteServiceImpl#getByQuery(arg0: AccountQueryDTO(username=admin)) -> 8ms', NULL, NULL, '2022-06-18 14:16:40');
INSERT INTO `t_sys_operation_log` VALUES (1200, 4, '[查询账号信息]\n--> mayfly.sys.biz.remote.AccountRemoteServiceImpl#getByQuery(arg0: AccountQueryDTO(username=admin)) -> 7ms', NULL, NULL, '2022-06-18 14:16:47');
INSERT INTO `t_sys_operation_log` VALUES (1201, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 15ms', 1, 'admin', '2022-06-18 14:16:54');
INSERT INTO `t_sys_operation_log` VALUES (1202, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 55ms', 1, 'admin', '2022-06-18 14:16:55');
INSERT INTO `t_sys_operation_log` VALUES (1203, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 51ms', 1, 'admin', '2022-06-18 14:16:55');
INSERT INTO `t_sys_operation_log` VALUES (1204, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 29ms', 1, 'admin', '2022-06-18 14:28:22');
INSERT INTO `t_sys_operation_log` VALUES (1205, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 52ms', 1, 'admin', '2022-06-18 14:28:25');
INSERT INTO `t_sys_operation_log` VALUES (1206, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 49ms', 1, 'admin', '2022-06-18 14:28:25');
INSERT INTO `t_sys_operation_log` VALUES (1207, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 13ms', 1, 'admin', '2022-06-18 14:28:27');
INSERT INTO `t_sys_operation_log` VALUES (1208, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 23ms', 1, 'admin', '2022-06-18 14:28:28');
INSERT INTO `t_sys_operation_log` VALUES (1209, 4, '[查询账号信息]\n--> mayfly.sys.biz.remote.AccountRemoteServiceImpl#getByQuery(arg0: AccountQueryDTO(username=admin)) -> 878ms', NULL, NULL, '2022-07-03 19:29:43');
INSERT INTO `t_sys_operation_log` VALUES (1210, 4, '[查询账号信息]\n--> mayfly.sys.biz.remote.AccountRemoteServiceImpl#getByQuery(arg0: AccountQueryDTO(username=admin)) -> 81ms', NULL, NULL, '2022-07-09 18:01:42');
INSERT INTO `t_sys_operation_log` VALUES (1211, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 26ms', 1, 'admin', '2022-07-09 18:02:09');
INSERT INTO `t_sys_operation_log` VALUES (1212, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 32ms', 1, 'admin', '2022-07-09 18:02:10');
INSERT INTO `t_sys_operation_log` VALUES (1213, 4, '[uid=1, uname=admin][获取服务列表信息]\n--> mayfly.sys.biz.controller.ServiceController#query(arg0: ServiceQuery(code=null, name=null, updateTime=null)) -> 31ms', 1, 'admin', '2022-07-09 18:02:10');
INSERT INTO `t_sys_operation_log` VALUES (1214, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 24ms', 1, 'admin', '2022-07-09 18:02:12');
INSERT INTO `t_sys_operation_log` VALUES (1215, 4, '[uid=1, uname=admin][获取服务api列表]\n--> mayfly.sys.biz.controller.ServiceApiController#query(arg0: ServiceApiQuery(serviceId=null, serviceCode=null, updateTime=null)) -> 25ms', 1, 'admin', '2022-07-09 18:02:14');
INSERT INTO `t_sys_operation_log` VALUES (1216, 4, '[查询账号信息]\n--> mayfly.sys.biz.remote.AccountRemoteServiceImpl#getByQuery(arg0: AccountQueryDTO(username=test11)) -> 9ms', NULL, NULL, '2022-07-09 18:05:55');
INSERT INTO `t_sys_operation_log` VALUES (1217, 4, '[查询账号信息]\n--> mayfly.sys.biz.remote.AccountRemoteServiceImpl#getByQuery(arg0: AccountQueryDTO(username=test11)) -> 10ms', NULL, NULL, '2022-07-09 18:06:06');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
