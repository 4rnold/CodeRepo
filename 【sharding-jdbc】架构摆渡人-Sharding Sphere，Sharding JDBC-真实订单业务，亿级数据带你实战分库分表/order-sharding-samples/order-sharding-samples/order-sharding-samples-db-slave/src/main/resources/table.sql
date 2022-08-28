CREATE TABLE `t_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `orderNo` varchar(64) DEFAULT NULL,
  `buyerId` bigint(11) DEFAULT NULL,
  `storeId` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_store` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `storeName` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;