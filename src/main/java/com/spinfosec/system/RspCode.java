package com.spinfosec.system;

import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * @title [错误码定义]
 * @description [一句话描述]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author Caspar Du
 * @version v 1.0
 * @create 2013-6-9 下午10:53:28
 */
public enum RspCode
{

	// ---------------------与SE通信的状态码-----------------------------//
	/**
	 * INVOKE_BY_SE_OK
	 */
	INVOKE_BY_SE_OK("0", "ok"),
	/**
	* policy not exist
	*/
	INVOKE_BY_SE_NO_POLICY("1", "policy not exist"),

	/**
	 * table scan stop
	 */
	INVOKE_BY_SE_SCAN_STOP("2", "table scan stop"),

	/**
	 * file parse error
	 */
	INVOKE_BY_SE_PARSE_FILE_ERROR("3", "file parse error"),

	/**
	 * se invoke error
	 */
	INVOKE_BY_SE_ERROR("4", "se invoke error"),

    /**
     * no data find
     */
    INVOKE_BY_SE_NO_DATA_FIND("5", "no data find"),

    /**
     * no column data find
     */
    INVOKE_BY_SE_NO_COLUMN_DATA_FIND("6", "no column data find"),

    /**
     * no column data find
     */
    INVOKE_BY_SE_CIPHERTEXT_RECOGNITION_ERROR("7", "ciphertext recognition error"),

	// ---------------------与SE通信的状态码-----------------------------//


	// --------------------------------系统类代码定义--------------------------------//
	/**
	 * 操作成功
	 */
	SUCCESS("000", "OK"),

	/**
	 * 操作失败
	 */
	FAILURE("111", "failure"),

	/**
	 * 内部错误
	 */
	INNER_ERROR("001", "内部错误。"),

	/**
	 * 参数错误
	 */
	PARAMERTER_ERROR("002", "参数错误。"),

	/**
	 * 重名校验失败
	 */
	DUPLICATION_ERROR("003", "重名校验失败！"),

	/**
	 * 不能删除默认
	 */
	DEFAULT_CAN_NOT_DELETE("004", "无法删除预置信息！"),

	/**
	 * 对象不存在
	 */
	OBJECE_NOT_EXIST("005", "the object not exists."),

	/**
	 * 加密解密
	 */
	ENC_DEC_FIELD("006", "AESPython Encrypt password or Decrypt password failed"),

	/**
	 * 对象已被使用
	 */
	OBJECT_IS_USED("007", "object is used"),

	/**
	 * 密码不存在
	 */
	PASSWORD_CAN_NOT_BE_NULL("008", "密码不能为空！"),

	/**
	 * 未授权或授权失败
	 */
	ERROR_LICENSE("009", "error license."),

	/**
	 * 没有符合条件的数据
	 */
	NO_MACTH_CONDITION_DATA("010,", "没有符合条件的数据！"),

    REPLAY_ATTACK("011", "监听到重放攻击，请刷新页面！"),

	/**
	 * 用户最小失效日期间隔
	 */
	USER_EXPIRATION_DATE_VALID("166", "最小失效日期间隔为%1$s天"),

	// --------------------------------用户类代码定义--------------------------------//

	/**
	 * session未初始化或超时
	 */
	INVALID_SESSION("101", "Invalid session."),

	/**
	 * 用户不存在
	 */
	ACCOUNT_NOT_EXIST("102", "account is not exist."),

	/**
	 * 登录失败
	 */
	LOGIN_FAILED("103", "login failed"),

	/**
	 * 用户密码错误
	 */
	FIND_PASSWORD_ERROR("104", "find user password error."),

	/**
	 * 旧密码错误
	 */
	OLD_PASSWORD_ERROR("105", "旧密码错误."),

	/**
	 * 显示验证码
	 */
	SHOW_AUTH_CODE("106", "true"),

	/**
	 * 隐藏验证码
	 */
	HIDE_AUTH_CODE("107", "false"),

