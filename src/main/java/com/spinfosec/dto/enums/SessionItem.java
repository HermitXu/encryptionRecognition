package com.spinfosec.dto.enums;

/**
 * 缓存常用量
 */
public enum SessionItem
{
	/**
	 * 登陆用户ID
	 */
	userId,

	/**
	 * 当前用户名
	 */
	userName,

	/**
	 * 校验码
	 */
	authCode,

	/**
	 * tokenID
	 */
	tokenId,

	/**
	 * 角色ID
	 */
	roleId,

	/**
	 * 登陆用户的角色名称
	 */
	roleName,

	/**
	 * 用户权限
	 */
	userFunctions,

	/**
	 * 是否显示验证码
	 */
	checkAuthCode,

	/**
	 * 当前登录用户邮箱地址
	 */
	email,

	/**
	 * 当前登录用户姓名
	 */
	name,

	/**
	 * 是否自动加载向导
	 */
	showPrompt,
	/**
	 * 产品名称
	 */
	productName,

	/**
	 * 密码有效期
	 */
	passwordValidity,

	/**
	 * 密码最小长度
	 */
	passwordLengthMin,
	/**
	 * 密码最大长度
	 */
	passwordLengthMax,
	/**
	 * 最大尝试登陆次数
	 */
	maxLoginTimes,
	/**
	 * 首次登陆是否修改密码
	 */
	isModifyPasswordFirst,
	/**
	 * 是否启用uKey登陆
	 */
	ukeyEnable,
	/**
	 * 是否可以重复登录
	 */
	isRepeatLogin,
	/**
	 * 是否启用信任主机
	 */
	sechostEnable;

}
