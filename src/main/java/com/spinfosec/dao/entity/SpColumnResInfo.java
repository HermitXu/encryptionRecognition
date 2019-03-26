package com.spinfosec.dao.entity;

/**
 * 列设置关系表
 *
 * @title [列设置关系表]
 * @description [列设置关系表]
 * @copyright Copyright 2017 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @author liugang
 * @version v 1.0
 * @create 2017-3-3 下午5:22:34
 */
public class SpColumnResInfo implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	private String id;

	private String tableName;

	private String columnName;

	private String ip;

	private String type;

	private String keyType;

	private String username;

	private String password;

	private String path;

	private String domain;

	private String publickey;

	private String port;

	private String publicKeyName;

	private String targetResId;

	private String collectionPolicyId;

	// 路径类型,relative相对路径,absolute绝对路径
	private String pathRelaBType;

	private String flagColumn2;

	public String getTargetResId()
	{
		return targetResId;
	}

	public void setTargetResId(String targetResId)
	{
		this.targetResId = targetResId;
	}

	public String getCollectionPolicyId()
	{
		return collectionPolicyId;
	}

	public void setCollectionPolicyId(String collectionPolicyId)
	{
		this.collectionPolicyId = collectionPolicyId;
	}

	public String getPathRelaBType()
	{
		return pathRelaBType;
	}

	public void setPathRelaBType(String pathRelaBType)
	{
		this.pathRelaBType = pathRelaBType;
	}

	public String getFlagColumn2()
	{
		return flagColumn2;
	}

	public void setFlagColumn2(String flagColumn2)
	{
		this.flagColumn2 = flagColumn2;
	}

	public String getPublickey()
	{
		return publickey;
	}

	public void setPublickey(String publickey)
	{
		this.publickey = publickey;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getPublicKeyName()
	{
		return publicKeyName;
	}

	public void setPublicKeyName(String publicKeyName)
	{
		this.publicKeyName = publicKeyName;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getKeyType()
	{
		return keyType;
	}

	public void setKeyType(String keyType)
	{
		this.keyType = keyType;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getDomain()
	{
		return domain;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}
}
