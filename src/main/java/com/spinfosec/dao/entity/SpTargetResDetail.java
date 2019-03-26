package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [标题]
 * @ClassName: SpTargetResDetail
 * @description [一句话描述]
 * @author Administrator
 * @version v 1.0
 * @create 2018/9/6 20:10
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpTargetResDetail implements Serializable
{

    private static final long serialVersionUID = 81293899330281736L;
    private String id;
    private String targetResId;
    private String name;
    private String path;
    private String displayPath;
    private String fileFolderType;
    private String fileSize;
    private String modifyDateTs;
    private String modifyDateTz;
    private String databaseName;
    private String isScanAllOnDatabase;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getTargetResId()
    {
        return targetResId;
    }

    public void setTargetResId(String targetResId)
    {
        this.targetResId = targetResId;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }


    public String getDisplayPath()
    {
        return displayPath;
    }

    public void setDisplayPath(String displayPath)
    {
        this.displayPath = displayPath;
    }


    public String getFileFolderType()
    {
        return fileFolderType;
    }

    public void setFileFolderType(String fileFolderType)
    {
        this.fileFolderType = fileFolderType;
    }


    public String getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(String fileSize)
    {
        this.fileSize = fileSize;
    }


    public String getModifyDateTs()
    {
        return modifyDateTs;
    }

    public void setModifyDateTs(String modifyDateTs)
    {
        this.modifyDateTs = modifyDateTs;
    }


    public String getModifyDateTz()
    {
        return modifyDateTz;
    }

    public void setModifyDateTz(String modifyDateTz)
    {
        this.modifyDateTz = modifyDateTz;
    }


    public String getDatabaseName()
    {
        return databaseName;
    }

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }


    public String getIsScanAllOnDatabase()
    {
        return isScanAllOnDatabase;
    }

    public void setIsScanAllOnDatabase(String isScanAllOnDatabase)
    {
        this.isScanAllOnDatabase = isScanAllOnDatabase;
    }

}
