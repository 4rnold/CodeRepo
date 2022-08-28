# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 120.78.140.138 (MySQL 5.7.33)
# Database: xdclass_order
# Generation Time: 2021-03-18 13:57:05 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table product_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `product_order`;

CREATE TABLE `product_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `out_trade_no` varchar(64) DEFAULT NULL COMMENT '订单唯一标识',
  `state` varchar(11) DEFAULT NULL COMMENT 'NEW 未支付订单,PAY已经支付订单,CANCEL超时取消订单',
  `create_time` datetime DEFAULT NULL COMMENT '订单生成时间',
  `total_amount` decimal(16,2) DEFAULT NULL COMMENT '订单总金额',
  `pay_amount` decimal(16,2) DEFAULT NULL COMMENT '订单实际支付价格',
  `pay_type` varchar(64) DEFAULT NULL COMMENT '支付类型，微信-银行-支付宝',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `head_img` varchar(524) DEFAULT NULL COMMENT '头像',
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户id',
  `del` int(5) DEFAULT '0' COMMENT '0表示未删除，1表示已经删除',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `order_type` varchar(32) DEFAULT NULL COMMENT '订单类型 DAILY普通单，PROMOTION促销订单',
  `receiver_address` varchar(1024) DEFAULT NULL COMMENT '收货地址 json存储',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `product_order` WRITE;
/*!40000 ALTER TABLE `product_order` DISABLE KEYS */;

INSERT INTO `product_order` (`id`, `out_trade_no`, `state`, `create_time`, `total_amount`, `pay_amount`, `pay_type`, `nickname`, `head_img`, `user_id`, `del`, `update_time`, `order_type`, `receiver_address`)
VALUES
	(2452,'9uIakxXHAgr0X33YMIyUmCotbKADcSQ6','PAY','2021-03-06 11:57:49',510.00,505.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2453,'9wnSzlQQ9KnCtyCJmsxLcMyZcs6MFmbW','CANCEL','2021-03-06 12:06:53',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2454,'aHEuu6vnDIfJ9gMUeHkO3LwiCkgoLxmQ','PAY','2021-03-07 11:13:10',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2455,'6eBBzmFJsmhWYezusEPb2s8VGbkj44SF','CANCEL','2021-03-07 11:17:12',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2456,'1DkCt7ub8SM6DhuocbP6lDQ9O74Vpjsg','CANCEL','2021-03-07 11:29:30',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2457,'WCiI6yrEVNGc5xlSfyOT5yoISNFvNh0F','CANCEL','2021-03-07 11:29:49',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2458,'9TMSQAdlYmV8isUGHq5e3czaxT0yZzN3','PAY','2021-03-07 11:34:45',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2459,'iVbHc46YeACQnF8AUjQGQZtLPzYPKYji','CANCEL','2021-03-07 11:35:12',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2460,'qq8rNpVUGP8nmfJefGVrWvy2qmHFomu6','CANCEL','2021-03-07 11:39:31',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2461,'bCweaVkaaHtsxCRKRCKIDYlLr1FZczeo','CANCEL','2021-03-07 11:39:58',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2462,'YEgyASJXWg7NUm0ftc3OLFL1L3aWmf0J','PAY','2021-03-07 11:48:11',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2463,'bmPgKITSuqWoEWl9wccm0E4Bw1adoyHM','CANCEL','2021-03-07 11:48:27',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2464,'SahcCrnFuRTDa2UaiiGVXenP2CL6rO3I','PAY','2021-03-07 12:14:10',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2465,'k1VB63mwJ31MTHkCECXOtOxs1JcytfMp','PAY','2021-03-07 12:22:41',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2466,'fxnIMMIS8E0GOrYrMKbfVudD8b1xgoHG','CANCEL','2021-03-07 12:23:01',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2467,'BuVNrPTMH2daY8mOYPJavESS27XmJcbe','PAY','2021-03-07 16:30:11',510.00,510.00,'ALIPAY','二当家小D-分布式事务---','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',36,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁5号\",\"id\":45,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":36}'),
	(2468,'fVYsNXN9ZQzUC4j1D70wA7QFc8tqLrYe','PAY','2021-03-13 11:36:35',510.00,505.00,'ALIPAY','Rancher测试','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/03/13/e4f463d20e424f7cbe766d4d02206b66.jpeg',37,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":1,\"detailAddress\":\"运营中心-老王隔壁5号-555\",\"id\":49,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":37}'),
	(2469,'el2CL9c2R4ZgUNIvY21YK4gwC1LCNE1S','PAY','2021-03-14 10:58:10',534.00,529.00,'ALIPAY','小滴课堂','https://xdclass-1024shop-img.oss-cn-shenzhen.aliyuncs.com/user/2021/02/03/39473aa1029a430298ac2620dd819962.jpeg',40,0,NULL,'DAILY','{\"city\":\"广州市\",\"defaultStatus\":0,\"detailAddress\":\"运营中心-老王隔壁53333号\",\"id\":50,\"phone\":\"12321312321\",\"province\":\"广东省\",\"receiveName\":\"小滴课堂-隔壁老王\",\"region\":\"天河区\",\"userId\":40}');

/*!40000 ALTER TABLE `product_order` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table product_order_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `product_order_item`;

CREATE TABLE `product_order_item` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_order_id` bigint(11) DEFAULT NULL COMMENT '订单号',
  `out_trade_no` varchar(32) DEFAULT NULL,
  `product_id` bigint(11) DEFAULT NULL COMMENT '产品id',
  `product_name` varchar(128) DEFAULT NULL COMMENT '商品名称',
  `product_img` varchar(524) DEFAULT NULL COMMENT '商品图片',
  `buy_num` int(11) DEFAULT NULL COMMENT '购买数量',
  `create_time` datetime DEFAULT NULL,
  `total_amount` decimal(16,2) DEFAULT NULL COMMENT '购物项商品总价格',
  `amount` decimal(16,0) DEFAULT NULL COMMENT '购物项商品单价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `product_order_item` WRITE;
