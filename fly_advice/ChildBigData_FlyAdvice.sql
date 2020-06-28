/*
 Navicat Premium Data Transfer

 Source Server         : flyadvice
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 192.168.101.48:3306
 Source Schema         : fly_advice

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 16/06/2020 12:02:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for airport
-- ----------------------------
DROP TABLE IF EXISTS `airport`;
CREATE TABLE `airport`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `IATA` char(3) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '国际航空运输协会机场代码',
  `ICAO` char(4) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '国际民航组织机场代码',
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `city` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `province` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `level` tinyint(2) NULL DEFAULT NULL,
  `latitude` double(6, 0) NULL DEFAULT NULL COMMENT '小数度，六位有效数字',
  `longitude` double(6, 0) NULL DEFAULT NULL COMMENT '小数度，六位有效数字',
  `website` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for history_flight
-- ----------------------------
DROP TABLE IF EXISTS `history_flight`;
CREATE TABLE `history_flight`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `flight_no` char(8) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '航班号',
  `aircraft_model` varchar(16) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '飞机型号',
  `start_time` datetime(0) NULL DEFAULT NULL,
  `end_time` datetime(0) NULL DEFAULT NULL,
  `source_airport` int(10) UNSIGNED NULL DEFAULT NULL,
  `destination_airport` int(10) UNSIGNED NULL DEFAULT NULL,
  `price` double(6, 0) NULL DEFAULT NULL,
  `stops` tinyint(4) NULL DEFAULT NULL COMMENT '经停次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `source_airport`(`source_airport`) USING BTREE,
  INDEX `destination_airport`(`destination_airport`) USING BTREE,
  CONSTRAINT `history_flight_ibfk_1` FOREIGN KEY (`source_airport`) REFERENCES `airport` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `history_flight_ibfk_2` FOREIGN KEY (`destination_airport`) REFERENCES `airport` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `distrust` tinyint(2) NOT NULL DEFAULT 0 COMMENT '下架拉黑',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nickname` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `email` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `admin` tinyint(2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_history
-- ----------------------------
DROP TABLE IF EXISTS `user_history`;
CREATE TABLE `user_history`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `plan_id` int(11) NOT NULL,
  `plan_order` tinyint(4) NOT NULL DEFAULT 0,
  `cabin_class` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `flight_id` int(10) UNSIGNED NOT NULL,
  `supplier_id` int(10) UNSIGNED NOT NULL,
  `flight_type` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `but_date` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `flight_id`(`flight_id`) USING BTREE,
  INDEX `supplier_id`(`supplier_id`) USING BTREE,
  CONSTRAINT `user_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_history_ibfk_2` FOREIGN KEY (`flight_id`) REFERENCES `history_flight` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_history_ibfk_3` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
