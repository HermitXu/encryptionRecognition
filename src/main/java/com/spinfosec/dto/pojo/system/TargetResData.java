package com.spinfosec.dto.pojo.system;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TargetResData
 * @Description: 〈主机资源测试连接数据〉
 * @date 2018/10/30
 * All rights Reserved, Designed By SPINFO
 */
public class TargetResData
{
	private String id;
	private String iType;
	private String type;
	private String databaseType;
	private String ip;
	private String port;
	private String databaseName;
	private String serverAddress;
	private String site;
	private String path;
	private String dirType;
	private String fqdn;
	private String username;
	private String password;
	private String domain;
	private String tableName;
	private String sql;
	// oracle数据库使用
	private String sid;
	private String readOnly;
	// 金仓用
	private String schemaName;
	private String lotusServerType;
	private String exchangeEdition;
	// 密钥类型：sftp用
	private String keyType;
	// 密钥值： sftp用
	private String publicKey;
	private String publicKeyName;

	// Sqlite和Access数据库使用(走共享文件)
	private String share_username;

	private String share_password;

	// 区分 文件共享 vmdk虚拟机解析
	private String fileType;

	// 虚拟机vmdk解析后资源树选择的文件
	private String endpoint;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getiType()
	{
		return iType;
	}

	public void setiType(String iType)
	{
		this.iType = iType;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDatabaseType()
	{
		return databaseType;
	}

	public void setDatabaseType(String databaseType)
	{
		this.databaseType = databaseType;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	public void setDatabaseName(String databaseName)
	{
		this.databaseName = databaseName;
	}

	public String getServerAddress()
	{
		return serverAddress;
	}

	public void setServerAddress(String serverAddress)
	{
		this.serverAddress = serverAddress;
	}

	public String getSite()
	{
		return site;
	}

	public void setSite(String site)
	{
		this.site = site;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getDirType()
	{
		return dirType;
	}

	public void setDirType(String dirType)
	{
		this.dirType = dirType;
	}

	public String getFqdn()
	{
		return fqdn;
	}

	public void setFqdn(String fqdn)
	{
		this.fqdn = fqdn;
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

	public String getDomain()
	{
		return domain;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public String getSid()
	{
		return sid;
	}

	public void setSid(String sid)
	{
		this.sid = sid;
	}

	public String getReadOnly()
	{
		return readOnly;
	}

	public void setReadOnly(String readOnly)
	{
		this.readOnly = readOnly;
	}

	public String getSchemaName()
	{
		return schemaName;
	}

	public void setSchemaName(String schemaName)
	{
		this.schemaName = schemaName;
	}

	public String getLotusServerType()
	{
		return lotusServerType;
	}

	public void setLotusServerType(String lotusServerType)
	{
		this.lotusServerType = lotusServerType;
	}

	public String getExchangeEdition()
	{
		return exchangeEdition;
	}

	public void setExchangeEdition(String exchangeEdition)
	{
		this.exchangeEdition = exchangeEdition;
	}

	public String getKeyType()
	{
		return keyType;
	}

	public void setKeyType(String keyType)
	{
		this.keyType = keyType;
	}

	public String getPublicKey()
	{
		return publicKey;
	}

	public void setPublicKey(String publicKey)
	{
		this.publicKey = publicKey;
	}

	public String getPublicKeyName()
	{
		return publicKeyName;
	}

	public void setPublicKeyName(String publicKeyName)
	{
		this.publicKeyName = publicKeyName;
	}

	public String getShare_username()
	{
		return share_username;
	}

	public void setShare_username(String share_username)
	{
		this.share_username = share_username;
	}

	public String getShare_password()
	{
		return share_password;
	}

	public void setShare_password(String share_password)
	{
		this.share_password = share_password;
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public String getEndpoint()
	{
		return endpoint;
	}

	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}
}
