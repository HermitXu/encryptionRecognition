package com.spinfosec.dao.common;

import com.spinfosec.dao.entity.SpCodeDecodes;
import com.spinfosec.dao.entity.SpRoleModulePermissions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ModuleDao
 * @Description: 〈模块管理〉
 * @date 2019/3/20
 * @copyright All rights Reserved, Designed By SPINFO
 */
public interface ModuleDao
{
	/**
	 * 获取所有模块
	 * @return
	 */
	List<SpCodeDecodes> getAllModule();

	/**
	 * 保存模块
	 * @param codeDecodes
	 */
	void saveCodeDecodes(SpCodeDecodes codeDecodes);

	/**
	 * 保存模块和角色关系
	 * @param permissions
	 */
	void saveRoleAndModule(SpRoleModulePermissions permissions);

	/**
	 * 更新模块
	 * @return
	 */
	void updateCodeDecodes(SpCodeDecodes codeDecodes);

	/**
	 * 删除模块
	 * @param id
	 */
	void deleteCodeDecodes(String id);

	/**
	 * 删除模块和角色关系
	 * @param moduleId
	 */
	void deleteRoleAndModule(String moduleId);

	/**
	 * 查找同模块下排在最后的菜单顺序
	 * @param parentId
	 * @return
	 */
	int getMaxOrderByParId(@Param("parentId") String parentId);
}
