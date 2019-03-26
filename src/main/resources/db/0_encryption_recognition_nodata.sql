/*
SQLyog Ultimate v9.62 
MySQL - 5.5.30 : Database - encryption_recognition
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`encryption_recognition` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `encryption_recognition`;

/*Table structure for table `sp_admin_host_setting` */

DROP TABLE IF EXISTS `sp_admin_host_setting`;

CREATE TABLE `sp_admin_host_setting` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `HOST_IP` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ip',
  `HOST_MAC` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'mac',
  `CREATED_BY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='信任主机';

/*Data for the table `sp_admin_host_setting` */

/*Table structure for table `sp_admin_trust_host` */

DROP TABLE IF EXISTS `sp_admin_trust_host`;

CREATE TABLE `sp_admin_trust_host` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `ADMIN_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户id',
  `HOST_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '信任主机id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户与信任主机关系表';

/*Data for the table `sp_admin_trust_host` */

/*Table structure for table `sp_admins` */

DROP TABLE IF EXISTS `sp_admins`;

CREATE TABLE `sp_admins` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `USERNAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码',
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '姓名',
  `EMAIL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮箱地址',
  `ID_CARD` varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '身份证号',
  `PASSWORD_CHANGE_FLAG` decimal(10,0) DEFAULT NULL COMMENT '密码修改标志',
  `PASSWORD_MODIFY_DATE` datetime DEFAULT NULL COMMENT '密码修改时间',
  `LAST_LOGIN_TIME` datetime DEFAULT NULL COMMENT '上次登录时间',
  `ACCOUNT_IS_DISABLED` decimal(1,0) DEFAULT NULL COMMENT '帐号是否失效',
  `EXPIRATION_DATE` datetime DEFAULT NULL COMMENT '过期时间',
  `USER_TYPE` decimal(10,0) DEFAULT NULL COMMENT '1  本地用户     默认',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `REGISTRATION_DATE` datetime DEFAULT NULL COMMENT '注册时间',
  `DEFINITION_TYPE` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'C_USER_DEFINE   自定义        C_PRE_DEFINE  预定义',
  `EXTERNAL_USER_DN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '外包用户dn',
  `sm2_pubKeyX` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sm2_pubKeyY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `usbkeyId` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATED_BY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PHONE` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户表';

/*Data for the table `sp_admins` */

/*Table structure for table `sp_code_decodes` */

DROP TABLE IF EXISTS `sp_code_decodes`;

CREATE TABLE `sp_code_decodes` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `PARENT_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '父节点',
  `URL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '路径',
  `ORDER` int(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '排序',
  `ICON` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图标',
  `SHOW` decimal(1,0) DEFAULT NULL COMMENT '是否在菜单中显示 0显示 1不显示',
  `IS_PRESET` int(50) NOT NULL COMMENT '是否预置菜单 0 预置 1 自定义',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='模块表';

/*Data for the table `sp_code_decodes` */

/*Table structure for table `sp_config_properties` */

DROP TABLE IF EXISTS `sp_config_properties`;

CREATE TABLE `sp_config_properties` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `NAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `GROUP_NAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `VALUE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EXTRA_DATA` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GROUP_ORDER` decimal(10,0) NOT NULL,
  `BUNDLE_KEY` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DEFAULT_VALUE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OPTLOCK` decimal(10,0) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='基本信息配置表';

/*Data for the table `sp_config_properties` */

/*Table structure for table `sp_data_backups` */

DROP TABLE IF EXISTS `sp_data_backups`;

CREATE TABLE `sp_data_backups` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `LOCALPATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DOWNLOAD_OPTLOCK` int(11) DEFAULT NULL,
  `CREATE_DATE` datetime DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILESIZE` decimal(32,2) DEFAULT NULL,
  `TYPE` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务数据     系统数据    安全数据',
  `RECOVER_OPTLOCK` int(11) DEFAULT NULL,
  `status` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态：0 归档中、2 归档成功、4 归档失败、1 恢复中、3 恢复成功、5 恢复失败',
  `CREATED_BY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `POLICY_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '安全数据按策略备份',
  `BEGIN_TIME` datetime DEFAULT NULL COMMENT '安全事件 备份开始时间',
  `END_TIME` datetime DEFAULT NULL COMMENT '安全数据 备份结束时间',
  `IS_DEL` decimal(1,0) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_data_backups` */

/*Table structure for table `sp_day_timeframe` */

DROP TABLE IF EXISTS `sp_day_timeframe`;

CREATE TABLE `sp_day_timeframe` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `DAY_OF_WEEK` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '周几',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='任务调度中天的信息 对于以“周”为周期的调度，该表存储了此调度需要在一周中的那几天执行。 该表存储了周一到周日的大写字符串 对于以天为周期的调度，该表默认存储“SUNDAY” 该表只有在按天或者按周调度才会用到';

/*Data for the table `sp_day_timeframe` */

/*Table structure for table `sp_day_timeframe_hours` */

DROP TABLE IF EXISTS `sp_day_timeframe_hours`;

CREATE TABLE `sp_day_timeframe_hours` (
  `DAY_TIMEFRAME_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `HOUR` decimal(10,0) NOT NULL COMMENT '小时',
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='调度任务中整点时间信息';

