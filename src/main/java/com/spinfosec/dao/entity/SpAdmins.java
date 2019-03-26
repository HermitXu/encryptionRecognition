package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title [用户表]
 * @ClassName: SpAdmins
 * @description [用户表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 16:01
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpAdmins implements Serializable
{

    private static final long serialVersionUID = 268038169013944018L;
    /**
     * 主键
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 身份证号
     */
    private String idCard;

	/**
	 * 手机号
	 */
	private String phone;
    /**
     * 密码修改标志
     */
    private double passwordChangeFlag;
    /**
     * 密码修改时间
     */
    private Date passwordModifyDate;
    /**
     * 上次登录时间
     */
    private Date lastLoginTime;
    /**
	 * 用户使用状态 0激活  1注销  2 休眠  3已过期
	 */
    private double accountIsDisabled;
    /**
     * 过期时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationDate;
    /**
     * 1  本地用户     默认
     */
    private double userType;
	/**
	 * 创建该用户的用户ID
	 * 三权用户各自管理自己创建的用户
	 */
	private String createdBy;
    /**
     * 备注
     */
    private String description;
    /**
     * 注册时间
     */
    private Date registrationDate;
    /**
     * C_USER_DEFINE   自定义        C_PRE_DEFINE  预定义
     */
    private String definitionType;
    /**
     * 外部用户dn
     */
    private String externalUserDn;
    /**
     * ukey相关
     */
    private String sm2PubKeyX;
    /**
     * ukey相关
     */
    private String sm2PubKeyY;
    /**
     * ukey相关
     */
    private String usbkeyId;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }


    public double getPasswordChangeFlag()
    {
        return passwordChangeFlag;
    }

    public void setPasswordChangeFlag(double passwordChangeFlag)
    {
        this.passwordChangeFlag = passwordChangeFlag;
    }


    public Date getPasswordModifyDate()
    {
        return passwordModifyDate;
    }

    public void setPasswordModifyDate(Date passwordModifyDate)
    {
        this.passwordModifyDate = passwordModifyDate;
    }


    public Date getLastLoginTime()
    {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime)
    {
        this.lastLoginTime = lastLoginTime;
    }


    public double getAccountIsDisabled()
    {
        return accountIsDisabled;
    }

    public void setAccountIsDisabled(double accountIsDisabled)
    {
        this.accountIsDisabled = accountIsDisabled;
    }


    public Date getExpirationDate()
    {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate)
    {
        this.expirationDate = expirationDate;
    }


    public double getUserType()
    {
        return userType;
    }

    public void setUserType(double userType)
    {
        this.userType = userType;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


    public Date getRegistrationDate()
    {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate)
    {
        this.registrationDate = registrationDate;
    }


    public String getDefinitionType()
    {
        return definitionType;
    }

    public void setDefinitionType(String definitionType)
    {
        this.definitionType = definitionType;
    }


    public String getExternalUserDn()
    {
        return externalUserDn;
    }

    public void setExternalUserDn(String externalUserDn)
    {
        this.externalUserDn = externalUserDn;
    }


    public String getSm2PubKeyX()
    {
        return sm2PubKeyX;
    }

    public void setSm2PubKeyX(String sm2PubKeyX)
    {
        this.sm2PubKeyX = sm2PubKeyX;
    }


    public String getSm2PubKeyY()
    {
        return sm2PubKeyY;
    }

    public void setSm2PubKeyY(String sm2PubKeyY)
    {
        this.sm2PubKeyY = sm2PubKeyY;
    }


    public String getUsbkeyId()
    {
        return usbkeyId;
    }

    public void setUsbkeyId(String usbkeyId)
    {
        this.usbkeyId = usbkeyId;
    }

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}
}
