package com.spinfosec.service.srv;

import com.spinfosec.dao.entity.SpAdmins;
import com.spinfosec.dto.pojo.common.Token;
import com.spinfosec.dto.pojo.common.TreeData;
import com.spinfosec.dto.pojo.system.CustomSpAdminsBean;
import com.spinfosec.dto.pojo.system.UserData;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IAuthSrv
 * @Description: 〈鉴权服务接口〉
 * @date 2018/10/9
 * All rights Reserved, Designed By SPINFO
 */
public interface IAuthSrv
{

	/**
	 * 通过用户名或密码查询用户
	 * @param userName 用户名
	 * @return
	 */
	public List<SpAdmins> findUserByName(String userName);

	/**
	 * 通过用户名查询用户信息和角色信息
	 * @param userName
	 * @return
	 */
	CustomSpAdminsBean findLogUserInfo(String userName);

	/**
	 * 通过用户ID获取信任主机IP
	 * @param userId
	 * @return
	 */
	List<String> getHostIpByUserId(String userId);

	/**
	 * 鉴权获取token
	 * @return
	 */
	public Token getToken();

	/**
	 * 初始化用户权限
	 *
	 * @param userId
	 */
	public List<TreeData> initRoleFunctions(String userId) throws IOException;

	/**
	 *
	 * @Title: setLoginInfo
	 * @Description: TODO(设置登录失败次数信息)
	 * @param: @param remoteIp
	 * @param: @param authName
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public String setLoginInfo(String loginIp, String logUserName, Integer maxLoginTimes);

	/**
	 * 获取当前用户的具体信息
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public CustomSpAdminsBean getCurruserUserById(String userId) throws Exception;

	/**
	 * 修改当前用户密码
	 * @param data
	 * @param req
	 */
	public void updateCurrentUser(UserData data, HttpServletRequest req);

}
