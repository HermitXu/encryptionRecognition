package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [主题设置]
 * @ClassName: SpThemeInfoSetting
 * @description [主题设置]
 * @author Administrator
 * @version v 1.0
 * @create 2018/9/6 20:29
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpThemeInfoSetting implements Serializable
{

    private static final long serialVersionUID = -6632882735077158940L;
    /**
     * 主键id
     */
    private String id;

    /**
     * 主题颜色
     */
    private String themeColor;

    /**
     * 按钮点击颜色
     */
    private String btnClickColor;

    /**
     * 公司名称
     */
    private String companyInfo;

    /**
     * 公司名称英文
     */
    private String companyInfoEn;

    /**
     * 公司网址
     */
    private String companyLink;

    /**
     * 公司电话
     */
    private String companyTelephone;

    /**
     * 是否显示帮助按钮
     */
    private String isShowHelp;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getThemeColor()
    {
        return themeColor;
    }

    public void setThemeColor(String themeColor)
    {
        this.themeColor = themeColor;
    }


    public String getBtnClickColor()
    {
        return btnClickColor;
    }

    public void setBtnClickColor(String btnClickColor)
    {
        this.btnClickColor = btnClickColor;
    }


    public String getCompanyInfo()
    {
        return companyInfo;
    }

    public void setCompanyInfo(String companyInfo)
    {
        this.companyInfo = companyInfo;
    }


    public String getCompanyInfoEn()
    {
        return companyInfoEn;
    }

    public void setCompanyInfoEn(String companyInfoEn)
    {
        this.companyInfoEn = companyInfoEn;
    }


    public String getCompanyLink()
    {
        return companyLink;
    }

    public void setCompanyLink(String companyLink)
    {
        this.companyLink = companyLink;
    }


    public String getCompanyTelephone()
    {
        return companyTelephone;
    }

    public void setCompanyTelephone(String companyTelephone)
    {
        this.companyTelephone = companyTelephone;
    }


    public String getIsShowHelp()
    {
        return isShowHelp;
    }

    public void setIsShowHelp(String isShowHelp)
    {
        this.isShowHelp = isShowHelp;
    }

}
