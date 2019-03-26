package com.spinfosec.service.impl;

import com.spinfosec.dao.common.ModuleDao;
import com.spinfosec.dao.entity.SpCodeDecodes;
import com.spinfosec.dao.entity.SpRoleModulePermissions;
import com.spinfosec.dto.pojo.common.TreeData;
import com.spinfosec.dto.pojo.system.ModuleBean;
import com.spinfosec.service.srv.IModuleSrv;
import com.spinfosec.utils.GenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ModuleSrvImpl
 * @Description: 〈模块管理〉
 * @date 2019/3/20
 * @copyright All rights Reserved, Designed By SPINFO
 */
@Service("moduleServ")
public class ModuleSrvImpl implements IModuleSrv
{
	@Autowired
	private ModuleDao moduleDao;

	@Override
	public List<TreeData> getAllModule()
	{
		List<SpCodeDecodes> allModule = moduleDao.getAllModule();
		List<TreeData> treeData = GenUtil.getModuleTree(allModule, true);
		return treeData;
	}

	/**
	 * 保存模块
	 * @param moduleBean
	 */
	@Override
	public void saveModuleBean(ModuleBean moduleBean)
	{
		SpCodeDecodes codeDecodes = moduleBean.getCodeDecodes();
		// 查找同模块下排在最后的菜单顺序 往上累加
		int maxOrder = 0;
		String parentId = codeDecodes.getParentId();
		// 父菜单ID为空说明是添加的父菜单
		if (StringUtils.isEmpty(parentId))
		{
			maxOrder = moduleDao.getMaxOrderByParId(null);
		}
		else
		{
			maxOrder = moduleDao.getMaxOrderByParId(parentId);
		}
		codeDecodes.setOrders(maxOrder + 1);

		codeDecodes.setId(GenUtil.getUUID());
		moduleDao.saveCodeDecodes(codeDecodes);

		// 保存模块和角色关系
		for (String roleId : moduleBean.getRoleIds())
		{
			SpRoleModulePermissions permissions = new SpRoleModulePermissions();
			permissions.setId(GenUtil.getUUID());
			permissions.setRoleId(roleId);
			permissions.setModuleId(codeDecodes.getId());
			moduleDao.saveRoleAndModule(permissions);
		}

	}

	/**
	 * 更新模块
	 * @param moduleBean
	 */
	@Override
	public void updateModuleBean(ModuleBean moduleBean)
	{
		SpCodeDecodes codeDecodes = moduleBean.getCodeDecodes();
		moduleDao.updateCodeDecodes(codeDecodes);

		// 删除原来的模块和角色关系
		moduleDao.deleteRoleAndModule(codeDecodes.getId());

		// 重新保存
		for (String roleId : moduleBean.getRoleIds())
		{
			SpRoleModulePermissions permissions = new SpRoleModulePermissions();
			permissions.setId(GenUtil.getUUID());
			permissions.setModuleId(codeDecodes.getId());
			permissions.setRoleId(roleId);
			moduleDao.saveRoleAndModule(permissions);
		}

	}

	/**
	 * 删除模块
	 * @param id
	 */
	@Override
	public void deleteCodeDecodes(String id)
	{
		moduleDao.deleteCodeDecodes(id);

		// 删除模块和角色关系
		moduleDao.deleteRoleAndModule(id);
	}

	/**
	 * 更新模块
	 * @param codeDecodes
	 */
	@Override
	public void updateCodeDecodes(SpCodeDecodes codeDecodes)
	{
		moduleDao.updateCodeDecodes(codeDecodes);
	}
}
