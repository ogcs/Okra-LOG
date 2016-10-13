/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.13-log : Database - okra_log
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `log_hero` */

CREATE TABLE `log_hero` (
  `logDate` datetime DEFAULT NULL COMMENT '格式 YYYY-MM-DD HH:MM:SS',
  `openId` varchar(50) DEFAULT NULL,
  `io` tinyint(4) DEFAULT NULL COMMENT '产出(1)/消耗(0)',
  `ioType` int(11) DEFAULT NULL COMMENT '途径',
  `itemId` int(11) DEFAULT NULL COMMENT '道具id',
  `itemType` int(11) DEFAULT NULL COMMENT '道具类型',
  `value` int(11) DEFAULT NULL COMMENT '道具变动数量',
  `leftCount` int(11) DEFAULT NULL COMMENT '变更之后道具数量',
  KEY `openId` (`openId`),
  KEY `logDate` (`logDate`,`openId`,`io`,`ioType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='英雄日志表';

/*Table structure for table `log_item` */

CREATE TABLE `log_item` (
  `logDate` datetime DEFAULT NULL COMMENT '格式 YYYY-MM-DD HH:MM:SS',
  `openId` varchar(50) DEFAULT NULL,
  `io` tinyint(4) DEFAULT NULL COMMENT '产出(1)/消耗(0)',
  `ioType` int(11) DEFAULT NULL COMMENT '途径',
  `itemId` int(11) DEFAULT NULL COMMENT '道具id',
  `itemType` int(11) DEFAULT NULL COMMENT '道具类型',
  `value` int(11) DEFAULT NULL COMMENT '道具变动数量',
  `leftCount` int(11) DEFAULT NULL COMMENT '变更之后道具数量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='道具日志表';

/*Table structure for table `log_level` */

CREATE TABLE `log_level` (
  `logDate` datetime DEFAULT NULL COMMENT '格式 YYYY-MM-DD HH:MM:SS',
  `openId` varchar(50) DEFAULT NULL,
  `io` tinyint(4) DEFAULT NULL COMMENT '产出(1)/消耗(0)',
  `ioType` int(11) DEFAULT NULL COMMENT '途径',
  `type` int(11) DEFAULT NULL COMMENT '等级类型',
  `value` int(11) DEFAULT NULL COMMENT '涉及值',
  `afterLevel` int(11) DEFAULT NULL COMMENT '变更之后等级',
  `afterExp` int(11) DEFAULT NULL COMMENT '变更之后经验值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='等级变更日志表';

/*Table structure for table `log_money` */

CREATE TABLE `log_money` (
  `logDate` datetime DEFAULT NULL COMMENT '格式 YYYY-MM-DD HH:MM:SS',
  `openId` varchar(50) DEFAULT NULL,
  `io` tinyint(4) DEFAULT NULL COMMENT '产出(1)/消耗(0)',
  `ioType` int(11) DEFAULT NULL COMMENT '途径',
  `type` int(11) DEFAULT NULL COMMENT '货币日志类型',
  `value` int(11) DEFAULT NULL COMMENT '涉及值',
  `afterValue` int(11) DEFAULT NULL COMMENT '变更之后',
  KEY `logDate_openid` (`logDate`,`openId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='货币变更日志表';

/*Table structure for table `t_team` */

CREATE TABLE `t_team` (
  `teamId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `createCharId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `leaderCharId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `leaderName` varchar(20) NOT NULL DEFAULT '',
  `mNumber` int(11) unsigned NOT NULL DEFAULT '0',
  `mNumberLimit` int(11) unsigned NOT NULL DEFAULT '0',
  `teamExp` bigint(20) unsigned NOT NULL DEFAULT '0',
  `teamLv` int(11) unsigned NOT NULL DEFAULT '0',
  `declaration` varchar(255) NOT NULL DEFAULT '',
  `notice` varchar(255) NOT NULL DEFAULT '',
  `homepage` varchar(255) NOT NULL DEFAULT '',
  `qq` varchar(255) NOT NULL DEFAULT '',
  `yy` varchar(255) NOT NULL DEFAULT '',
  `addExpPerDay` text,
  `failNum` int(11) NOT NULL DEFAULT '0',
  `winNum` int(11) NOT NULL DEFAULT '0',
  `battleEndNum` int(11) NOT NULL DEFAULT '0',
  `dateline` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`teamId`,`name`,`createCharId`),
  UNIQUE KEY `name` (`name`),
  KEY `teamExp` (`teamExp`),
  FULLTEXT KEY `name_2` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
