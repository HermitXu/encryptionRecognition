package com.spinfosec.utils;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName Contants
 * @Description: 〈常用量〉
 * @date 2018/10/18
 * All rights Reserved, Designed By SPINFO
 */
public class Contants
{
    public static final String SUBSYSTEM_SIMP_CRS = "10007";
    public static final String PRODUCT_NAME_SIMP_CRS = "SIMP-CRS"; // 密文识别系统
    public static final String PRODUCT_MODEL_SIMP_CRS_1000 = "SIMP-CRS-1000";

	public static final long DAY = 1 * 24 * 60 * 60 * 1000;

	public static final String YES = "1";
	public static final String NO = "0";

	public static final String COMPANY_FLAG = "SPINFO";

	// ----------------升级管理相关----------------------//
	// 0未部署，1部署中 2部署成功 3部署失败
	public static final String UPDATE_DEPLOY_STATUS_UNDEPLOY = "0";
	public static final String UPDATE_DEPLOY_STATUS_DEPLOYING = "1";
	public static final String UPDATE_DEPLOY_STATUS_DEPLOY_SUCCESS = "2";
	public static final String UPDATE_DEPLOY_STATUS_DEPLOY_FAIL = "3";

	// 基本设置
	public static final String BASE_SETTINGS = "BASE_SETTINGS";

	// -------------------------用户类型 相关---------------------------//

	public static final String AUDIT_MANAGER = "AUDIT_MANAGER"; // 安全审核员角色
	public static final String SECURITY_MANAGER = "SECURITY_MANAGER"; // 安全管理员角色
	public static final String SYSTEM_MANAGER = "SYSTEM_MANAGER"; // 系统管理员角色
	public static final String ROOT_MANAGER = "ROOT_MANAGER"; // 超级管理员角色

	public static final String C_PRE_DEFINE = "C_PRE_DEFINE"; // 预制用户
	public static final String C_USER_DEFINE = "C_USER_DEFINE"; // 自定义用户

	// -------------------------归档状态 相关---------------------------//
	public static final String LOG_ARCHIVE_RUNNING = "0"; // 归档中
	public static final String LOG_ARCHIVE_SUCCESS = "2"; // 归档成功
	public static final String LOG_ARCHIVE_FAIL = "4"; // 归档失败
	public static final String LOG_RECOVER_RUNNING = "1"; // 恢复中
	public static final String LOG_RECOVER_SUCCESS = "3"; // 恢复成功
	public static final String LOG_RECOVER_FAIL = "5"; // 恢复失败

	public static final String sp_system_operate_log_info = "sp_system_operate_archive_log"; // 系统日志归档表
	public static final String sp_event_archive_log = "sp_event_archive_log"; // 安全日志归档表
	public static final String sp_data_backups = "sp_data_backups"; // 系统备份表

	// -------------------------邮箱 相关------------------------------//
	public static final String EMAIL_SETTINGS = "EMAIL_SETTINGS";
	public final class EmailSettings
	{
		public static final String SMTP = "SMTP";
		public static final String PORT = "PORT";
		public static final String EMAIL = "EMAIL";
		public static final String PASSWORD = "PASSWORD";
		public static final String USE_SSL = "USE_SSL";
	}

	// -------------------------LICENSE 相关------------------------------//
	// 机器码
	public static final String MCODE = "mcode";
	public static final String RES_LIMIT_COUNT = "resLimitCount";
	public static final String SUBSYSTEM = "subsystem";
	public static final String PRODUCT_NAME = "productName";
	public static final String MODEL = "model";
	public static final String INCLUDE_MODULE = "includeModule";
	public static final String NOT_BEFORE = "notBefore";
	public static final String NOT_AFTER = "notAfter";
	public static final String LICENSE_SUBSYSTEM_SPLIT = "#";

	// -------------------------消息队列相关------------------------------//
	// 获取主机资源信息
	public static final String TARGET_RES_OPERATE = "TargetResources";
	// 部署策略消息队列
	public static final String JOB_OPERATE = "JobOperate";
	// 获取系统运行状态/设置系统运行状态队列
	public static final String GET_SYSTEMTIME = "getSystemStatus";
	//删除任务
	public static final String DELETE_JOB = "deleteJob";

	// -------------------------策略相关相关------------------------------//
	// 发现类型
	public static final String DISCOVERY = "discovery";
	// 发现数据库扫描策略
	public static final String DB_SCAN_TASK_DISCOVER = "DB_SCAN_TASK_DISCOVER";
	// 发现扫描策略
	public static final String SCAN_TASK_DISCOVER = "SCAN_TASK_DISCOVER";
	// 发现FTP扫描策略
	public static final String FTP_SCAN_TASK_DISCOVER = "FTP_SCAN_TASK_DISCOVER";
	// 发现Linux扫描策略
	public static final String SFTP_SCAN_TASK_DISCOVER = "SFTP_SCAN_TASK_DISCOVER";
	// 发现sharePoint扫描策略
	public static final String SHAREPOINT_SCAN_TASK_DISCOVER = "SHAREPOINT_SCAN_TASK_DISCOVER";
	// 发现lotus扫描策略
	public static final String LOTUS_SCAN_TASK_DISCOVER = "LOTUS_SCAN_TASK_DISCOVER";
	// 发现exchange扫描策略
	public static final String EXCHANGE_SCAN_TASK_DISCOVER = "EXCHANGE_SCAN_TASK_DISCOVER";
	public static final String ON_POLICY_AND_FP_VERSION_UPDATE = "ON_POLICY_AND_FP_VERSION_UPDATE";


	public static final String DISCOVERY_TASK_TYPE_FILE = "FILE_SYSTEM";
	public static final String DISCOVERY_TASK_TYPE_DATABASE = "DATA_BASE";
	public static final String DISCOVERY_TASK_TYPE_FTP = "FTP";
	public static final String DISCOVERY_TASK_TYPE_LINUX = "SFTP";
	public static final String DISCOVERY_TASK_TYPE_SHARE_POINT = "SHARE_POINT";
	public static final String DISCOVERY_TASK_TYPE_EXCHANGE = "EXCHANGE";
	public static final String DISCOVERY_TASK_TYPE_LOTUS = "LOTUS";

}
