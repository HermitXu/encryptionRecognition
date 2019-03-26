package com.spinfosec.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spinfosec.dao.AdminDao;
import com.spinfosec.dao.RoleDao;
import com.spinfosec.dao.SecPasswordPolicyDao;
import com.spinfosec.dao.SysOperateLogDao;
import com.spinfosec.dao.entity.*;
import com.spinfosec.dto.pojo.common.CodeRsp;
import com.spinfosec.dto.pojo.common.TreeData;
import com.spinfosec.dto.pojo.system.AdminReq;
import com.spinfosec.dto.pojo.system.RoleReq;
import com.spinfosec.service.srv.ISystemSrv;
import com.spinfosec.dto.pojo.system.CustomSpAdminsBean;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.EmailUtil;
import com.spinfosec.utils.EncUtil;
import com.spinfosec.utils.GenUtil;
import com.spinfosec.utils.sm2.Sm2Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SystemImpl
 * @Description: 〈系统业务处理接口实现类〉
 * @date 2018/10/11
 * All rights Reserved, Designed By SPINFO
 */

@Transactional
@Service("systemSrv")
public class SystemSrvImpl implements ISystemSrv
{
	private static final Logger log = LoggerFactory.getLogger(SystemSrvImpl.class);

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private SecPasswordPolicyDao secPasswordPolicyDao;

	@Autowired
	private SysOperateLogDao sysOperateLogDao;

	/**
	 * 分页条件查询用户信息
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<CustomSpAdminsBean> querySpAdminsByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<CustomSpAdminsBean> customSpAdminsBeanList = adminDao.querySpAdmins(queryMap);
		PageInfo<CustomSpAdminsBean> pageList = new PageInfo<>(customSpAdminsBeanList);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageList;
	}

	/**
	 * 根据ID查询用户详细信息
	 * @param id
	 * @return
	 */
	@Override
	public AdminReq getAdminReqById(String id)
	{
		AdminReq adminReq = new AdminReq();
		SpAdmins adminById = adminDao.findSpAdminById(id);
		adminReq.setSpAdmins(adminById);
		// 通过用户ID查询角色ID
		String roleIdByUserId = adminDao.findRoleIdByUserId(adminById.getId());
		adminReq.setRoleId(roleIdByUserId);
		// 通过用户ID查询组织信息
		String orgIdByUserId = adminDao.findOrgIdByUserId(adminById.getId());
		adminReq.setOrgId(orgIdByUserId);
		// 通过用户ID查询信任主机集合
		List<String> trustHostIds = new ArrayList<>();
		List<SpAdminHostSetting> hostSettings = adminDao.findTrustHostByUserId(adminById.getId());
		for (SpAdminHostSetting hostSetting : hostSettings)
		{
			trustHostIds.add(hostSetting.getId());
		}

		adminReq.setHostItems(trustHostIds);
		return adminReq;
	}

	/**
	 * 根据用户ID查询用户
	 * @param userId
	 * @return
	 */
	@Override
	public SpAdmins getAdminById(String userId)
	{
		return adminDao.findSpAdminById(userId);
	}

	/**
	 * 根据用户ID获取其创建子用户
	 * @param userId 用户ID
	 * @return
	 */
	@Override
	public List<SpAdmins> getChildByUserId(String userId)
	{
		return adminDao.getChildByUserId(userId);
	}

	/**
	 * 根据角色ID获取角色信息
	 * @param id
	 * @return
	 */
	@Override
	public SpRoles getRoleById(String id)
	{
		return roleDao.getRoleById(id);
	}

