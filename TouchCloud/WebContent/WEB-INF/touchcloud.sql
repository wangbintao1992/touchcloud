/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50617
 Source Host           : localhost
 Source Database       : touchcloud

 Target Server Type    : MySQL
 Target Server Version : 50617
 File Encoding         : utf-8

 Date: 09/07/2016 22:12:44 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `a`
-- ----------------------------
DROP TABLE IF EXISTS `a`;
CREATE TABLE `a` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `a`
-- ----------------------------
BEGIN;
INSERT INTO `a` VALUES ('1', '1'), ('2', '2'), ('3', '3'), ('4', '4'), ('5', '5'), ('6', '6'), ('7', '7'), ('8', '8'), ('9', '9'), ('10', '10'), ('11', '11'), ('12', '12'), ('13', '13'), ('14', '14'), ('15', '15'), ('16', '16'), ('17', '17'), ('18', '18');
COMMIT;

-- ----------------------------
--  Table structure for `t_data_point`
-- ----------------------------
DROP TABLE IF EXISTS `t_data_point`;
CREATE TABLE `t_data_point` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` datetime DEFAULT NULL,
  `value` varchar(200) DEFAULT NULL,
  `device_id` int(11) NOT NULL,
  `sensor_id` int(11) NOT NULL,
  `type` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_data_point`
-- ----------------------------
BEGIN;
INSERT INTO `t_data_point` VALUES ('1', '2016-09-01 14:48:09', '1', '1', '1', null), ('2', '2016-09-28 14:48:18', '1', '1', '1', null), ('3', '2016-09-13 14:48:23', '1', '3', '3', null), ('4', '2016-08-31 14:48:27', '1', '1', '1', null), ('5', '2016-09-14 14:48:31', '1', '2', '2', null), ('6', '2016-09-04 14:48:35', '1', '1', '1', null), ('7', '2016-09-26 14:48:38', '1', '3', '3', null), ('8', '2016-09-07 14:48:42', '1', '1', '1', null), ('9', '2016-09-14 14:48:46', '1', '1', '1', null), ('10', '2016-09-15 14:48:50', '1', '1', '1', null), ('11', '2016-09-06 14:48:53', '1', '3', '3', null);
COMMIT;

-- ----------------------------
--  Table structure for `t_devices`
-- ----------------------------
DROP TABLE IF EXISTS `t_devices`;
CREATE TABLE `t_devices` (
  `device_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) NOT NULL,
  `about` varchar(20) DEFAULT NULL,
  `lat` double(20,8) DEFAULT NULL,
  `lng` double(20,8) DEFAULT NULL,
  `user_key` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_devices`
-- ----------------------------
BEGIN;
INSERT INTO `t_devices` VALUES ('2', 'asdaaaaaaaa', null, '39.92198400', '116.41826100', 'e329a8b9-ff3a-4c26-9d43-2fd83b8ca008'), ('3', 'test', 'hehe', '39.91653200', '116.42333200', 'e329a8b9-ff3a-4c26-9d43-2fd83b8ca008'), ('4', 'a', null, '39.93065800', '116.41978700', 'e329a8b9-ff3a-4c26-9d43-2fd83b8ca008'), ('5', 'a', null, '39.92092100', '116.41845500', 'e329a8b9-ff3a-4c26-9d43-2fd83b8ca008');
COMMIT;

-- ----------------------------
--  Table structure for `t_sensors`
-- ----------------------------
DROP TABLE IF EXISTS `t_sensors`;
CREATE TABLE `t_sensors` (
  `sensor_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `about` varchar(20) DEFAULT NULL,
  `device_id` int(11) DEFAULT NULL,
  `user_key` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`sensor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_sensors`
-- ----------------------------
BEGIN;
INSERT INTO `t_sensors` VALUES ('1', 'caadasd', '0', null, '1', null);
COMMIT;

-- ----------------------------
--  Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `name` varchar(20) DEFAULT NULL,
  `password` varchar(20) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `user_key` varchar(120) NOT NULL,
  `email` varchar(120) DEFAULT NULL,
  `sex` int(11) DEFAULT '0',
  `addr` varchar(120) DEFAULT NULL,
  `signature` varchar(120) DEFAULT NULL,
  `icon` varchar(120) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`user_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_user`
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES ('asd', 'asd', 'qwe', '74597b96-27f8-4286-8bc3-5757aa8da714', 'asdas', '1', 'asd', 'azxzxc', null, '2016-09-03 13:44:00');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
