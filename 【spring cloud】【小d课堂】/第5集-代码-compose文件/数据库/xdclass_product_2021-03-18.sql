# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 120.78.140.138 (MySQL 5.7.33)
# Database: xdclass_product
# Generation Time: 2021-03-18 13:57:12 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table banner
# ------------------------------------------------------------

DROP TABLE IF EXISTS `banner`;

CREATE TABLE `banner` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `img` varchar(524) DEFAULT NULL COMMENT '图片',
  `url` varchar(524) DEFAULT NULL COMMENT '跳转地址',
  `weight` int(11) DEFAULT NULL COMMENT '权重',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `banner` WRITE;
/*!40000 ALTER TABLE `banner` DISABLE KEYS */;

INSERT INTO `banner` (`id`, `img`, `url`, `weight`)
VALUES
	(1,'https://file.xdclass.net/video/2020/alibabacloud/zx-lbt.jpeg','https://m.xdclass.net/#/member',100),
	(2,'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/20%E5%B9%B4%E5%8F%8C11%E9%98%BF%E9%87%8C%E4%BA%91/fc-lbt.jpeg','https://www.aliyun.com/1111/pintuan-share?ptCode=MTcwMTY3MzEyMjc5MDU2MHx8MTE0fDE%3D&userCode=r5saexap',3),
	(3,'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/20%E5%B9%B4%E5%8F%8C11%E9%98%BF%E9%87%8C%E4%BA%91/FAN-lbu-vip.jpeg','https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/Nginx.jpeg',2);

/*!40000 ALTER TABLE `banner` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table product
# ------------------------------------------------------------

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(128) DEFAULT NULL COMMENT '标题',
  `cover_img` varchar(128) DEFAULT NULL COMMENT '封面图',
  `detail` varchar(256) DEFAULT '' COMMENT '详情',
  `old_amount` decimal(16,2) DEFAULT NULL COMMENT '老价格',
  `amount` decimal(16,2) DEFAULT NULL COMMENT '新价格',
  `stock` int(11) DEFAULT NULL COMMENT '库存',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `lock_stock` int(11) DEFAULT '0' COMMENT '锁定库存',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;

INSERT INTO `product` (`id`, `title`, `cover_img`, `detail`, `old_amount`, `amount`, `stock`, `create_time`, `lock_stock`)
VALUES
	(1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png','https://file.xdclass.net/video/2021/60-MLS/summary.jpeg',32.00,213.00,100,'2021-09-12 00:00:00',86),
	(2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png','https://file.xdclass.net/video/2021/59-Postman/summary.jpeg',432.00,42.00,100,'2021-03-12 00:00:00',86),
	(3,'技术人的杯子docker','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png','https://file.xdclass.net/video/2021/60-MLS/summary.jpeg',35.00,12.00,20,'2022-09-22 00:00:00',15),
	(4,'技术人的杯子git','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png','https://file.xdclass.net/video/2021/60-MLS/summary.jpeg',12.00,14.00,20,'2022-11-12 00:00:00',2);

/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table product_task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `product_task`;

CREATE TABLE `product_task` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` bigint(11) DEFAULT NULL COMMENT '商品id',
  `buy_num` int(11) DEFAULT NULL COMMENT '购买数量',
  `product_name` varchar(128) DEFAULT NULL COMMENT '商品标题',
  `lock_state` varchar(32) DEFAULT NULL COMMENT '锁定状态锁定LOCK  完成FINISH-取消CANCEL',
  `out_trade_no` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `product_task` WRITE;
/*!40000 ALTER TABLE `product_task` DISABLE KEYS */;

