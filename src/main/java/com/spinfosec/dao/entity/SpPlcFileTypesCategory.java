package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [文件类型分组]
 * @ClassName: SpPlcFileTypesCategory
 * @description [文件类型分组]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:06
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpPlcFileTypesCategory implements Serializable
{

    private static final long serialVersionUID = 5872680112325471686L;
    /**
     * 主键
     */
    private String id;
    /**
     * 英文信息
     */
    private String nameEn;
    /**
     * 中文信息
     */
    private String nameCn;
    /**
     * 备注
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


    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }


    public String getNameCn()
    {
        return nameCn;
    }

    public void setNameCn(String nameCn)
    {
        this.nameCn = nameCn;
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
