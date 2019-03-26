package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpOrgUnitDict;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName OrgDao
 * @Description: 〈组织部门持久层〉
 * @date 2018/10/16
 * All rights Reserved, Designed By SPINFO
 */
public interface OrgDao
{
	/**
	 * 获取所有组织信息
	 * @return
	 */
	List<SpOrgUnitDict> getOrg(Map<String, Object> queryMap);

	/**
	 * 获取父级组织
	 * @return
	 */
	List<SpOrgUnitDict> getParentOrg(Map<String, Object> queryMap);

	/**
	 * 根据父组织id获取其下所有的组织
	 * @param parentId
	 * @return
	 */
	List<SpOrgUnitDict> getOrgByParId(String parentId);

	/**
	 * 通过部门ID查询关联的用户
	 * @param orgId
	 * @return
	 */
	List<Object[]> getUserByOrgId(String orgId);

	/**
	 * 保存组织信息
	 * @param spOrgUnitDict
	 */
	void saveOrg(SpOrgUnitDict spOrgUnitDict);

	/**
	 * 更新组织信息
	 * @param spOrgUnitDict
	 */
	void updateOrg(SpOrgUnitDict spOrgUnitDict);

	/**
	 * 根据ID获取其组织
	 * @param id
	 * @return
	 */
	SpOrgUnitDict getOrgById(String id);

	/**
	 * 删除组织信息
	 * @param id
	 */
	void deleteOrgById(String id);

	/**
	 * 根据组织删除用户和组织关系表
	 * @param orgId
	 */
	void deleteUserAndOrg(String orgId);
}
