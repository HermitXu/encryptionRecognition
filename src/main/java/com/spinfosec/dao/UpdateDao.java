package com.spinfosec.dao;

import com.spinfosec.dao.entity.SpUpdateServerPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName UpdateDao
 * @Description: 〈升级管理〉
 * @date 2019/1/21
 * @copyright All rights Reserved, Designed By SPINFO
 */
public interface UpdateDao
{
	/**
	 * 根据条件查找对应的升级包
	 * @param querMap
	 * @return
	 */
	List<SpUpdateServerPackage> queryUpPackage(Map<String, Object> querMap);

	/**
	 * 根据ID获取对应的升级包
	 * @param id
	 * @return
	 */
	SpUpdateServerPackage getUpdatePackageById(@Param("id") String id);

	/**
	 * 保存系统升级包
	 * @param updateServerPackage
	 */
	void saveUpdatePackage(SpUpdateServerPackage updateServerPackage);

	/**
	 * 更新系统升级包
	 * @param updateServerPackage
	 */
	void updateUpdatePackage(SpUpdateServerPackage updateServerPackage);

	/**
	 * 根据ID删除对应的升级包
	 * @param id
	 */
	void deleteUpdatePackageById(@Param("id") String id);
}
