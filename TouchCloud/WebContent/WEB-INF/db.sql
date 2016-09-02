/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.14-log : Database - touchcloud
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`touchcloud` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `touchcloud`;

/*Table structure for table `a` */

DROP TABLE IF EXISTS `a`;

CREATE TABLE `a` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Data for the table `a` */

insert  into `a`(`id`,`name`) values (1,'1'),(2,'2'),(3,'3'),(4,'4'),(5,'5'),(6,'6'),(7,'7'),(8,'8'),(9,'9'),(10,'10'),(11,'11'),(12,'12'),(13,'13'),(14,'14'),(15,'15'),(16,'16'),(17,'17'),(18,'18');

/*Table structure for table `t_data_point` */

DROP TABLE IF EXISTS `t_data_point`;

CREATE TABLE `t_data_point` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` datetime DEFAULT NULL,
  `value` varchar(200) DEFAULT NULL,
  `device_id` int(11) NOT NULL,
  `sensor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Data for the table `t_data_point` */

insert  into `t_data_point`(`id`,`timestamp`,`value`,`device_id`,`sensor_id`) values (1,'2016-09-01 14:48:09','1',1,1),(2,'2016-09-28 14:48:18','1',1,1),(3,'2016-09-13 14:48:23','1',3,3),(4,'2016-08-31 14:48:27','1',1,1),(5,'2016-09-14 14:48:31','1',2,2),(6,'2016-09-04 14:48:35','1',1,1),(7,'2016-09-26 14:48:38','1',3,3),(8,'2016-09-07 14:48:42','1',1,1),(9,'2016-09-14 14:48:46','1',1,1),(10,'2016-09-15 14:48:50','1',1,1),(11,'2016-09-06 14:48:53','1',3,3);

/*Table structure for table `t_devices` */

DROP TABLE IF EXISTS `t_devices`;

CREATE TABLE `t_devices` (
  `device_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) NOT NULL,
  `about` varchar(20) DEFAULT NULL,
  `lat` double(10,3) DEFAULT NULL,
  `lng` double(10,3) DEFAULT NULL,
  `user_key` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_devices` */

insert  into `t_devices`(`device_id`,`title`,`about`,`lat`,`lng`,`user_key`) values (2,'asdaaaaaaaa',NULL,0.000,0.000,'e329a8b9-ff3a-4c26-9d43-2fd83b8ca008');

/*Table structure for table `t_sensors` */

DROP TABLE IF EXISTS `t_sensors`;

CREATE TABLE `t_sensors` (
  `sensor_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `about` varchar(20) DEFAULT NULL,
  `device_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`sensor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `t_sensors` */

insert  into `t_sensors`(`sensor_id`,`title`,`type`,`about`,`device_id`) values (1,'caadasd',0,NULL,1);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `name` varchar(20) DEFAULT NULL,
  `password` varchar(20) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `user_key` varchar(120) NOT NULL,
  `email` varchar(20) DEFAULT NULL,
  `sex` int(11) DEFAULT '0',
  `addr` varchar(20) DEFAULT NULL,
  `signature` varchar(20) DEFAULT NULL,
  `icon` varchar(120) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`user_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