	/**
	 * 保存用户信息
	 * @param data
	 */
	@Override
	public void saveSpAdmin(AdminReq data, String userId)
	{
		SpAdmins spAdmins = data.getSpAdmins();
		String adminId = GenUtil.getUUID();
		spAdmins.setId(adminId);
		spAdmins.setDefinitionType(Contants.C_USER_DEFINE);
		spAdmins.setRegistrationDate(new Date());
		spAdmins.setCreatedBy(userId);

		// 保存用户信任主机关系
		List<String> secHostIds = data.getHostItems();
		if (null != secHostIds && !secHostIds.isEmpty())
		{
			for (String hostId : secHostIds)
			{
				String relationId = GenUtil.getUUID();
				SpAdminTrustHost adminTrustHost = new SpAdminTrustHost();
				adminTrustHost.setId(relationId);
				adminTrustHost.setAdminId(adminId);
				adminTrustHost.setHostId(hostId);
				adminDao.saveAdminTrustHost(adminTrustHost);
			}
		}
		// 保存用户和角色的关系
		SpRoleAdminRelation roleAdminRelation = new SpRoleAdminRelation();
		roleAdminRelation.setId(GenUtil.getUUID());
		roleAdminRelation.setAdminId(spAdmins.getId());
		roleAdminRelation.setRoleId(data.getRoleId());
		adminDao.saveUserAndRole(roleAdminRelation);

		// 保存用户和组织关系
		SpOrgAdminRelation orgAdminRelation = new SpOrgAdminRelation();
		orgAdminRelation.setId(GenUtil.getUUID());
		orgAdminRelation.setAdminId(spAdmins.getId());
		orgAdminRelation.setOrgId(data.getOrgId());
		adminDao.saveUserAndOrg(orgAdminRelation);

		// 保存用户
		adminDao.saveSpAdmin(spAdmins);
	}

	/**
	 * 更新用户信息
	 * @param data
	 */
	@Override
	public void updateSpAdmin(AdminReq data)
	{
		SpAdmins tempSpAdminsBean = adminDao.findSpAdminById(data.getSpAdmins().getId());

		data.getSpAdmins().setRegistrationDate(tempSpAdminsBean.getRegistrationDate());
		data.getSpAdmins().setPasswordChangeFlag(tempSpAdminsBean.getPasswordChangeFlag());
		data.getSpAdmins().setCreatedBy(tempSpAdminsBean.getCreatedBy());
		data.getSpAdmins().setDefinitionType(tempSpAdminsBean.getDefinitionType());
		// 如果更新密码，则同时更新密码修改时间；否则密码修改时间保持原状
		if (StringUtils.isNotEmpty(data.getSpAdmins().getPassword()))
		{
			data.getSpAdmins().setPasswordModifyDate(new Date());
		}
		else
		{
			data.getSpAdmins().setPasswordModifyDate(tempSpAdminsBean.getPasswordModifyDate());
		}
		// 删除对应的用户和信任主机关系
		adminDao.deldeteAdminTrustHostByAdminId(data.getSpAdmins().getId());
		// 重新添加用户和信息主机关系
		List<String> secHostIds = data.getHostItems();
		String adminId = data.getSpAdmins().getId();
		if (null != secHostIds && !secHostIds.isEmpty())
		{
			for (String hostId : secHostIds)
			{
				String relationId = GenUtil.getUUID();
				SpAdminTrustHost adminTrustHost = new SpAdminTrustHost();
				adminTrustHost.setId(relationId);
				adminTrustHost.setAdminId(adminId);
				adminTrustHost.setHostId(hostId);
				adminDao.saveAdminTrustHost(adminTrustHost);
			}
		}

		// 先删除用户和角色关系
		adminDao.deleteUserAndRole(adminId);
		// 重新保存用户和角色关系
		SpRoleAdminRelation spRoleAdminRelation = new SpRoleAdminRelation();
		spRoleAdminRelation.setId(GenUtil.getUUID());
		spRoleAdminRelation.setAdminId(data.getSpAdmins().getId());
		spRoleAdminRelation.setRoleId(data.getRoleId());
		adminDao.saveUserAndRole(spRoleAdminRelation);

		// 先删除用户和组织关系
		adminDao.deleteUserAndOrg(adminId);
		// 重新保存用户和组织关系
		SpOrgAdminRelation orgAdminRelation = new SpOrgAdminRelation();
		orgAdminRelation.setId(GenUtil.getUUID());
		orgAdminRelation.setAdminId(adminId);
		orgAdminRelation.setOrgId(data.getOrgId());
		adminDao.saveUserAndOrg(orgAdminRelation);

		// 更新用户
		adminDao.updateSpAdmin(data.getSpAdmins());

	}

