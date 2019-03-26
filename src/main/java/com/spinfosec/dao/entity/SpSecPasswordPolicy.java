package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [密码安全策略]
 * @ClassName: SpSecPasswordPolicy
 * @description [密码安全策略]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:35
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpSecPasswordPolicy implements Serializable
{

    private static final long serialVersionUID = 8467736677926418197L;
    /**
     * 主键
     */
    private String id;
    /**
	 * 密码有效期
	 */
    private String passwordValidity;
    /**
	 * 密码最小长度
	 */
    private String passwordLengthMin;
    /**
	 * 密码最大长度
	 */
    private String passwordLengthMax;
    /**
	 * 最大尝试登陆次数
	 */
    private String maxLoginTimes;
    /**
	 * 首次登陆是否修改密码
	 */
    private String isModifyPasswordFirst;
    /**
	 * 是否启用uKey登陆
	 */
    private String ukeyEnable;
    /**
	 * 是否可以重复登录
	 */
    private String isRepeatLogin;
    /**
	 * 是否启用信任主机
	 */
    private String sechostEnable;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getPasswordValidity()
    {
        return passwordValidity;
    }

    public void setPasswordValidity(String passwordValidity)
    {
        this.passwordValidity = passwordValidity;
    }


    public String getPasswordLengthMin()
    {
        return passwordLengthMin;
    }

    public void setPasswordLengthMin(String passwordLengthMin)
    {
        this.passwordLengthMin = passwordLengthMin;
    }


    public String getPasswordLengthMax()
    {
        return passwordLengthMax;
    }

    public void setPasswordLengthMax(String passwordLengthMax)
    {
        this.passwordLengthMax = passwordLengthMax;
    }


    public String getMaxLoginTimes()
    {
        return maxLoginTimes;
    }

    public void setMaxLoginTimes(String maxLoginTimes)
    {
        this.maxLoginTimes = maxLoginTimes;
    }


    public String getIsModifyPasswordFirst()
    {
        return isModifyPasswordFirst;
    }

    public void setIsModifyPasswordFirst(String isModifyPasswordFirst)
    {
        this.isModifyPasswordFirst = isModifyPasswordFirst;
    }


    public String getUkeyEnable()
    {
        return ukeyEnable;
    }

    public void setUkeyEnable(String ukeyEnable)
    {
        this.ukeyEnable = ukeyEnable;
    }


    public String getIsRepeatLogin()
    {
        return isRepeatLogin;
    }

    public void setIsRepeatLogin(String isRepeatLogin)
    {
        this.isRepeatLogin = isRepeatLogin;
    }


    public String getSechostEnable()
    {
        return sechostEnable;
    }

    public void setSechostEnable(String sechostEnable)
    {
        this.sechostEnable = sechostEnable;
    }

}