	/**
	 * 密码复杂度验证失败
	 */
	PASSWORD_COMPLEXITY_VALID_FAIL("108", "密码复杂度验证失败！"),

	/**
	 * 预制用户不能删除
	 */
	USER_PRE_DEFINE_NO_DELETING("109", "系统预置用户不能删除"),

	/**
	 * 鉴权超时或未登录
	 */
	INVALID_TOKENID("110", "Invalid tokenId."),

	/**
	 * 账户过期
	 */
	ACCOUNT_OUT_OF_DATE("112", "账户已过期，请联系管理员！"),

	/**
	 * 非法地址登录
	 */
	INVALID_IP("113", "非法IP登录，请使用信任主机登录本系统！"),

	/**
	 * 密码已过期，请修改密码
	 */
	PASSWORD_OUT_OF_DATE("114", "密码已过期，请修改密码！"),

	/**
	 * 密码即将过期，请修改密码
	 */
	PASSWORD_WILL_OUT_OF_DATE("115", "密码即将过期，请修改密码！"),

	/**
	 * 首次登陆请修改密码
	 */
	MODIFY_PASSWORD_FIRST("116", "首次登陆请修改密码！"),

	/**
	 * 当前用户已经登录
	 */
	USER_LOGIN_AGING("117", "当前用户已经登录"),

	/**
	 * 账号处于休眠状态
	 */
	ACCOUNT_IS_SLEEP("118", "账号处于休眠状态，请联系管理员！"),

	/**
	 * 当前账号已被注销
	 */
	ACCOUNT_IS_CANCEL("176", "当前账号已被注销！"),

	/**
	 * 用户不存在
	 */
	USER_NOT_EXIST("133", "用户不存在！"),

	/**
	 * 重置密码失败
	 */
	RESET_PASSWORD_ERROR("135", "reset user password error."),

	// --------------------------------文件类代码定义--------------------------------//

	/**
	 * 导出文件失败
	 */
	EXPORT_FILE_ERROR("201", "导出文件失败！"),

	/**
	 * 文件不存在
	 */
	FILE_NOT_EXIST("202", "file not exist."),

	/**
	 * 归档开始时间不能大于结束时间
	 */
	LOG_ARCHIVE_DATE_INVALID("203", "归档开始时间不能大于结束时间"),

	/**
	 * 数据恢复失败
	 */
	RECOVER_BACKUP_ERROR("204", "recover backup error."),

	/**
	 * 上传失败
	 */
	FILE_UPLOAD_ERROR("205", "file upload error"),

	/**
	 * 删除失败
	 */
	DELETE_FAILED("206", "Delete failed"),

	/**
	 * 无效的文件类型
	 */
	INVALID_FILE_TYPE("207", "无效的文件类型！"),

	/**
	 * 归档结束时间只能选择三个月前，无法对近三个月以内的日志进行操作
	 */
	LOG_ARCHIVE_LESS_THREE_MONTH("208", "归档结束时间只能选择三个月前，无法对近三个月以内的日志进行操作！"),

	/**
	 * 归档或恢复操作执行中（安全日志归档/系统日志归档/数据备份恢复），请稍后！
	 */
	LOG_ARCHIVE_OR_RECOVER_RUNNING("209", "归档或恢复操作执行中（安全日志归档/系统日志归档/数据备份恢复），请稍后！"),

	/**
	 * 数据备份文件无效
	 */
	DB_BACKUP_PACAGE_VALID("210", "数据备份文件无效"),

	/**
	 * 压缩包格式错误
	 */
	RAR_TYPE_ERROR("211", "上传数据不是.zip格式文件!"),

	/**
	 * 文件下载失败
	 */
	DOWNLOAD_ERROR("212", "文件下载失败!"),

	/**
	 * 升级包格式不正确，请选择正确的升级包！
	 */
	UPDATE_PACKAGE_FORMAT_ERROR("213", "升级包格式不正确，请选择正确的升级包！"),