	/**
	 * 更新用户信息
	 * @param admins
	 */
	@Override
	public void updateSpAdmin(SpAdmins admins)
	{
		adminDao.updateSpAdmin(admins);
	}

	/**
	 * 删除用户信息
	 * @return
	 */
	@Override
	public RspCode deleteSpAdmins(List<String> idList)
	{
		RspCode rspCode = RspCode.SUCCESS;
		for (String id : idList)
		{
			// 预制用户不能删除
			SpAdmins spAdminById = adminDao.findSpAdminById(id);
			if (Contants.C_PRE_DEFINE.equals(spAdminById.getDefinitionType()))
			{
				rspCode = RspCode.USER_PRE_DEFINE_NO_DELETING;
				return rspCode;
			}
			// 删除用户和信息主机关系
			adminDao.deldeteAdminTrustHostByAdminId(id);
			// 删除用户和角色关系表
			adminDao.deleteUserAndRole(id);
			// 删除用户和组织关系
			adminDao.deleteUserAndOrg(id);
			// 删除用户信息
			adminDao.deleteSpAdminById(id);
		}
		return rspCode;
	}

	/**
	 * 分页条件查询角色
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<SpRoles> querySpRolesByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<SpRoles> rolesList = roleDao.querySpRolesByPage(queryMap);
		PageInfo<SpRoles> pageList = new PageInfo<>(rolesList);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageList;
	}

	/**
	 * 根据角色ID获取对应权限
	 * @return
	 */
	@Override
	public List<TreeData> getMenuByRoleId(String roleId)
	{
		List<SpCodeDecodes> allPermission = roleDao.getPermissionByRole(roleId);
		List<TreeData> treeData = GenUtil.getModuleTree(allPermission, false);
		return treeData;
	}

	/**
	 * 根据角色ID获取该角色的拥有权限和角色信息
	 * @param roleId
	 * @return
	 */
	@Override
	public RoleReq getRoleDataById(String roleId)
	{
		// 根据ID查询角色信息
		RoleReq roleReq = new RoleReq();
		SpRoles roleById = roleDao.getRoleById(roleId);
		roleReq.setSpRoles(roleById);

		// 根据角色ID获取所拥有的模块
		List<SpRoleModulePermissions> permissionsList = roleDao.getModulePermissions(roleId);
		roleReq.setSpRoleModulePermissions(permissionsList);
		return roleReq;
	}

	/**
	 * 获取所有角色信息
	 * @return
	 */
	@Override
	public List<SpRoles> getAllSpRole(String roleId)
	{
		return roleDao.getAllSpRole(roleId);
	}

	/**
	 * 保存角色信息
	 *
	 * @return
	 */
	@Override
	public void saveSpRoles(RoleReq data, String userId)
	{
		SpRoles role = data.getSpRoles();
		String roleId = GenUtil.getUUID();
		role.setId(roleId);
		role.setCreateDate(new Date());
		role.setCreatedBy(userId);
		role.setDefinitionType(Contants.C_USER_DEFINE);
		role.setRoleType(Contants.C_USER_DEFINE);
		// 保存角色
		roleDao.savaSpRole(role);
		// 保存权限
		List<SpRoleModulePermissions> roleModuleList = data.getSpRoleModulePermissions();
		for (SpRoleModulePermissions spRoleModulePermissions : roleModuleList)
		{
			String id = GenUtil.getUUID();
			spRoleModulePermissions.setId(id);
			spRoleModulePermissions.setRoleId(roleId);
			roleDao.saveModulePermissions(spRoleModulePermissions);
		}
	}

