package com.spinfosec.dao;

import com.spinfosec.dao.entity.*;
import com.spinfosec.dto.pojo.system.CustomSpAdminsBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName AdminDao
 * @Description: 〈用户查询持久层〉
 * @date 2018/10/10
 * All rights Reserved, Designed By SPINFO
 */
public interface AdminDao
{
	/**
	 * 通过用户名查询用户
	 * @param userName
	 * @return
	 */
	public List<SpAdmins> findUserByName(String userName);

	/**
	 * 保存用户和信任主机关系表
	 * @param spAdminTrustHost
	 */
	void saveAdminTrustHost(SpAdminTrustHost spAdminTrustHost);

	/**
	 * 通过信息主机ID删除关系
	 * @param hostId
	 */
	void deldeteAdminTrustHostByHostId(String hostId);

	/**
	 * 通过用户名查询用户（密码和用户名校验）
	 * @param userName
	 * @return
	 */
	List<SpAdmins> getUserByName(String userName);

	/**
	 * 通过用户ID删除关系
	 * @param adminId
	 */
	void deldeteAdminTrustHostByAdminId(String adminId);


	/**
	 * 通过用户名查询用户信息和角色信息
	 * @param userName
	 * @return
	 */
	CustomSpAdminsBean findLogUserInfo(String userName);

	/**
	 * 通过用户id查询用户信息和其角色信息
	 * @param userId
	 * @return
	 */
	CustomSpAdminsBean findLoginInfoByUserId(String userId);

	/**
	 * 根据角色ID获取对应的权限
	 * @param roleId
	 * @return
	 */
	List<SpCodeDecodes> getRoleUrlFunctions(String roleId);

	/**
	 * 获取所有菜单权限
	 * @return
	 */
	List<SpCodeDecodes> getAllCodeDecodes();

	/**
	 * 查询用户
	 * @param querMap
	 * @return
	 */
	List<CustomSpAdminsBean> querySpAdmins(Map<String, Object> querMap);

	/**
	 * 按条件统计用户总数
	 * @param querMap
	 * @return
	 */
	Long countSpAdmins(Map<String, Object> querMap);

	/**
	 * 根据ID删除用户信息
	 * @param id
	 */
	void deleteSpAdminById(String id);

	/**
	 * 保存用户和角色关系
	 * @param spRoleAdminRelation
	 */
	void saveUserAndRole(SpRoleAdminRelation spRoleAdminRelation);

	/**
	 * 根据用户ID删除用户和角色关系表
	 * @param userId
	 */
	void deleteUserAndRole(String userId);

	/**
	 * 保存用户和组织关系
	 * @param orgAdminRelation
	 */
	void saveUserAndOrg(SpOrgAdminRelation orgAdminRelation);

	/**
	 *删除用户和组织关系
	 * @param userId
	 */
	void deleteUserAndOrg(String userId);
	/**
	 * 保存用户信息
	 * @param spAdmins
	 */
	void saveSpAdmin(SpAdmins spAdmins);

	/**
	 * 更新用户信息
	 * @param spAdmins
	 */
	void updateSpAdmin(SpAdmins spAdmins);

	/**
	 * 根据ID查询用户信息
	 * @param id
	 * @return
	 */
	SpAdmins findSpAdminById(String id);

	/**
	 * 根据用户ID获取其创建子用户
	 * @param userId 用户ID
	 * @return
	 */
	List<SpAdmins> getChildByUserId(@Param("userId") String userId);

	/**
	 * 修改用户使用状态 0激活  1注销  2 休眠
	 * @param id 用户ID
	 * @param state 使用状态
	 */
	void updateAdminState(@Param("id") String id, @Param("state") String state);

	/**
	 * 通过用户ID查角色ID
	 * @param userId 用户ID
	 * @return
	 */
	String findRoleIdByUserId(String userId);

	/**
	 * 通过角色ID查用户ID
	 * @param roleId 角色ID
	 * @return
	 */
	List<String> findUserIdByRoleId(String roleId);

	/**
	 * 通过用户ID查组织ID
	 * @param userId
	 * @return
	 */
	String findOrgIdByUserId(String userId);

	/**
	 * 通过用户ID查询信任主机集合
	 * @param userId
	 * @return
	 */
	List<SpAdminHostSetting> findTrustHostByUserId(String userId);

	/**
	 * 获取当前用户的具体信息
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	CustomSpAdminsBean getCurruserUserById(String userId);

	/**
	 * 根据邮箱和用户名查找用户
	 * @param queryMap
	 * @return
	 */
	List<SpAdmins> findUserByEmailAndName(Map<String, Object> queryMap);

	/**
	 * 查找系统管理员的信息
	 * @return
	 */
	SpAdmins findSysadminInfo();
}
