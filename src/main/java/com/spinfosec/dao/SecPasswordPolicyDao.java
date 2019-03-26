package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpSecPasswordComplexityItem;
import com.spinfosec.dao.entity.SpSecPasswordPolicy;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SecPasswordPolicyDao
 * @Description: 〈密码安全策略持久层〉
 * @date 2018/10/12
 * All rights Reserved, Designed By SPINFO
 */
public interface SecPasswordPolicyDao
{
	/**
	 * 获取密码安全策略
	 * @return
	 */
	SpSecPasswordPolicy getSpSecPasswordPolicy();

	/**
	 * 获取密码复杂度
	 * @return
	 */
	List<SpSecPasswordComplexityItem> getComplexityItem();

	/**
	 * 更新密码安全策略
	 * @param secPasswordPolicy
	 */
	void saveSecPasswordPolicy(SpSecPasswordPolicy secPasswordPolicy);

	/**
	 * 重置密码复杂度
	 */
	void resetSpSecPasswordComplexityItem();

	/**
	 * 更新密码复杂度
	 * @param name
	 */
	void updateSpSecPasswordComplexityItem(String name);
}