	/**
	 * 修改角色信息
	 *
	 * @return
	 */
	@Override
	public void updateSpRoles(RoleReq data)
	{
		String roleId = data.getSpRoles().getId();
		if (StringUtils.isEmpty(roleId))
		{
			log.error("更新角色失败,失败原因 : 根据角色ID查询不到该角色!");
			throw new TMCException(RspCode.PARAMERTER_ERROR);
		}
		SpRoles role = roleDao.getRoleById(roleId);
		role.setName(data.getSpRoles().getName());
		role.setDescription(data.getSpRoles().getDescription());
		// 更新角色信息
		roleDao.updateSpRole(role);
		// 非预置角色将原先的权限清空
		if (StringUtils.isNotEmpty(role.getDefinitionType()) && !Contants.C_PRE_DEFINE.equals(role.getDefinitionType()))
		{
			// 先删除原来的权限信息
			roleDao.deleteModulePermissionsByRoleId(roleId);
			// 重新保存权限
			List<SpRoleModulePermissions> roleModuleList = data.getSpRoleModulePermissions();
			for (SpRoleModulePermissions spRoleModulePermissions : roleModuleList)
			{
				String id = GenUtil.getUUID();
				spRoleModulePermissions.setId(id);
				spRoleModulePermissions.setRoleId(roleId);
				roleDao.saveModulePermissions(spRoleModulePermissions);
			}
		}
	}

	/**
	 * 删除角色信息
	 *
	 * @return
	 */
	@Override
	public boolean deleteSpRoles(List<String> idList)
	{
		boolean result = true;
		for (String id : idList)
		{
			SpRoles role = roleDao.getRoleById(id);
			if (null != role && "SYSTEM".equals(role.getRoleType()))
			{
				result = false;
				continue;
			}

			// List<String> userIds = adminDao.findUserIdByRoleId(role.getId());
			// if (null != userIds && userIds.size() > 0)
			// {
			// result = false;
			// continue;
			// }

			// 删除该角色下对应的权限信息
			roleDao.deleteModulePermissionsByRoleId(role.getId());
			// 删除用户和角色关系表
			// roleDao.deleteUserAndRole(role.getId());
			// 删除角色信息
			roleDao.deleteRoleById(id);
		}
		return result;
	}

	/**
	 * 分页查询系统操作日志
	 * @param queryMap
	 * @return
	 */
	@Override
	public PageInfo<SpSystemOperateLogInfo> queryOperateLogByPage(Map<String, Object> queryMap)
	{
		Integer pageNum = Integer.valueOf(queryMap.get("currentPage").toString());
		Integer pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		PageHelper.startPage(pageNum, pageSize);
		List<SpSystemOperateLogInfo> systemOperateLogInfoList = sysOperateLogDao.queryOperateLog(queryMap);
		PageInfo<SpSystemOperateLogInfo> pageList = new PageInfo<>(systemOperateLogInfoList);
		// 手动清理 ThreadLocal 存储的分页参 否则PageHelper会自动加上 limit
		PageHelper.clearPage();
		return pageList;
	}

	/**
	 * 忘记密码
	 * @param username
	 * @param email
	 */
	@Override
	public void forgetPassword(String username, String email) throws EmailException
	{
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("email", email);
		queryMap.put("userName", username);
		List<SpAdmins> users = adminDao.findUserByEmailAndName(queryMap);
		if (null != users && users.size() > 0)
		{
			SpAdmins user = users.get(0);
			// 获取密码策略和复杂度
			List<SpSecPasswordComplexityItem> complexityItem = secPasswordPolicyDao.getComplexityItem();
			SpSecPasswordPolicy secPasswordPolicy = secPasswordPolicyDao.getSpSecPasswordPolicy();
			int passwordLengthMin = Integer.parseInt(secPasswordPolicy.getPasswordLengthMin());
			int passwordLengthMax = Integer.parseInt(secPasswordPolicy.getPasswordLengthMax());
			Random random = new Random();
			// 得到一个符合密码策略中设置的随机密码长度
			int randomNum = (random.nextInt(passwordLengthMax - passwordLengthMin + 1)) + passwordLengthMin;
			// 得到密码复杂度
			List<String> spSecPasswordComplexityItemIdList = new ArrayList<String>();
			for (SpSecPasswordComplexityItem spSecPasswordComplexityItem : complexityItem)
			{
				if ("1".equals(spSecPasswordComplexityItem.getIsEnable()))
				{
					spSecPasswordComplexityItemIdList.add(spSecPasswordComplexityItem.getId());
				}
			}
			// 生成随机密码
			String initPwd = EncUtil.getRandomPwdBySecPasswordPolicy(randomNum, spSecPasswordComplexityItemIdList);

			// 发送邮箱
			EmailUtil.sendTextMail(user.getEmail(), null, "hi " + user.getName() + ",\n\t您在加密文件检查系统的账号为："
					+ user.getUsername() + ",新密码为：" + initPwd + "\n\n\t登录系统后，可在\"个人中心\"修改您的密码。", "加密文件检查系统-密码重置");

			// 修改用户状态
			user.setPassword(Sm2Utils.encryptString(Sm2Utils.PUBLIC_KEY, initPwd.getBytes()));
			user.setPasswordModifyDate(new Date());
			adminDao.updateSpAdmin(user);

		}
		else
		{
			throw new TMCException(RspCode.ACCOUNT_NOT_EXIST);
		}
	}

