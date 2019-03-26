package com.spinfosec.service.srv;

import com.spinfosec.dao.entity.SpCodeDecodes;
import com.spinfosec.dto.pojo.common.TreeData;
import com.spinfosec.dto.pojo.system.ModuleBean;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IModuleSrv
 * @Description: 〈模块管理〉
 * @date 2019/3/20
 * @copyright All rights Reserved, Designed By SPINFO
 */
public interface IModuleSrv
{
	/**
	 * 获取所有模块
	 * @return
	 */
	List<TreeData> getAllModule();

	/**
	 * 保存模块和角色
	 * @param moduleBean
	 */
	void saveModuleBean(ModuleBean moduleBean);

	/**
	 * 更新模块和角色
	 * @param moduleBean
	 */
	void updateModuleBean(ModuleBean moduleBean);

	/**
	 * 删除模块
	 * @param id
	 */
	void deleteCodeDecodes(String id);

	/**
	 * 更新模块
	 * @param codeDecodes
	 */
	void updateCodeDecodes(SpCodeDecodes codeDecodes);
}