/*!40000 ALTER TABLE `product_order_item` DISABLE KEYS */;

INSERT INTO `product_order_item` (`id`, `product_order_id`, `out_trade_no`, `product_id`, `product_name`, `product_img`, `buy_num`, `create_time`, `total_amount`, `amount`)
VALUES
	(218,2452,'9uIakxXHAgr0X33YMIyUmCotbKADcSQ6',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-06 11:57:50',84.00,42),
	(219,2452,'9uIakxXHAgr0X33YMIyUmCotbKADcSQ6',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-06 11:57:50',426.00,213),
	(220,2453,'9wnSzlQQ9KnCtyCJmsxLcMyZcs6MFmbW',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-06 12:06:53',426.00,213),
	(221,2453,'9wnSzlQQ9KnCtyCJmsxLcMyZcs6MFmbW',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-06 12:06:53',84.00,42),
	(222,2454,'aHEuu6vnDIfJ9gMUeHkO3LwiCkgoLxmQ',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:13:10',426.00,213),
	(223,2454,'aHEuu6vnDIfJ9gMUeHkO3LwiCkgoLxmQ',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:13:10',84.00,42),
	(224,2455,'6eBBzmFJsmhWYezusEPb2s8VGbkj44SF',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:17:12',84.00,42),
	(225,2455,'6eBBzmFJsmhWYezusEPb2s8VGbkj44SF',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:17:12',426.00,213),
	(226,2456,'1DkCt7ub8SM6DhuocbP6lDQ9O74Vpjsg',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:29:30',426.00,213),
	(227,2456,'1DkCt7ub8SM6DhuocbP6lDQ9O74Vpjsg',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:29:30',84.00,42),
	(228,2457,'WCiI6yrEVNGc5xlSfyOT5yoISNFvNh0F',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:29:49',84.00,42),
	(229,2457,'WCiI6yrEVNGc5xlSfyOT5yoISNFvNh0F',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:29:49',426.00,213),
	(230,2458,'9TMSQAdlYmV8isUGHq5e3czaxT0yZzN3',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:34:45',426.00,213),
	(231,2458,'9TMSQAdlYmV8isUGHq5e3czaxT0yZzN3',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:34:45',84.00,42),
	(232,2459,'iVbHc46YeACQnF8AUjQGQZtLPzYPKYji',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:35:12',84.00,42),
	(233,2459,'iVbHc46YeACQnF8AUjQGQZtLPzYPKYji',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:35:12',426.00,213),
	(234,2460,'qq8rNpVUGP8nmfJefGVrWvy2qmHFomu6',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:39:31',426.00,213),
	(235,2460,'qq8rNpVUGP8nmfJefGVrWvy2qmHFomu6',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:39:31',84.00,42),
	(236,2461,'bCweaVkaaHtsxCRKRCKIDYlLr1FZczeo',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:39:58',84.00,42),
	(237,2461,'bCweaVkaaHtsxCRKRCKIDYlLr1FZczeo',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:39:58',426.00,213),
	(238,2462,'YEgyASJXWg7NUm0ftc3OLFL1L3aWmf0J',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:48:12',426.00,213),
	(239,2462,'YEgyASJXWg7NUm0ftc3OLFL1L3aWmf0J',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:48:12',84.00,42),
	(240,2463,'bmPgKITSuqWoEWl9wccm0E4Bw1adoyHM',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:48:27',84.00,42),
	(241,2463,'bmPgKITSuqWoEWl9wccm0E4Bw1adoyHM',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 11:48:27',426.00,213),
	(242,2464,'SahcCrnFuRTDa2UaiiGVXenP2CL6rO3I',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 12:14:10',426.00,213),
	(243,2464,'SahcCrnFuRTDa2UaiiGVXenP2CL6rO3I',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 12:14:10',84.00,42),
	(244,2465,'k1VB63mwJ31MTHkCECXOtOxs1JcytfMp',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 12:22:41',84.00,42),
	(245,2465,'k1VB63mwJ31MTHkCECXOtOxs1JcytfMp',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 12:22:41',426.00,213),
	(246,2466,'fxnIMMIS8E0GOrYrMKbfVudD8b1xgoHG',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 12:23:01',426.00,213),
	(247,2466,'fxnIMMIS8E0GOrYrMKbfVudD8b1xgoHG',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 12:23:01',84.00,42),
	(248,2467,'BuVNrPTMH2daY8mOYPJavESS27XmJcbe',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 16:30:11',426.00,213),
	(249,2467,'BuVNrPTMH2daY8mOYPJavESS27XmJcbe',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-07 16:30:11',84.00,42),
	(250,2468,'fVYsNXN9ZQzUC4j1D70wA7QFc8tqLrYe',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-13 11:36:35',84.00,42),
	(251,2468,'fVYsNXN9ZQzUC4j1D70wA7QFc8tqLrYe',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-13 11:36:35',426.00,213),
	(252,2469,'el2CL9c2R4ZgUNIvY21YK4gwC1LCNE1S',3,'技术人的杯子docker','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-14 10:58:10',24.00,12),
	(253,2469,'el2CL9c2R4ZgUNIvY21YK4gwC1LCNE1S',1,'老王-小滴课堂抱枕','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-14 10:58:10',426.00,213),
	(254,2469,'el2CL9c2R4ZgUNIvY21YK4gwC1LCNE1S',2,'老王-技术人的杯子Linux','https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png',2,'2021-03-14 10:58:10',84.00,42);

/*!40000 ALTER TABLE `product_order_item` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
