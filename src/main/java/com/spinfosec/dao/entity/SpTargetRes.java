package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [目标信息表]
 * @ClassName: SpTargetRes
 * @description [目标信息表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 20:00
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpTargetRes implements Serializable
{

    private static final long serialVersionUID = 5865481270770016000L;
    /**
     * 主键
     */
    private String id;
    /**
     * IP
     */
    private String ip;
    /**
     * 计算机全名
     */
    private String fqdn;
    /**
     * 类型  和策略类型保持一致
     */
    private String resType;
    /**
     * 站点（sharepoint）
     */
    private String site;
    /**
     * 服务器地址 exchange
     */
    private String serverAddress;
    /**
     * 数据库类型
     */
    private String databaseType;
    /**
     * 数据库版本
     */
    private String databaseVersion;
    /**
     * 端口
     */
    private String port;
    /**
     * 数据库名称
     */
    private String databaseName;
    /**
     * 模式名
     */
    private String schemaName;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * Sqlite和Access数据库使用(共享文件的用户名和密码)
     */
    private String shareUsername;
    /**
     * Sqlite和Access数据库使用(共享文件的用户名和密码)
     */
    private String sharePassword;
    /**
     *
     */
    private String vmdkPath;
    /**
     * 域名
     */
    private String domain;
    /**
     * sid oracle
     */
    private String sid;
    /**
     * 是否扫描附件 Lotus  1 是     0 否
     */
    private String isScanAttachment;
    /**
     * 是否启用安全连接  Exchange扫描使用   1 是  0 否
     */
    private String isSsl;
    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    /**
     * 创建者
     */
    private String createdBy;
    /**
     * 最后修改时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastModifyDate;
    /**
     * Lotus服务类型
     */
    private String lotusServerType;
    /**
     * exchange版本
     */
    private String exchangeEdition;
    /**
     * 密码类型
     */
    private String passwordType;
    /**
     * 密钥类型
     */
    private String keyType;
    /**
     * 密钥
     */
    private String publicKey;
    /**
     * 密钥名称
     */
    private String publicKeyName;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }


    public String getFqdn()
    {
        return fqdn;
    }

    public void setFqdn(String fqdn)
    {
        this.fqdn = fqdn;
    }


    public String getResType()
    {
        return resType;
    }

    public void setResType(String resType)
    {
        this.resType = resType;
    }


    public String getSite()
    {
        return site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }


    public String getServerAddress()
    {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }


    public String getDatabaseType()
    {
        return databaseType;
    }

    public void setDatabaseType(String databaseType)
    {
        this.databaseType = databaseType;
    }


    public String getDatabaseVersion()
    {
        return databaseVersion;
    }

    public void setDatabaseVersion(String databaseVersion)
    {
        this.databaseVersion = databaseVersion;
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


    public String getSchemaName()
    {
        return schemaName;
    }

    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
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


    public String getShareUsername()
    {
        return shareUsername;
    }

    public void setShareUsername(String shareUsername)
    {
        this.shareUsername = shareUsername;
    }


    public String getSharePassword()
    {
        return sharePassword;
    }

    public void setSharePassword(String sharePassword)
    {
        this.sharePassword = sharePassword;
    }


    public String getVmdkPath()
    {
        return vmdkPath;
    }

    public void setVmdkPath(String vmdkPath)
    {
        this.vmdkPath = vmdkPath;
    }


    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }


    public String getSid()
    {
        return sid;
    }

    public void setSid(String sid)
    {
        this.sid = sid;
    }


    public String getIsScanAttachment()
    {
        return isScanAttachment;
    }

    public void setIsScanAttachment(String isScanAttachment)
    {
        this.isScanAttachment = isScanAttachment;
    }


    public String getIsSsl()
    {
        return isSsl;
    }

    public void setIsSsl(String isSsl)
    {
        this.isSsl = isSsl;
    }


    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }


    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    public Date getLastModifyDate()
    {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate)
    {
        this.lastModifyDate = lastModifyDate;
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


    public String getPasswordType()
    {
        return passwordType;
    }

    public void setPasswordType(String passwordType)
    {
        this.passwordType = passwordType;
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

}
