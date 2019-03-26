package com.spinfosec.service.srv;

import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.entity.SpUpdateServerPackage;
import com.spinfosec.system.RspCode;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IUpdateSrv
 * @Description: 〈升级管理〉
 * @date 2019/1/21
 * @copyright All rights Reserved, Designed By SPINFO
 */
public interface IUpdateSrv
{
	/**
	 * 分页查询升级管理信息
	 * @param queryMap
	 * @return
	 */
	PageInfo<SpUpdateServerPackage> queryUpPackageByPage(Map<String, Object> queryMap);

	/**
	 * 根据条件查找对应的升级包
	 * @param queryMap
	 * @return
	 */
	List<SpUpdateServerPackage> queryUpPackage(Map<String, Object> queryMap);

	/**
	 * 保存系统升级包
	 * @param updateServerPackage
	 */
	void saveUpdatePackage(SpUpdateServerPackage updateServerPackage);

	/**
	 * 上传服务端升级包时，删除库里相同升级包信息
	 * @param idArr
	 */
	void deleteDuplicateServerPackageData(String[] idArr);

	/**
	 * 服务端升级包升级
	 * @param id
	 * @return
	 * @throws Exception
	 */
	RspCode deployServerPackage(String id) throws Exception;

	/**
	 * 重启服务器
	 * @return
	 */
	RspCode restartServer();

	/**
	 * 删除服务端升级包(并删除文件)
	 * @param ids
	 */
	void deleteServerPackage(List<String> ids);
}
