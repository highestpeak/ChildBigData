CREATE TABLE `airport` (
`id` int UNSIGNED NOT NULL AUTO_INCREMENT,
`IATA` char(3) NULL COMMENT '国际航空运输协会机场代码',
`ICAO` char(4) NULL COMMENT '国际民航组织机场代码',
`name` varchar(256) NOT NULL,
`city` varchar(64) NOT NULL,
`province` varchar(64) NULL,
`Latitude` double(6,0) NOT NULL COMMENT '小数度，六位有效数字',
`Longitude` double(6,0) NOT NULL COMMENT '小数度，六位有效数字',
`website` tinytext NULL,
PRIMARY KEY (`id`) 
);
CREATE TABLE `AirLine` (
);
CREATE TABLE `history_flight` (
`id` int NOT NULL,
`flight_no` char(8) NOT NULL COMMENT '航班号',
`aircraft_model` varchar(16) NULL COMMENT '飞机型号',
`start_time` timestamp NULL ON UPDATE CURRENT_TIMESTAMP,
`end_time` timestamp NULL ON UPDATE CURRENT_TIMESTAMP,
`source_ariport` int NULL COMMENT '外键',
`destination_airport` int NULL COMMENT '外键',
`price` double(6,0) NULL,
`stops` tinyint NULL COMMENT '经停次数',
PRIMARY KEY (`id`) 
);
CREATE TABLE `user` (
`id` int NOT NULL,
`nickname` varchar(32) NULL,
`email` text NOT NULL,
`password` varchar(32) NOT NULL,
`admin` tinyint NOT NULL,
PRIMARY KEY (`id`) 
);
CREATE TABLE `supplier` (
`id` int NOT NULL,
`name` varchar(32) NOT NULL,
`distrust` tinyint NULL COMMENT '下架、拉黑',
PRIMARY KEY (`id`) 
);
CREATE TABLE `user_history` (
`id` int NOT NULL,
`plan_id` int NOT NULL COMMENT '飞行计划标记',
`plan_order` tinyint NOT NULL DEFAULT 0,
`cabin_class` varchar(32) NULL,
`user_id` int NOT NULL,
`flight_id` int NOT NULL,
`flight_type` varchar(32) NOT NULL,
`supplier_id` int NOT NULL,
`buy_date` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`) 
);

ALTER TABLE `history_flight` ADD CONSTRAINT `flight_source_foreign_key` FOREIGN KEY (`source_ariport`) REFERENCES `airport` (`id`);
ALTER TABLE `history_flight` ADD CONSTRAINT `flight_destination_foreign_key` FOREIGN KEY (`destination_airport`) REFERENCES `airport` (`id`);
ALTER TABLE `user_history` ADD CONSTRAINT `user_buy_history_foreign_key` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
ALTER TABLE `user_history` ADD CONSTRAINT `flight_buy_history_foreign_key` FOREIGN KEY (`flight_id`) REFERENCES `history_flight` (`id`);
ALTER TABLE `user_history` ADD CONSTRAINT `supplier_buy_history_foreign_key` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`);