	/**
	 * 升级包验证失败，请选择正确的升级包！
	 */
	UPDATE_PACKAGE_VALID_FAIL("214", "升级包验证失败，请选择正确的升级包！"),

	// --------------------------------测试连接类代码定义--------------------------------//

	/**
	 * 连接成功
	 */
	CONNECT_SUCCESS("300", "连接成功!"),

	/**
	 * 连接失败
	 */
	CONNECT_ERROR("301", "连接失败！"),

	/**
	 * 连接失败,用户名或密码不正确
	 */
	CONNECT_AUTH_ERROR("302", "连接失败,用户名或密码不正确！"),
	/**
	 * 连接失败,服务器端口不正确
	 */
	CONNECT_PORT_ERROR("303", "连接失败,服务器地址或端口不正确！"),

	/**
	 * 测试连接错误码定义 连接失败,网络路径不可用
	 */
	CONNECT_PATH_ERROR("304", "连接失败,网络路径不可用！"),
	/**
	 * 连接失败,服务器地址不正确
	 */
	CONNECT_HOST_ERROR("305", "连接失败,服务器地址不正确！"),

	/**
	 * 连接失败,邮箱地址或密码不正确
	 */
	CONNECT_EMAIL_AUTH_ERROR("306", "连接失败,邮箱地址或密码不正确！"),

	/**
	 * Exchange连接失败
	 */
	DOMAIN_CONNECT_FAIL("307", "域控链接失败，请检查认证设置是否正确。"),

	// --------------------------------License许可类代码定义--------------------------------//
	/**
	 * License安装失败
	 */
	LICENSE_INSTALL_FAIL("401", "License安装失败！"),

	/**
	 * License验证失败
	 */
	LICENSE_VERIFY_FAIL("402", "License验证失败！"),

	/**
	 * License不在有效期内
	 */
	LICENSE_OUT_OF_DATE("403", "License不在有效期内！"),

	/**
	 * License证书不存在
	 */
	LICENSE_NOT_EXIST("404", "License证书不存在！"),

	/**
	 * 许可更新成功，请重新分配权限
	 */
	LICENSE_PRODUCT_CHANGE("405", "许可更新成功，请重新分配权限！"),

	/**
	 * License与当前机器不匹配
	 */
	LICENSE_NOT_MATCH_MACHINE("406", "License与当前机器不匹配"),

	/**
	 * License验证失败！需要重新登录！(许可拦截器中使用 区别是否需要登出)
	 */
	LICENSE_VERIFY_FAIL_NEND_TO_RELOGIN("407", "License验证失败！需要重新登录！"),

	/**
	 * License验证失败，需要重新上传许可！
	 */
	LICENSE_VERIFY_FAIL_NEND_TO_REUPLOAD("408", "License验证失败，需要重新上传许可！"),

	/**
	 * 获取机器码失败
	 */
	LICENSE_CANOTGET_MACHINE("409", "获取机器码失败！"),

	/**
	 * License证书文件格式不正确
	 */
	LICENSE_FILE_NOT_RIGHT("410", "License证书文件格式不正确！"),

	// --------------------------------扫描任务通信返回状态码定义-----------------------------//

	/**
	 * 消息通信错误(JMSException统一抛出)
	 */
	MQ_CONNECTION_ERROR("501", "通信错误,请检查网络连接"),

	/**
	 * 消息通信错误(JMSException统一抛出)
	 */
	MQ_MESSAGE_NULL("502", "消息为空!"),

	/**
	 * 消息通信错误(JMSException统一抛出)
	 */
	MQ_QUEUE_NULL("503", "目标队列为空!"),

	// --------------------------------策略任务操作类代码定义--------------------------------//

	/**
	 * 策略操作状态
	 */
	JOB_SUCCESS("510000", "操作成功！"),

	/**
	 * 部署策略失败
	 */
	DEPLOY_POLICY_FAILURE("601", "部署策略失败！"),

	/**
	 * 任务操作失败
	 */
	JOB_FAILURE("602", "操作失败！"),

