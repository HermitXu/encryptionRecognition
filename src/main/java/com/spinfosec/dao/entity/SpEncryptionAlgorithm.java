package com.spinfosec.dao.entity;

import java.io.Serializable;
/**
 * @title [算法表]
 * @ClassName: SpEncryptionAlgorithm
 * @description [算法表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 18:41
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpEncryptionAlgorithm implements Serializable
{

    private static final long serialVersionUID = -4912207608926806016L;
    /**
     * 主键
     */
    private String id;
    /**
     * 算法名称
     */
    private String name;
    /**
     * 备注
     */
    private String description;
    /**
     * 算法类型 国密  NATIONAL   非国密  INTERNATIONAL
     */
    private String type;


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


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

}
