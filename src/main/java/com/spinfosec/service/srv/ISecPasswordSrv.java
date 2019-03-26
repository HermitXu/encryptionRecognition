package com.spinfosec.service.srv;

import com.spinfosec.dao.entity.SpSecPasswordComplexityItem;
import com.spinfosec.dao.entity.SpSecPasswordPolicy;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.system.SecPasswordPolicyFormData;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ISecPasswordSrv
 * @Description: 〈密码安全策略接口〉
 * @date 2018/10/12
 * All rights Reserved, Designed By SPINFO
 */
public interface ISecPasswordSrv
{
	/**
	 * 获取密码安全策略
	 * @return
	 */
	SpSecPasswordPolicy getSpSecPasswordPolicy();

	/**
	 * 获取密码复杂度选项
	 * @return List<SpSecPasswordComplexityItem>
	 */
	List<SpSecPasswordComplexityItem> getComplexityItem();

	/**
	 * 保存密码安全策略
	 * @param req
	 * @param secPasswordPolicyFormData
	 */
	void saveSecPasswordPolicy(HttpServletRequest req, SecPasswordPolicyFormData secPasswordPolicyFormData);

	/**
	 * 验证密码有效性
	 * @param password
	 * @param userName 禁止用户口令与用户名相同或包含用户名
	 * @return String
	 */
	String passwordValid(String password, String userName);

	/**
	 * 验证用户失效日期
	 * @param s 日期
	 * @param expirationDateOffset 日期间隔
	 * @return
	 */
	CodeRsp dateValid(String s, String expirationDateOffset);

}
