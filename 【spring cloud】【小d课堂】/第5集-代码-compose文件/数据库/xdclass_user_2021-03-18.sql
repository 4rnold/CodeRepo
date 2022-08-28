# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 120.78.140.138 (MySQL 5.7.33)
# Database: xdclass_user
# Generation Time: 2021-03-18 13:57:19 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table address
# ------------------------------------------------------------

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `default_status` int(1) DEFAULT NULL COMMENT '是否默认收货地址：0->否；1->是',
  `receive_name` varchar(64) DEFAULT NULL COMMENT '收发货人姓名',
  `phone` varchar(64) DEFAULT NULL COMMENT '收货人电话',
  `province` varchar(64) DEFAULT NULL COMMENT '省/直辖市',
  `city` varchar(64) DEFAULT NULL COMMENT '市',
  `region` varchar(64) DEFAULT NULL COMMENT '区',
  `detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电商-公司收发货地址表';

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;

INSERT INTO `address` (`id`, `user_id`, `default_status`, `receive_name`, `phone`, `province`, `city`, `region`, `detail_address`, `create_time`)
VALUES
	(39,3,0,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁1号','2021-02-05 10:48:45'),
	(40,3,0,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁2号','2021-02-05 10:49:32'),
	(43,9,0,'小滴课堂-隔壁大哥','12321312321','广东省','广州市','天河区','运营中心-老王隔壁1号','2021-02-05 10:48:45'),
	(44,10,0,'小滴课堂-隔壁小文','12321312321','广东省','广州市','天河区','运营中心-老王隔壁1号','2021-02-05 10:48:45'),
	(45,36,0,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁5号','2021-03-01 17:36:21'),
	(46,36,0,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁88号','2021-03-01 17:36:29'),
	(47,36,0,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁99号','2021-03-01 17:36:34'),
	(48,36,1,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁5号','2021-03-07 16:20:34'),
	(49,37,1,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁5号-555','2021-03-13 11:34:41'),
	(50,40,0,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁53333号','2021-03-14 10:52:52'),
	(51,40,1,'小滴课堂-隔壁老王','12321312321','广东省','广州市','天河区','运营中心-老王隔壁533311113号','2021-03-14 10:52:58');

/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table undo_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `undo_log`;

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '昵称',
  `pwd` varchar(124) DEFAULT NULL COMMENT '密码',
  `head_img` varchar(524) DEFAULT NULL COMMENT '头像',
  `slogan` varchar(524) DEFAULT NULL COMMENT '用户签名',
  `sex` tinyint(2) DEFAULT '1' COMMENT '0表示女，1表示男',
  `points` int(10) DEFAULT '0' COMMENT '积分',
  `create_time` datetime DEFAULT NULL,
  `mail` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `secret` varchar(12) DEFAULT NULL COMMENT '盐，用于个人敏感信息处理',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mail_idx` (`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `name`, `pwd`, `head_img`, `slogan`, `sex`, `points`, `create_time`, `mail`, `secret`)
VALUES
	(3,'Anna小姐姐','$1$V908ssVg$HIp7IF/MbiUAsyQ.ZwXAb/','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg','人生需要动态规划，学习需要贪心算法',1,0,'2021-02-04 10:31:07','7946669181@qq.com','$1$V908ssVg'),
	(4,'老王','$1$3znxHOtF$u1yNaVN6XIA/MLqB5RYgP0','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg','人生需要动态规划，学习需要贪心算法',1,0,'2021-02-23 18:17:20','7946669218@qq.com','$1$3znxHOtF'),
	(7,'小d','$1$MqBfDGFj$LYVccjLN7TZNfOWESgw4e.','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg','人生需要动态规划，学习需要贪心算法',1,0,'2021-02-23 22:55:15','794666911118@qq.com','$1$MqBfDGFj'),
	(36,'二当家小D-分布式事务---','$1$XDlvKGlo$7ZTOKwOOFwV1YmNjON6iL1','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg','人生需要动态规划，学习需要贪心算法',1,0,'2021-02-24 21:52:48','7946663918@qq.com','$1$XDlvKGlo'),
	(37,'Rancher测试','$1$m6onNPfr$.L8IIen/XfpN2V8FS3S0B/','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/03/13/e4f463d20e424f7cbe766d4d02206b66.jpeg','人生需要动态规划，学习需要贪心算法',1,0,'2021-03-13 11:33:25','794662426918@qq.com','$1$m6onNPfr'),
	(40,'小滴课堂','$1$0DnHF7Hd$MTv.7QuEQ.oxSrWh7cxM6.','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg','人生需要动态规划，学习需要贪心算法',1,0,'2021-03-14 10:51:47','794666918@qq.com','$1$0DnHF7Hd');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
