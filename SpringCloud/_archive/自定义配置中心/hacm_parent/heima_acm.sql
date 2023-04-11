/*
Navicat MySQL Data Transfer

Source Server         : local_connection
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : heima_acm

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2020-05-30 22:48:15
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `acm_project_group`
-- ----------------------------
DROP TABLE IF EXISTS `acm_project_group`;
CREATE TABLE `acm_project_group` (
  `id` varchar(40) NOT NULL,
  `project_group_name` varchar(50) DEFAULT NULL,
  `project_manager_name` varchar(64) DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of acm_project_group
-- ----------------------------

-- ----------------------------
-- Table structure for `acm_user`
-- ----------------------------
DROP TABLE IF EXISTS `acm_user`;
CREATE TABLE `acm_user` (
  `id` varchar(40) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL COMMENT '不能重复,可为中文',
  `password` varchar(64) DEFAULT NULL COMMENT 'shiro MD5密码32位',
  `gender` varchar(10) DEFAULT NULL,
  `age` int(2) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `telephone` varchar(11) DEFAULT NULL,
  `email` varchar(60) DEFAULT NULL,
  `degree` int(2) DEFAULT NULL,
  `state` varchar(10) DEFAULT NULL COMMENT '1启用0停用',
  `remark` varchar(255) DEFAULT NULL,
  `project_group_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of acm_user
-- ----------------------------
INSERT INTO `acm_user` VALUES ('1', 'test', '1234', '', '30', '1990-05-25', '13987654321', 'test@itheima.com', '1', '1', 'test', '1');

-- ----------------------------
-- Table structure for `config_info`
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` varchar(200) NOT NULL,
  `env_name` varchar(100) NOT NULL,
  `project_name` varchar(100) NOT NULL,
  `cluster_number` varchar(100) NOT NULL,
  `service_name` varchar(100) NOT NULL,
  `config_detail` varchar(4000) DEFAULT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `project_group` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES ('1263157308729462784', 'dev', 'tensquare', 'NO1', 'tensquare-user', '{\"jwt.config.key\":\"itcast\",\"spring.datasource.url\":\"jdbc:mysql://192.168.242.142:3306/tensquare_user?useUnicode=true&characterEncoding=UTF8\",\"spring.rabbitmq.host\":\"192.168.242.142\",\"spring.datasource.password\":\"123456\",\"eureka.client.service-url.defaultZone\":\"http://localhost:6868/eureka\",\"spring.application.name\":\"tensquare-user\",\"jwt.config.ttl\":\"36000000\",\"spring.datasource.username\":\"root\",\"project.name\":\"tensquare\",\"spring.datasource.driverClassName\":\"com.mysql.jdbc.Driver\",\"env.name\":\"dev\",\"spring.redis.host\":\"192.168.242.142\",\"spring.jpa.database\":\"MySQL\",\"projectGroup\":\"itheima\",\"server.port\":\"9008\",\"eureka.instance.prefer-ip-address\":\"true\",\"cluster.number\":\"NO2\",\"spring.jpa.show-sql\":\"true\"}', '1', 'itheima', '2020-05-21 01:19:09', '2020-05-21 01:19:10');
INSERT INTO `config_info` VALUES ('1263163507453595648', 'dev', 'tensquare', 'NO1', 'tensquare-base', '{\"spring.datasource.url\":\"jdbc:mysql://127.0.0.1:3306/tensquare_base?characterEncoding=UTF8\",\"spring.jpa.generate-ddl\":\"false\",\"spring.rabbitmq.host\":\"192.168.32.128\",\"service.name\":\"tensquare-base\",\"spring.datasource.password\":\"1234\",\"system.ip\":\"127.0.0.1\",\"eureka.client.service-url.defaultZone\":\"http://localhost:6868/eureka\",\"spring.application.name\":\"tensquare-base\",\"spring.datasource.username\":\"root\",\"project.name\":\"tensquare\",\"spring.datasource.driverClassName\":\"com.mysql.jdbc.Driver\",\"env.name\":\"dev\",\"spring.redis.host\":\"192.168.32.128\",\"spring.jpa.database\":\"mysql\",\"server.port\":\"9001\",\"eureka.instance.prefer-ip-address\":\"true\",\"cluster.number\":\"NO2\",\"spring.jpa.show-sql\":\"true\"}', '1', 'itheima', '2020-05-25 23:16:43', '2020-05-25 23:16:47');
INSERT INTO `config_info` VALUES ('1265215266661470208', 'dev', 'tensquare', 'NO1', 'tenquare-sms', '{\"url\":\"localhost\",\"sms.ip\":\"localhost\",\"test\":\"demo1\"}', '1', 'itheima', '2020-05-26 17:36:40', '2020-05-26 17:36:40');
INSERT INTO `config_info` VALUES ('1265230669022367744', 'dev', 'tensquare', 'NO1', 'tensquare-friend', '{\"project.name\":\"tensquare\",\"spring.datasource.url\":\"jdbc:mysql://192.168.242.142:3306/tensquare_friend?characterEncoding=utf-8\",\"spring.datasource.username\":\"root\",\"jwt.config.key\":\"itcast\",\"spring.jpa.database\":\"MySQL\",\"service.name\":\"tensquare-friend\",\"spring.datasource.driverClassName\":\"com.mysql.jdbc.Driver\",\"server.port\":9010,\"spring.datasource.password\":123456,\"spring.rabbitmq.host\":\"192.168.242.142\",\"eureka.client.service-url.defaultZone\":\"http://localhost:6868/eureka\",\"spring.application.name\":\"tensquare-friend\",\"eureka.instance.prefer-ip-address\":true,\"cluster.number\":\"NO1\",\"env.name\":\"dev\",\"spring.jpa.show-sql\":true}', '1', 'itheima', '2020-05-26 18:37:52', '2020-05-26 18:37:52');