	/**--------------------------白盒测试状态码-------------------------------------*/

	WHITE_TEST_STATE_NO_SUCH_ALGORITHM("701", "不支持的算法类型！"), WHITE_TEST_STATE_NO_SUCH_PADDING("702",
			"不支持的填充方式！"), WHITE_TEST_STATE_INVALID_KEY("703", "无效的key值！"), WHITE_TEST_STATE_UNSUPPORTED_ENCODING("704",
					"不支持的编码方式！"), WHITE_TEST_STATE_INVALID_ALGORITHM_PARAMETER("705",
							"无效的IV偏移量值！"), WHITE_TEST_STATE_BAD_PADDING("706",
									"错误的填充方式！"), WHITE_TEST_STATE_ILLEGAL_BLOCK_SIZE("707",
											"错误的块大小！"), WHITE_TEST_STATE_ERROR("708", "数据加解密失败！"),

	/**--------------------------白盒测试状态码-------------------------------------*/

	/**--------------------------MQ相关信息-------------------------------------*/

	/**
	 * 指定的NTP服务器不可用
	 */
	ERRORCODE_REMOTE_NTPSERVER_IS_NOT_AVAILABLE("500328", "指定的NTP服务器不可用，请检查或更换一台服务器 !"),

	/**
	* 设置NTP同步命令时失败
	*/
	ERRORCODE_SET_NTP_SERVER_IS_FAILED("500329", "设置NTP同步命令时失败 !"),

	/**
	* 设置NTP同步配置项时失败
	*/
	ERRORCODE_SET_REGEDIT_IS_FAILED("500330", "设置NTP同步配置项时失败 !"),

	/**
	 * 从NTP同步时间失败
	 */
	ERRORCODE_SYNC_NTP_SERVER_IS_FAILED("500331", "从NTP同步时间失败 !"),
	/**
	* MQ消息格式错误
	*/
	ERRORCODE_SET_NTP_MSG_IS_NULL("500332", "MQ消息格式错误 !"),

	/**
	 *获取CPU状态失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL7("500300", "获取CPU状态失败"),

	/**
	 *获取内存信息失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL8("500301", "获取内存信息失败"),

	/**
	 * 获取磁盘信息失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL9("500302", "获取磁盘信息失败"),

	/**
	 *获取网络信息失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL10("500303", "获取网络信息失败"),

	/**
	 *发送消息失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL11("500304", "发送消息失败"),

	/**
	 *获取网卡详细信息失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL12("500305", "获取网卡详细信息失败"),

	/**
	 * 设置系统IP地址失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL13("500306", "设置系统IP地址失败"),

	/**
	 *设置系统网关失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL14("500307", "设置系统网关失败"),

	/**
	 *设置系统DNS失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL15("500308", "设置系统DNS失败"),

	/**
	 *需要重启系统，以更新设置信息
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL16("500309", "需要重启系统，以更新设置信息"),

	/**
	 *带设置的网卡索引不存在
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL17("500310", "带设置的网卡索引不存在"),

	/**
	 *待设置DNS的为空
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL18("500311", "待设置DNS的为空"),

	/**
	 *获取系统时间失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL19("500312", "获取系统时间失败"),

	/**
	 * 设置系统时间失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL20("500313", "设置系统时间失败"),

	/**
	 *带设置的时间格式错误
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL21("500314", "带设置的时间格式错误"),

	/**
	 *设置系统日期失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL22("500315", "设置系统日期失败"),

	/**
	 *获取系统时区失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL23("500317", "获取系统时区失败"),
	/**
	 *待设置的IP地址冲突
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL24("500318", "待设置的IP地址冲突"),

	/**
	 *待设置的网络信息为空
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL25("500319", "待设置的网络信息为空"),

	/**
	 *待设置得IP地址为空
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL26("500320", "待设置得IP地址为空"),

	/**
	 *待设置的子网掩码为空
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL27("500321", "待设置的子网掩码为空"),

	/**
	 *备份网卡信息失败
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL28("500322", "备份网卡信息失败"),

	/**
	 * 待设置的网卡信息冲突
	 */
	DDS_ENCRIPT_FILE_COMMON_NETPATH_DEL29("500323", "待设置的网卡信息冲突"),

