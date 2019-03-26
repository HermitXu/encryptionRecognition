package com.spinfosec.dto.pojo.system;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName AdminHostSettingData
 * @Description: 〈信任主机信息模型〉
 * @date 2018/10/15
 * All rights Reserved, Designed By SPINFO
 */
public class TrustHostBean
{
	/**
	 * 信息主机id
	 */
	private String id;

	/**
	 * 信任主机名称
	 */
	private String name;

	/**
	 * 信任主机IP
	 */
	private String hostIp;

	/**
	 * 创建此信任主机的用户ID
	 */
	private String userCreateId;

	/**
	 * 创建此信任主机的角色ID
	 */
	private String roleCreateId;

	/**
	 * 所属角色名称
	 */
	private String roleCreateName;

	/**
	 * 所属用户名称
	 */
	private String userCreateName;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 描述
	 */
	private String description;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getHostIp()
	{
		return hostIp;
	}

	public void setHostIp(String hostIp)
	{
		this.hostIp = hostIp;
	}

	public String getUserCreateId()
	{
		return userCreateId;
	}

	public void setUserCreateId(String userCreateId)
	{
		this.userCreateId = userCreateId;
	}

	public String getRoleCreateId()
	{
		return roleCreateId;
	}

	public void setRoleCreateId(String roleCreateId)
	{
		this.roleCreateId = roleCreateId;
	}

	public String getRoleCreateName()
	{
		return roleCreateName;
	}

	public void setRoleCreateName(String roleCreateName)
	{
		this.roleCreateName = roleCreateName;
	}

	public String getUserCreateName()
	{
		return userCreateName;
	}

	public void setUserCreateName(String userCreateName)
	{
		this.userCreateName = userCreateName;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