	/**
	 * 首次/密码过期修改密码
	 * @param oldPwd
	 * @param newPwd
	 * @param currentUserId
	 * @return
	 */
	@Override
	public CodeRsp updatePassword(String oldPwd, String newPwd, String currentUserId)
	{
		CodeRsp codeRsp = new CodeRsp(RspCode.SUCCESS);
		SpAdmins adminById = adminDao.findSpAdminById(currentUserId);

		if (null != adminById)
		{
			oldPwd = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, oldPwd));
			String pwdFromDb = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, adminById.getPassword()));

			if (!oldPwd.equals(pwdFromDb))
			{
				codeRsp = new CodeRsp(RspCode.OLD_PASSWORD_ERROR);
			}
			else
			{
				adminById.setPasswordChangeFlag(Double.valueOf("1"));
				adminById.setPasswordModifyDate(new Date());
				adminById.setPassword(newPwd);
				adminDao.updateSpAdmin(adminById);
			}
		}
		else
		{
			codeRsp = new CodeRsp(RspCode.USER_NOT_EXIST);
		}

		return codeRsp;
	}

	/**
	 * 记录系统操作日志
	 * @param operateLogInfo 需要记录的日志
	 */
	@Override
	public void saveOperateLog(SpSystemOperateLogInfo operateLogInfo)
	{
		operateLogInfo.setId(GenUtil.getUUID());
		sysOperateLogDao.saveOperateLog(operateLogInfo);
	}

	/**
	 * 修改用户使用状态 0激活  1注销  2 休眠
	 * @param id  用户ID
	 * @param state  使用状态
	 */
	@Override
	public void updateAdminState(String id, String state)
	{
		adminDao.updateAdminState(id, state);
	}

	/**
	 * 用户管理中重置密码
	 * @param id
	 * @param newPwd
	 * @param emailSend
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@Override
	public CodeRsp resetPwd(String id, String newPwd, String emailSend, HttpServletRequest req) throws Exception
	{
		// 查询用户
		SpAdmins user = adminDao.findSpAdminById(id);
		// 对新密码进行解密
		String newPassword = new String(Sm2Utils.decrypt(Sm2Utils.PRIVATE_KEY, newPwd));
		if (null != user && StringUtils.isNotEmpty(user.getEmail()))
		{
			// 更新密码
			user.setPassword(newPwd);
			user.setPasswordChangeFlag(1);
			user.setPasswordModifyDate(new Date());
			// 如果该用户自动发送密码邮件,则向该邮箱发送邮件
			if ("1".equals(emailSend))
			{
				EmailUtil.sendTextMail(user.getEmail(), null, "hi " + user.getName() + ",\n\t您在加密文件检查系统的账号为："
						+ user.getName() + ",新密码为：" + newPassword + "\n\n\t登录系统后，可在\"个人中心\"修改您的密码。", "加密文件检查系统-密码重置");
			}
			adminDao.updateSpAdmin(user);
			return new CodeRsp(RspCode.SUCCESS);
		}
		else
		{
			return new CodeRsp(RspCode.OBJECE_NOT_EXIST);
		}
	}
}