	/**
	 * 启动、停止、删除操作状态码定义 删除扫描策略状态码定义
	 * 启动成功
	 */
	START_JOB_SUCCESS("","任务启动成功!"),
	/**
	 * 任务正在停止，启动失败
	 */
	JOB_IS_STOPPING_START_FAILED("-407","任务正在停止，启动失败！"),
	/**
	 * 任务正在运行中，启动失败
	 */
	JOB_IS_RUNNING_START_FAILED("-402","任务正在运行中，启动失败！"),
	/**
	 * 停止成功
	 */
	STOP_JOB_SUCCESS("","任务停止成功!"),
	/**
	 *任务正在停止，停止失败
	 */
	JOB_IS_STOPPING_STOP_FAILED("-407","任务正在停止，停止失败!"),
	/**
	 * 任务处于暂停状态，停止失败
	 */
	JOB_IS_PARSED_STOP_FAILED("-405","任务处于暂停状态，停止失败!"),
	/**
	 * 任务已停止
	 */
	JOB_IS_STOPPED_STOP_FAILED("-406","任务处于停止状态，停止失败!"),
	/**
	 * 删除成功
	 */
	DELETE_JOB_SUCCESS("400", "删除扫描策略成功!"),

	/**
	 * 任务已删除，启动失败。
	 */
	JOB_DELETE_ERROR("510004", "任务已删除，启动失败。"),

	/**
	 * 任务文件不存在。
	 */
	JOB_PATH_FAILED("510010", "任务文件不存在。"),

	/**
	 * 任务运行异常，请重新尝试部署操作。
	 */
	JOB_RUNNING_EXCEPTION("510022", "任务运行异常，请重新尝试部署操作。"),

	/**
	 * 任务正在暂停，不能被删除。
	 */
	JOB_ERR_DELETE_JOB_IS_PAUSING("512200", "任务正在暂停，不能被删除。"),

	/**
	 * 任务正在停止，不能被删除
	 */
	JOB_ERR_DELETE_JOB_IS_STOPPING("512201", "任务正在停止，不能被删除。"),

	/**
	 * 任务正在运行，不能被删除
	 */
	JOB_ERR_DELETE_JOB_IS_RUNNING("512202", "任务正在运行，不能被删除。"),
	/**
	 * 移除任务文件夹失败
	 */
	DELETE_REMOVE_JOB_FOLDER("512203","任务文件被占用,请稍候删除!");


	/**--------------------------NTP服务器-------------------------------------*/

	/**
	 * 状态码
	 */
	private final String code;
	/**
	 * 状态描述
	 */
	private String description;

	private RspCode(String code, String description)
	{
		this.code = code;
		this.description = description;
	}

	public String getCode()
	{
		return code;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(RspCode rspCode, String description)
	{
		rspCode.description = description;
	}

	/**
	 * @Title: getRspCodeByCode
	 * @Description: 根据状态码返回错误码对象
	 * @param: @param code 传入状态码
	 * @param: @return 返回RspCode对象
	 * @return: RspCode 返回RspCode对象
	 * @throws
	 */
	public static RspCode getRspCodeByCode(String code)
	{
		EnumSet<RspCode> enumSet = EnumSet.allOf(RspCode.class);
		for (RspCode rspCode : enumSet)
		{
			if (StringUtils.isNotEmpty(code) && code.equals(rspCode.getCode()))
			{
				return rspCode;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return code + ": " + description;
	}

	/**
	 * @Title: toJson
	 * @Description: 将对象转为json
	 * @param: @return 返回json字符串
	 * @return: String 返回json字符窜
	 * @throws
	 */
	public String toJson()
	{
		return "{\"code\":\"" + code + "\",\"msg\":\"" + description + "\"}";
	}
}