INSERT INTO `product_task` (`id`, `product_id`, `buy_num`, `product_name`, `lock_state`, `out_trade_no`, `create_time`)
VALUES
	(27,2,2,'老王-技术人的杯子Linux','LOCK','ngnDfcrExq4udzmUMFarN4piiAcxCtPh','2021-03-06 11:39:26'),
	(28,1,2,'老王-小滴课堂抱枕','LOCK','ngnDfcrExq4udzmUMFarN4piiAcxCtPh','2021-03-06 11:39:26'),
	(29,2,2,'老王-技术人的杯子Linux','FINISH','9uIakxXHAgr0X33YMIyUmCotbKADcSQ6','2021-03-06 11:57:49'),
	(30,1,2,'老王-小滴课堂抱枕','FINISH','9uIakxXHAgr0X33YMIyUmCotbKADcSQ6','2021-03-06 11:57:49'),
	(31,1,2,'老王-小滴课堂抱枕','CANCEL','9wnSzlQQ9KnCtyCJmsxLcMyZcs6MFmbW','2021-03-06 12:06:53'),
	(32,2,2,'老王-技术人的杯子Linux','CANCEL','9wnSzlQQ9KnCtyCJmsxLcMyZcs6MFmbW','2021-03-06 12:06:53'),
	(33,1,2,'老王-小滴课堂抱枕','FINISH','aHEuu6vnDIfJ9gMUeHkO3LwiCkgoLxmQ','2021-03-07 11:13:09'),
	(34,2,2,'老王-技术人的杯子Linux','FINISH','aHEuu6vnDIfJ9gMUeHkO3LwiCkgoLxmQ','2021-03-07 11:13:09'),
	(35,2,2,'老王-技术人的杯子Linux','LOCK','6eBBzmFJsmhWYezusEPb2s8VGbkj44SF','2021-03-07 11:17:12'),
	(36,1,2,'老王-小滴课堂抱枕','LOCK','6eBBzmFJsmhWYezusEPb2s8VGbkj44SF','2021-03-07 11:17:12'),
	(37,1,2,'老王-小滴课堂抱枕','CANCEL','1DkCt7ub8SM6DhuocbP6lDQ9O74Vpjsg','2021-03-07 11:29:30'),
	(38,2,2,'老王-技术人的杯子Linux','CANCEL','1DkCt7ub8SM6DhuocbP6lDQ9O74Vpjsg','2021-03-07 11:29:30'),
	(39,2,2,'老王-技术人的杯子Linux','CANCEL','WCiI6yrEVNGc5xlSfyOT5yoISNFvNh0F','2021-03-07 11:29:49'),
	(40,1,2,'老王-小滴课堂抱枕','CANCEL','WCiI6yrEVNGc5xlSfyOT5yoISNFvNh0F','2021-03-07 11:29:49'),
	(41,1,2,'老王-小滴课堂抱枕','FINISH','9TMSQAdlYmV8isUGHq5e3czaxT0yZzN3','2021-03-07 11:34:45'),
	(42,2,2,'老王-技术人的杯子Linux','LOCK','9TMSQAdlYmV8isUGHq5e3czaxT0yZzN3','2021-03-07 11:34:45'),
	(43,2,2,'老王-技术人的杯子Linux','LOCK','iVbHc46YeACQnF8AUjQGQZtLPzYPKYji','2021-03-07 11:35:11'),
	(44,1,2,'老王-小滴课堂抱枕','LOCK','iVbHc46YeACQnF8AUjQGQZtLPzYPKYji','2021-03-07 11:35:11'),
	(45,1,2,'老王-小滴课堂抱枕','LOCK','qq8rNpVUGP8nmfJefGVrWvy2qmHFomu6','2021-03-07 11:39:31'),
	(46,2,2,'老王-技术人的杯子Linux','LOCK','qq8rNpVUGP8nmfJefGVrWvy2qmHFomu6','2021-03-07 11:39:31'),
	(47,2,2,'老王-技术人的杯子Linux','LOCK','bCweaVkaaHtsxCRKRCKIDYlLr1FZczeo','2021-03-07 11:39:57'),
	(48,1,2,'老王-小滴课堂抱枕','LOCK','bCweaVkaaHtsxCRKRCKIDYlLr1FZczeo','2021-03-07 11:39:57'),
	(49,1,2,'老王-小滴课堂抱枕','FINISH','YEgyASJXWg7NUm0ftc3OLFL1L3aWmf0J','2021-03-07 11:48:11'),
	(50,2,2,'老王-技术人的杯子Linux','FINISH','YEgyASJXWg7NUm0ftc3OLFL1L3aWmf0J','2021-03-07 11:48:11'),
	(51,2,2,'老王-技术人的杯子Linux','CANCEL','bmPgKITSuqWoEWl9wccm0E4Bw1adoyHM','2021-03-07 11:48:27'),
	(52,1,2,'老王-小滴课堂抱枕','CANCEL','bmPgKITSuqWoEWl9wccm0E4Bw1adoyHM','2021-03-07 11:48:27'),
	(53,1,2,'老王-小滴课堂抱枕','LOCK','SahcCrnFuRTDa2UaiiGVXenP2CL6rO3I','2021-03-07 12:14:10'),
	(54,2,2,'老王-技术人的杯子Linux','LOCK','SahcCrnFuRTDa2UaiiGVXenP2CL6rO3I','2021-03-07 12:14:10'),
	(55,2,2,'老王-技术人的杯子Linux','FINISH','k1VB63mwJ31MTHkCECXOtOxs1JcytfMp','2021-03-07 12:22:40'),
	(56,1,2,'老王-小滴课堂抱枕','FINISH','k1VB63mwJ31MTHkCECXOtOxs1JcytfMp','2021-03-07 12:22:40'),
	(57,1,2,'老王-小滴课堂抱枕','CANCEL','fxnIMMIS8E0GOrYrMKbfVudD8b1xgoHG','2021-03-07 12:23:00'),
	(58,2,2,'老王-技术人的杯子Linux','CANCEL','fxnIMMIS8E0GOrYrMKbfVudD8b1xgoHG','2021-03-07 12:23:00'),
	(59,1,2,'老王-小滴课堂抱枕','FINISH','BuVNrPTMH2daY8mOYPJavESS27XmJcbe','2021-03-07 16:30:11'),
	(60,2,2,'老王-技术人的杯子Linux','FINISH','BuVNrPTMH2daY8mOYPJavESS27XmJcbe','2021-03-07 16:30:11'),
	(61,2,2,'老王-技术人的杯子Linux','FINISH','fVYsNXN9ZQzUC4j1D70wA7QFc8tqLrYe','2021-03-13 03:36:34'),
	(62,1,2,'老王-小滴课堂抱枕','FINISH','fVYsNXN9ZQzUC4j1D70wA7QFc8tqLrYe','2021-03-13 03:36:34'),
	(63,3,2,'技术人的杯子docker','FINISH','el2CL9c2R4ZgUNIvY21YK4gwC1LCNE1S','2021-03-14 02:58:09'),
	(64,1,2,'老王-小滴课堂抱枕','FINISH','el2CL9c2R4ZgUNIvY21YK4gwC1LCNE1S','2021-03-14 02:58:09'),
	(65,2,2,'老王-技术人的杯子Linux','FINISH','el2CL9c2R4ZgUNIvY21YK4gwC1LCNE1S','2021-03-14 02:58:09');

/*!40000 ALTER TABLE `product_task` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
