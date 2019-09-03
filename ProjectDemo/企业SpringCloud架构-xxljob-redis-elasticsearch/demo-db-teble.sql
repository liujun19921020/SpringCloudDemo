CREATE database if NOT EXISTS `cs1_db` default character set utf8 collate utf8_general_ci;
use `cs1_db`;



CREATE TABLE `ORDER_AMAZON` (
	`order_id` varchar(50) NOT NULL COMMENT '系统订单号',
	`ship_name` char(30) NOT NULL COMMENT '收件人',
  `ship_phone` char(30) NOT NULL COMMENT '收件电话',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';


INSERT INTO `ORDER_AMAZON`(`order_id`, `ship_name`, `ship_phone`) VALUES ('AM181213014486', '张三', '13977889900');
INSERT INTO `ORDER_AMAZON`(`order_id`, `ship_name`, `ship_phone`) VALUES ('AM171229006868', '李四', '13977889911');
INSERT INTO `ORDER_AMAZON`(`order_id`, `ship_name`, `ship_phone`) VALUES ('AM180222004731', '王五', '13977889922');


CREATE TABLE `ORDER_AMAZON_DETAIL` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) NOT NULL COMMENT '系统订单号',
  `sku` varchar(100) NOT NULL COMMENT 'SKU',
  `site` varchar(20) NOT NULL COMMENT '购买站:美国站,德国站等',
  `order_status` varchar(4) NOT NULL COMMENT '订单状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单详情表';


INSERT INTO `ORDER_AMAZON_DETAIL`(`id`, `order_id`, `sku`, `site`, `order_status`) VALUES (1, 'AM181213014486', 'JM00056', '美国站', '2');
INSERT INTO `ORDER_AMAZON_DETAIL`(`id`, `order_id`, `sku`, `site`, `order_status`) VALUES (2, 'AM171229006868', 'JM00012', '德国站', '2');
INSERT INTO `ORDER_AMAZON_DETAIL`(`id`, `order_id`, `sku`, `site`, `order_status`) VALUES (3, 'AM171229006868', 'JM00012', '德国站', '3');
INSERT INTO `ORDER_AMAZON_DETAIL`(`id`, `order_id`, `sku`, `site`, `order_status`) VALUES (4, 'AM171229006868', 'JM00012', '德国站', '3');
INSERT INTO `ORDER_AMAZON_DETAIL`(`id`, `order_id`, `sku`, `site`, `order_status`) VALUES (5, 'AM180222004731', 'JM00038', '美国站', '2');

commit;


CREATE database if NOT EXISTS `cs2_db` default character set utf8 collate utf8_general_ci;
use `cs2_db`;

CREATE TABLE `LJ_PRODUCT` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sku` varchar(130) DEFAULT NULL,
  `product_name` varchar(100) NOT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品表';


INSERT INTO `LJ_PRODUCT`(`id`, `sku`, `product_name`) VALUES (1, 'JM00012', 'Desktop microfono a condensatore');
INSERT INTO `LJ_PRODUCT`(`id`, `sku`, `product_name`) VALUES (2, 'JM00038', 'Lanterna LED Portatile');
INSERT INTO `LJ_PRODUCT`(`id`, `sku`, `product_name`) VALUES (3, 'JM00056', 'ZJchao 3V 1A AC Adapter to DC Power Adapter 5.5/2.1mm');

commit;

