package com.spinfosec.service.impl;

import com.spinfosec.dao.OrgDao;
import com.spinfosec.dao.entity.SpOrgUnitDict;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.system.OrgTreeBean;
import com.spinfosec.service.srv.IOrgService;
import com.spinfosec.system.RspCode;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.GenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName OrgServiceImpl
 * @Description: 〈组织业务实现类〉
 * @date 2018/10/16
 * All rights Reserved, Designed By SPINFO
 */
@Transactional
@Service("orgSrv")
public class OrgServiceImpl implements IOrgService
{

	@Autowired
	private OrgDao orgDao;

	/**
	 * 获取所有组织信息
	 * @return
	 */

	@Override
	public List<OrgTreeBean> getOrg(Map<String, Object> queryMap)
	{
		List<SpOrgUnitDict> orgUnitDictList = new ArrayList<>();
		// 如果没有查询条件 直接查询所有的父组织 有查询条件则要查询全部
		if (StringUtils.isEmpty((String) queryMap.get("name")))
		{
			orgUnitDictList = orgDao.getParentOrg(queryMap);
		}
		else
		{
			orgUnitDictList = orgDao.getOrg(queryMap);
		}
		return packageOrgTree(orgUnitDictList);
	}

	/**
	 * 递归封装每层组
	 * @param orgUnitDictList
	 * @return
	 */
	public List<OrgTreeBean> packageOrgTree(List<SpOrgUnitDict> orgUnitDictList)
	{
		List<OrgTreeBean> orgTreeBeanList = new ArrayList<>();
		for (SpOrgUnitDict spOrgUnitDict : orgUnitDictList)
		{
			OrgTreeBean orgTreeBean = new OrgTreeBean();
			orgTreeBean.setId(spOrgUnitDict.getId());
			orgTreeBean.setName(spOrgUnitDict.getName());
			orgTreeBean.setParentId(spOrgUnitDict.getParentId());
			orgTreeBean.setDescription(spOrgUnitDict.getDescription());

			List<SpOrgUnitDict> orgByParIdList = orgDao.getOrgByParId(spOrgUnitDict.getId());

			if (null != orgByParIdList && !orgByParIdList.isEmpty())
			{
				List<OrgTreeBean> orgTreeBeanByParIdList = packageOrgTree(orgByParIdList);
				for (OrgTreeBean orgTreeBean1 : orgTreeBeanByParIdList)
				{
					orgTreeBean1.setParentName(orgTreeBean.getName());
				}
				orgTreeBean.setChildren(orgTreeBeanByParIdList);

			}
			orgTreeBeanList.add(orgTreeBean);
		}
		return orgTreeBeanList;

	}

	/**
	 * 查询部门ID关联的用户
	 * @param orgId
	 * @return
	 */
	@Override
	public List<Object[]> getUserByOrgId(String orgId)
	{
		return orgDao.getUserByOrgId(orgId);
	}

	/**
	 * 根据ID获取组织信息
	 * @param orgId
	 */
	@Override
	public SpOrgUnitDict getOrgById(String orgId)
	{
		return orgDao.getOrgById(orgId);
	}

	/**
	 * 获取组织信息
	 * @param orgId 组织ID
	 * @return
	 */
	@Override
	public OrgTreeBean getOrgInfoById(String orgId)
	{
		SpOrgUnitDict orgById = orgDao.getOrgById(orgId);
		OrgTreeBean bean = new OrgTreeBean();
		bean.setId(orgById.getId());
		bean.setName(orgById.getName());
		bean.setParentId(orgById.getParentId());
		bean.setDescription(orgById.getDescription());
		if (StringUtils.isNotEmpty(orgById.getParentId()))
		{
			bean.setParentName(orgDao.getOrgById(orgById.getParentId()).getName());
		}

		return bean;
	}

	/**
	 * 保存组织
	 * @param orgTreeBean
	 */
	@Override
	public void saveOrgUnit(OrgTreeBean orgTreeBean)
	{
		SpOrgUnitDict orgUnitDict = new SpOrgUnitDict();
		String orgUnitDictId = GenUtil.getUUID();
		orgUnitDict.setId(orgUnitDictId);
		orgUnitDict.setName(orgTreeBean.getName());
		// 新增均为用户自定义
		orgUnitDict.setDefinitionType(Contants.C_USER_DEFINE);
		orgUnitDict.setDescription(orgTreeBean.getDescription());
		orgUnitDict.setParentId(orgTreeBean.getParentId());
		orgDao.saveOrg(orgUnitDict);

	}

	/**
	 * 更新组织
	 * @param orgTreeBean
	 */
	@Override
	public void updateOrgUnit(OrgTreeBean orgTreeBean)
	{
		SpOrgUnitDict orgUnitDict = new SpOrgUnitDict();
		orgUnitDict.setId(orgTreeBean.getId());
		orgUnitDict.setName(orgTreeBean.getName());
		// 新增均为用户自定义
		orgUnitDict.setDefinitionType(Contants.C_USER_DEFINE);
		orgUnitDict.setDescription(orgTreeBean.getDescription());
		orgUnitDict.setParentId(orgTreeBean.getParentId());
		orgDao.updateOrg(orgUnitDict);
	}

	/**
	 * 删除组织
	 * @param id
	 * @return
	 */
	@Override
	public CodeRsp delOrg(String id)
	{
		CodeRsp codeRsp = codeRsp = new CodeRsp(RspCode.SUCCESS);
		// 查询该组织信息
		SpOrgUnitDict orgById = orgDao.getOrgById(id);
		// 预制组织不可删除
		if (orgById.getDefinitionType().equalsIgnoreCase(Contants.C_PRE_DEFINE))
		{
			codeRsp = new CodeRsp(RspCode.DEFAULT_CAN_NOT_DELETE);
			return codeRsp;
		}
		// 如果该组织由父组织 则将其组织下所有子部门的父ID改成其父ID
		if (StringUtils.isNotEmpty(orgById.getParentId()))
		{
			// 查询该组织下的子部门
			List<SpOrgUnitDict> orgChild = orgDao.getOrgByParId(orgById.getId());
			// 将该组织下的子部门改成其父组织的id
			if (!orgChild.isEmpty())
			{
				for (SpOrgUnitDict spOrgUnitDict : orgChild)
				{
					spOrgUnitDict.setParentId(orgById.getParentId());
					orgDao.updateOrg(spOrgUnitDict);
				}
			}
		}

		// 删除组织和用户关系表
		orgDao.deleteUserAndOrg(orgById.getId());
		// 删除该组织单位
		orgDao.deleteOrgById(id);

		return codeRsp;
	}
}