/*Data for the table `sp_day_timeframe_hours` */

/*Table structure for table `sp_discovery_tasks` */

DROP TABLE IF EXISTS `sp_discovery_tasks`;

CREATE TABLE `sp_discovery_tasks` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `NAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '策略名称',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `DISCOVERY_TASK_TYPE` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '策略类型   文件  数据库  exchange 等',
  `ELEMENT_STATUS` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '部署状态  SYNCHRONIZED, UNSYNCHRONIZED_NEW, UNSYNCHRONIZED_EDIT',
  `TARGET_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '目标id',
  `SCHEDULING_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '调度id',
  `IS_FILE_NAME_ENABLED` decimal(1,0) DEFAULT NULL COMMENT '是否启用文件名称过滤',
  `COMMON_OR_CUSTOM` decimal(1,0) DEFAULT NULL COMMENT '常用文件类型 0 或 自定义文件类型 1',
  `IS_FILE_AGE_ENABLED` decimal(1,0) DEFAULT NULL COMMENT '文件时间过滤是否启用',
  `SCAN_PERIOD_TYPE` varchar(1020) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '对应文件时间过滤选项内的三个radio选项，指定文件修改时间的范围 如： WITHIN，多少个月之内修改的文件 MORE_THAN，多少个月之前修改的文件 BETWEEN ，某个时间区间内修改的文件',
  `MODIFIED_WITHIN_MONTHS` decimal(10,0) DEFAULT NULL COMMENT '多少个月之内修改过的文件',
  `MODIFIED_MONTHS_AGO` decimal(10,0) DEFAULT NULL COMMENT '多少个月之前修改的文件',
  `MODIFIED_FROM_DATE` datetime DEFAULT NULL COMMENT '修改时间开始',
  `MODIFIED_TO_DATE` datetime DEFAULT NULL COMMENT '修改日期结束',
  `IS_LARGER_THAN_ENABLED` decimal(1,0) DEFAULT NULL COMMENT '是否启用文件大小的上限',
  `SIZE_LARGER_THAN` decimal(10,0) DEFAULT NULL COMMENT '上限文件大小',
  `IS_SMALLER_THAN_ENALBED` decimal(1,0) DEFAULT NULL COMMENT '是否启用文件大小的下限',
  `SIZE_SMALLER_THAN` decimal(10,0) DEFAULT NULL COMMENT '文件下限大小',
  `DEFINITION_TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'C_USER_DEFINE C_PRE_DEFINE',
  `OCR_ENABLED` decimal(1,0) DEFAULT NULL COMMENT '文档内ocr启用',
  `IS_CPU_LIMIT_ENABLED` decimal(1,0) DEFAULT NULL COMMENT 'cpu限制启用',
  `CPU_LIMIT_VALUE` decimal(3,0) DEFAULT NULL COMMENT 'cpu限制',
  `BUSY_BANDWIDTH_STARTDATE` datetime DEFAULT NULL COMMENT '忙时开始时间',
  `BUSY_BANDWIDTH_ENDDATE` datetime DEFAULT NULL COMMENT '忙时结束时间',
  `BUSY_BANDWIDTH_ENABLED` decimal(1,0) DEFAULT NULL COMMENT '忙时带宽限制启用',
  `BUSY_BANDWIDTH_LIMIT` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '忙时带宽值',
  `PERCENT_ENABLED` decimal(1,0) DEFAULT NULL COMMENT '扫描百分比',
  `PERCENT_AGE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '百分比扫描百分数',
  `TIMER_ENABLED` decimal(1,0) DEFAULT NULL COMMENT '计时器扫描（扫描时间限制）是否启用',
  `TIMER_AGE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '计时器扫描值',
  `BE_CHECKED_ORG_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '被检单位',
  `CHECK_ORG_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '检查单位',
  `CHECK_USER` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '检查人',
  `CHECK_DATE` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '检查时间',
  `OCR_PREPROCESS` decimal(1,0) DEFAULT NULL COMMENT 'ocr预处理',
  `CREATED_BY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`,`CREATE_DATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='策略表';

/*Data for the table `sp_discovery_tasks` */

/*Table structure for table `sp_dscvr_files` */

DROP TABLE IF EXISTS `sp_dscvr_files`;

CREATE TABLE `sp_dscvr_files` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `INC_EXTERNAL_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '事件id',
  `JOB_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '策略id',
  `JOB_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '策略名称',
  `INSERT_DATE` datetime DEFAULT NULL COMMENT '入库时间',
  `IS_AUTHORIZED` decimal(3,0) DEFAULT NULL COMMENT '是否审核',
  `DETECT_DATE_TS` datetime DEFAULT NULL COMMENT '检查时间',
  `DETECT_DATE_TZ` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '检查时区',
  `LOCAL_DETECT_TS` datetime DEFAULT NULL COMMENT '本地检查时间',
  `LOCAL_DETECT_TZ` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '本地检查时区',
  `FILE_PATH` text COLLATE utf8_unicode_ci COMMENT '路径',
  `FILE_NAME` text COLLATE utf8_unicode_ci COMMENT '文件名称',
  `FILE_EXTENSION` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '后缀',
  `HOST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '主机名',
  `IP` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ip',
  `MAC` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'mac',
  `FILE_SIZE` decimal(19,0) DEFAULT NULL COMMENT '文件大小',
  `FDATE_ACCESSED_TS` datetime DEFAULT NULL COMMENT '访问时间',
  `FDATE_ACCESSED_TZ` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '访问时区',
  `FDATE_CREATED_TS` datetime DEFAULT NULL COMMENT '创建时间',
  `FDATE_CREATED_TZ` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建时区',
  `FDATE_MODIFIED_TS` datetime DEFAULT NULL COMMENT '修改时间',
  `FDATE_MODIFIED_TZ` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改时区',
  `ORG_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织资源ID',
  `ORG_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织资源名称',
  `DATABASE_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据库名称',
  `DATABASE_TYPE` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据库类型',
  `TABLE_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '表名',
  `forwardedFrom` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '发件人',
  `department` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '发件部门',
  `inetSendTo` text COLLATE utf8_unicode_ci COMMENT '收件人',
  `inetCopyTo` text COLLATE utf8_unicode_ci COMMENT '抄送',
  `emailSubject` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮件主题',
  `sendTime` datetime DEFAULT NULL COMMENT '发送时间',
  `MD5_VALUE` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '源文件md5值',
  `IS_ENCRYPT` decimal(1,0) DEFAULT NULL COMMENT '是否加密',
  `ALGORITHM_TYPE` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '算法类型',
  `CREATED_BY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '所产生事件任务的创建者',
  `TASK_TYPE` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '事件类型',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `sp_DSCVR_FILES_PK` (`ID`),
  KEY `IDX_DSCVR_FILES_DETECT_DATE_TS` (`DETECT_DATE_TS`),
  KEY `IDX_DSCVR_FILES_FILE_SIZE` (`FILE_SIZE`),
  KEY `IDX_DSCVR_FILES_HOSTNAME` (`HOST_NAME`),
  KEY `IDX_DSCVR_FILES_INC_EXT_ID` (`INC_EXTERNAL_ID`),
  KEY `IDX_DSCVR_FILES_IP` (`IP`),
  KEY `IDX_DSCVR_FILES_JOB_NAME` (`JOB_NAME`),
  KEY `IDX_DSCVR_FILES_JOB_PATH` (`JOB_ID`),
  KEY `IDX_DSCVR_FILES_ORG` (`ORG_NAME`),
  KEY `IDX_DSCVR_FILES_ORG_ID` (`ORG_ID`),
  KEY `IDX_DSCVR_FILES_ORG_NAME` (`ORG_NAME`),
  KEY `IDX_DSCVR_FILES_ENCRYPT` (`IS_ENCRYPT`),
  KEY `IDX_DSCVR_FILES_ALGORITHM_TYPE` (`ALGORITHM_TYPE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_dscvr_files` */



-- ----------------------------
-- Table structure for sp_source_data_md5
-- ----------------------------
DROP TABLE IF EXISTS `sp_source_data_md5`;
CREATE TABLE `sp_source_data_md5` (
  `ID` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `MD5_VALUE` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILE_PATH` varchar(1024) COLLATE utf8_unicode_ci NOT NULL,
  `FILE_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BREACH_CONTENT` text COLLATE utf8_unicode_ci,
  `MATCH_CONTENT` text COLLATE utf8_unicode_ci,
  `RULE_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `RULE_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_DATE` datetime DEFAULT NULL,
  `ADMIN_ID` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORG_ID` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORG_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `RESOURCE_TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `RESOURCE_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SECRET_RATE` float(4,1) DEFAULT '1.0',
  `FILE_NAME` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_ID` mediumtext COLLATE utf8_unicode_ci,
  `FALSE_POSITIVE` decimal(1,0) DEFAULT NULL,
  `SUBSYSTEM` int(11) DEFAULT NULL,
  `DETECT_DATE_TS` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*Data for the table `sp_source_data_md5` */

-- ----------------------------
-- Table structure for sp_dscvr_excld_file_types
-- ----------------------------
DROP TABLE IF EXISTS `sp_dscvr_excld_file_types`;
CREATE TABLE `sp_dscvr_excld_file_types` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `DISCOVERY_TASK_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `FILE_TYPE` text COLLATE utf8_unicode_ci NOT NULL,
  `ELEMENT_INDEX` decimal(10,0) DEFAULT NULL,
  `FILE_PROPERTY_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*Data for the table `sp_dscvr_excld_file_types` */

-- ----------------------------
-- Table structure for sp_dscvr_incld_file_types
-- ----------------------------
DROP TABLE IF EXISTS `sp_dscvr_incld_file_types`;
CREATE TABLE `sp_dscvr_incld_file_types` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `DISCOVERY_TASK_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `FILE_TYPE` text COLLATE utf8_unicode_ci NOT NULL,
  `ELEMENT_INDEX` decimal(10,0) DEFAULT NULL,
  `FILE_PROPERTY_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*Data for the table `sp_dscvr_incld_file_types` */


/*Table structure for table `sp_encryption_algorithm` */

DROP TABLE IF EXISTS `sp_encryption_algorithm`;

CREATE TABLE `sp_encryption_algorithm` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '算法名称',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '国密  NATIONAL_   非国密  INTERNATIONAL',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_encryption_algorithm` */



-- ----------------------------
-- Table structure for sp_algorithm_file_type
-- ----------------------------
DROP TABLE IF EXISTS `sp_algorithm_file_type`;
CREATE TABLE `sp_algorithm_file_type` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ALGORITHM_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '算法ID',
  `FILE_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件类型',
  `JOB_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '策略ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_algorithm_file_type` */


/*Table structure for table `sp_event_archive_log` */

DROP TABLE IF EXISTS `sp_event_archive_log`;

CREATE TABLE `sp_event_archive_log` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `STATUS` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL COMMENT '开始时间',
  `END_DATE` datetime DEFAULT NULL COMMENT '结束时间',
  `INCIDENT_NUM` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '归档数量',
  `PATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '存储路径',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `ISDEL` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `CREATED_BY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
  `downOptLock` int(11) DEFAULT NULL,
  `recoverOptLock` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='安全日志归档';

/*Data for the table `sp_event_archive_log` */

/*Table structure for table `sp_org_admin_relation` */

DROP TABLE IF EXISTS `sp_org_admin_relation`;

CREATE TABLE `sp_org_admin_relation` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `ORG_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织单位id',
  `ADMIN_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_org_admin_relation` */

/*Table structure for table `sp_org_unit_dict` */

DROP TABLE IF EXISTS `sp_org_unit_dict`;

CREATE TABLE `sp_org_unit_dict` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `DEFINITION_TYPE` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'C_USER_DEFINE     C_PRE_DEFINE',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `PARENT_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '父部门id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_org_unit_dict` */

/*Table structure for table `sp_plc_file_types` */

DROP TABLE IF EXISTS `sp_plc_file_types`;

CREATE TABLE `sp_plc_file_types` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `OPTLOCK` decimal(10,0) NOT NULL COMMENT '操作标识',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `NAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '名称',
  `INT_VAL` decimal(10,0) NOT NULL COMMENT '整型值,同id',
  `EXTENSION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '扩展名',
  `FORMAT_GROUP` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '格式分组',
  `IS_UNICAST` decimal(1,0) DEFAULT NULL,
  `POLICY_ENTITY_STATUS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DEFINITION_TYPE` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '定义类型',
   `COMMON_USE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否是常用类型 1常用 0自定义',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_plc_file_types` */

/*Table structure for table `sp_plc_file_types_category` */

DROP TABLE IF EXISTS `sp_plc_file_types_category`;

CREATE TABLE `sp_plc_file_types_category` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME_EN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '英文信息',
  `NAME_CN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '中文信息',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_plc_file_types_category` */

/*Table structure for table `sp_role_admin_relation` */

DROP TABLE IF EXISTS `sp_role_admin_relation`;

CREATE TABLE `sp_role_admin_relation` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `ROLE_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色id',
  `ADMIN_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='角色用户关系表';

/*Data for the table `sp_role_admin_relation` */

/*Table structure for table `sp_role_module_permissions` */

DROP TABLE IF EXISTS `sp_role_module_permissions`;

CREATE TABLE `sp_role_module_permissions` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `MODULE_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '模块id',
  `ROLE_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='角色模块关系表';

/*Data for the table `sp_role_module_permissions` */

/*Table structure for table `sp_roles` */

DROP TABLE IF EXISTS `sp_roles`;

CREATE TABLE `sp_roles` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME` varchar(40) COLLATE utf8_unicode_ci NOT NULL,
  `ROLE_TYPE` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色分类   系统管理员 SYSTEM_MANAGER     业务管理员 SECURITY_MANAGER    审核管理员 AUDIT_MANAGER',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATED_BY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
  `DEFINITION_TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '预定义 C_PRE_DEFINE     自定义  C_USER_DEFINE',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_roles` */

/*Table structure for table `sp_scheduling_data` */

DROP TABLE IF EXISTS `sp_scheduling_data`;

CREATE TABLE `sp_scheduling_data` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `START_DATE` datetime DEFAULT NULL COMMENT '开始时间',
  `END_DATE` datetime DEFAULT NULL COMMENT '结束时间',
  `END_DATE_TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '结束时间类型',
  `IS_DURATION_LIMIT` decimal(1,0) DEFAULT NULL COMMENT '是否限制持续时间',
  `DURATION_VALUE` decimal(10,0) DEFAULT NULL COMMENT '间隔时间',
  `FREQUENCY_TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '重复周期类型',
  `SCHEDULEDAYS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '计划时间',
  `RECUR_DATES` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '重复类型：周、日、月',
  `CONTINUOUS_STOP_INTERVAL` decimal(10,0) DEFAULT NULL COMMENT '停止继续的时间间隔 默认为10，单位分钟，该选项对 “持续”选项有效，其他都为10',
  `USE_INITIAL_RECUR` decimal(1,0) DEFAULT NULL COMMENT '是否选中“不早于”CHECKBOX 时间为 START_DATE字段',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='策略调度';

/*Data for the table `sp_scheduling_data` */

/*Table structure for table `sp_scheduling_day_tf` */

DROP TABLE IF EXISTS `sp_scheduling_day_tf`;

CREATE TABLE `sp_scheduling_day_tf` (
  `SCHEDULING_DATA_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '外键，参考sp_scheduling_data表',
  `DAY_TF_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'sp_day_timeframe',
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='调度方案主键与星期之间的对应关系表 指出了该调度一周内那几天执行该表只有在按天或者按周调度才会用到';

/*Data for the table `sp_scheduling_day_tf` */

/*Table structure for table `sp_sec_password_complexity_item` */

DROP TABLE IF EXISTS `sp_sec_password_complexity_item`;

CREATE TABLE `sp_sec_password_complexity_item` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IS_ENABLE` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_sec_password_complexity_item` */

/*Table structure for table `sp_sec_password_policy` */

DROP TABLE IF EXISTS `sp_sec_password_policy`;

CREATE TABLE `sp_sec_password_policy` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `PASSWORD_VALIDITY` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD_LENGTH_MIN` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD_LENGTH_MAX` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MAX_LOGIN_TIMES` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IS_MODIFY_PASSWORD_FIRST` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UKEY_ENABLE` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IS_REPEAT_LOGIN` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SECHOST_ENABLE` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_sec_password_policy` */

/*Table structure for table `sp_server_status` */

DROP TABLE IF EXISTS `sp_server_status`;

CREATE TABLE `sp_server_status` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cpuUsage` decimal(8,2) NOT NULL,
  `memUsage` decimal(8,2) NOT NULL,
  `freeDisk` decimal(16,2) NOT NULL,
  `GetTime` datetime NOT NULL,
  `hostname` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `netName` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isEnable` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '网卡是否可用',
  `hostIP` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `totalDisk` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '磁盘总大小',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_server_status` */

/*Table structure for table `sp_system_operate_archive_log` */

DROP TABLE IF EXISTS `sp_system_operate_archive_log`;

CREATE TABLE `sp_system_operate_archive_log` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `status` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态：0 归档中、2 归档成功、4 归档失败、1 恢复中、3 恢复成功、5 恢复失败',
  `start_date` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '截止事件',
  `incident_num` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '归档事件个数',
  `path` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '存储路径',
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL,
  `ISDEL` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除原记录：是、否',
  `CREATED_BY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `downOptLock` int(11) DEFAULT NULL,
  `recoverOptLock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_system_operate_archive_log` */

/*Table structure for table `sp_system_operate_log_info` */

DROP TABLE IF EXISTS `sp_system_operate_log_info`;

CREATE TABLE `sp_system_operate_log_info` (
  `DISCRIMINATOR` varchar(31) COLLATE utf8_unicode_ci NOT NULL,
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `ADMIN_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ADMIN_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ROLE_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ROLE_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TRANSACTION_ID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `IS_LEADER_FOR_TX` decimal(1,0) NOT NULL,
  `MESSAGE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GENERATION_TIME_TS` datetime DEFAULT NULL,
  `ENTITY_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OPERATION` varchar(2048) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ENTITY_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BUSINESS_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `RESULT` decimal(1,0) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_system_operate_log_info` */

/*Table structure for table `sp_target_res` */

DROP TABLE IF EXISTS `sp_target_res`;

CREATE TABLE `sp_target_res` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `IP` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ip地址',
  `FQDN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '计算机全名',
  `RES_TYPE` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '类型  和策略类型保持一致',
  `SITE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '站点（sharepoint）',
  `SERVER_ADDRESS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '服务器地址 exchange',
  `DATABASE_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据库类型',
  `DATABASE_VERSION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据库版本',
  `PORT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '端口',
  `DATABASE_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据库名称',
  `SCHEMA_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模式名',
  `USERNAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码',
  `SHARE_USERNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sqlite和Access数据库使用(共享文件的用户名和密码)',
  `SHARE_PASSWORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Sqlite和Access数据库使用(共享文件的用户名和密码)',
  `VMDK_PATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DOMAIN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '域名',
  `SID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'sid oracle',
  `IS_SCAN_ATTACHMENT` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否扫描附件 Lotus  1 是     0 否',
  `IS_SSL` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否启用安全连接  Exchange扫描使用   1 是  0 否',
  `CREATE_DATE` datetime NOT NULL COMMENT '创建时间',
  `CREATED_BY` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '创建者',
  `LAST_MODIFY_DATE` datetime DEFAULT NULL COMMENT '最后修改时间',
  `LOTUS_SERVER_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Lotus服务类型',
  `EXCHANGE_EDITION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'exchange版本',
  `PASSWORD_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码类型',
  `KEY_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密钥类型',
  `PUBLIC_KEY` text COLLATE utf8_unicode_ci COMMENT '密钥',
  `PUBLIC_KEY_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密钥名称',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='主机资源';


-- ----------------------------
-- Table structure for sp_column_resinfo
-- ----------------------------
DROP TABLE IF EXISTS `sp_column_resinfo`;
CREATE TABLE `sp_column_resinfo` (
  `ID` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `TABLENAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '表名',
  `COLUMNNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段名',
  `IP` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'IP',
  `TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `KEYTYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码类型',
  `USERNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户名',
  `PASSWORD` text COLLATE utf8_unicode_ci COMMENT '密码',
  `PATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '路径',
  `DOMAIN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PUBLICKEY` text COLLATE utf8_unicode_ci COMMENT '密钥',
  `PORT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '端口',
  `PUBLICKEYNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密钥名称',
  `TARGET_RES_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '主机资源ID',
  `COLLECTION_POLICY_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PATH_REALATIVE_ABSOLUTE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '路径(绝对或相对)',
  `FLAG_COLUMN2` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_target_res` */

/*Table structure for table `sp_target_res_detail` */

DROP TABLE IF EXISTS `sp_target_res_detail`;

CREATE TABLE `sp_target_res_detail` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `TARGET_RES_ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DISPLAY_PATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILE_FOLDER_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILE_SIZE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODIFY_DATE_TS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODIFY_DATE_TZ` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DATABASE_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IS_SCAN_ALL_ON_DATABASE` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_target_res_detail` */

/*Table structure for table `sp_task` */

DROP TABLE IF EXISTS `sp_task`;

CREATE TABLE `sp_task` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `jobId` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '任务名称',
  `type` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '和策略类型一致',
  `startTime` datetime DEFAULT NULL COMMENT '开始时间',
  `endTime` datetime DEFAULT NULL COMMENT '结束时间',
  `failReason` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `result` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '进度',
  `isLast` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `size` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `orgId` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `orgName` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `totalCount` int(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `totalRecordCount`int(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `successCount`int(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `successRecordCount` int(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `failCount`int(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `failRecordCount` int(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sp_task_endTime_index` (`endTime`),
  KEY `sp_task_isLast_index` (`isLast`),
  KEY `sp_task_jobId_index` (`jobId`),
  KEY `sp_task_name_index` (`name`),
  KEY `sp_task_startTime_index` (`startTime`),
  KEY `sp_task_type_index` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='任务中心';

/*Data for the table `sp_task` */

/*Table structure for table `sp_task_algorithm_relation` */

DROP TABLE IF EXISTS `sp_task_algorithm_relation`;

CREATE TABLE `sp_task_algorithm_relation` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `TASK_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '策略id',
  `ALGORITHM_ID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '算法id',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_task_algorithm_relation` */

/*Table structure for table `sp_task_files` */

DROP TABLE IF EXISTS `sp_task_files`;

CREATE TABLE `sp_task_files` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `lastModified` datetime DEFAULT NULL,
  `name` varchar(2048) COLLATE utf8_unicode_ci DEFAULT NULL,
  `taskId` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `scanTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sp_audit_files_name_index` (`name`(255)),
  KEY `sp_audit_files_scanTime_index` (`scanTime`),
  KEY `sp_audit_files_taskId_index` (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_task_files` */

/*Table structure for table `sp_task_sipped` */

DROP TABLE IF EXISTS `sp_task_sipped`;

CREATE TABLE `sp_task_sipped` (
  `id` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(2048) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `taskId` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  `scanTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sp_audit_sipped_name_index` (`name`(255)),
  KEY `sp_audit_sipped_scanTime_index` (`scanTime`),
  KEY `sp_audit_sipped_taskId_index` (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_task_sipped` */

/*Table structure for table `sp_theme_info_setting` */

DROP TABLE IF EXISTS `sp_theme_info_setting`;

CREATE TABLE `sp_theme_info_setting` (
  `id` int(11) NOT NULL,
  `themeColor` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `btnClickColor` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `companyInfo` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `companyInfo_en` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `companyLink` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `companyTelephone` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isShowHelp` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_theme_info_setting` */

/*Table structure for table `sp_update_server_package` */

DROP TABLE IF EXISTS `sp_update_server_package`;

CREATE TABLE `sp_update_server_package` (
  `ID` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ID',
  `SYSTEM_TYPE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '系统类型',
  `VERSION` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '升级包依赖版本',
  `TIME_STAMP` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '升级包时间戳',
  `SECONDARY_VERSION` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '升级包次版本号',
  `PATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PATH_RELATIVE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SRC_PATH` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPLOAD_TIME` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILE_SIZE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILE_TYPE` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DEPLOY_STATUS` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '本次更新是否成功。成功2，失败3',
  `ERROR_CODE` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'shell脚本返回值。0,1,2,3...',
  `DEPLOY_MSG` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '部署消息',
  `DEPLOY_TIME` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '部署时间',
  `VERSION_TYPE` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SERVER_TYPE` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BAK1` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BAK2` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BAK3` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sp_update_server_package` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
