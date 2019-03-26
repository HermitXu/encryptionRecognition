package com.spinfosec.dao.entity;


import java.io.Serializable;

/**
 * @title [升级管理]
 * @ClassName: SpUpdateServerPackage
 * @description [升级管理]
 * @author Administrator
 * @version v 1.0
 * @create 2018/9/6 20:33
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpUpdateServerPackage implements Serializable
{

    private static final long serialVersionUID = 3985568863715091500L;
    /**
     * 主键
     */
    private String id;
    /**
     * 系统类型
     */
    private String systemType;
    /**
     * 升级包依赖版本
     */
    private String version;
    /**
     * 升级包时间戳
     */
    private String timeStamp;
    /**
     * 升级包次版本号
     */
    private String secondaryVersion;
    /**
     * 路径
     */
    private String path;

    /**
     * 相对路径
     */
    private String pathRelative;
    /**
     * 源文件路径
     */
    private String srcPath;
    /**
     * 上传时间
     */
    private String uploadTime;
    /**
     * 备注
     */
    private String description;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 0未部署，1部署中 2部署成功  3部署失败
     */
    private String deployStatus;
    /**
     * shell脚本退出错误码
     */
    private String errorCode;
    /**
     * 部署消息
     */
    private String deployMsg;
    /**
     * 部署时间
     */
    private String deployTime;
    /**
     * 版本类型   1 升级包   2  补丁包
     */
    private String versionType;
    /**
     * 服务器类型   1 SE  2 PE  3 DEPSERVER
     */
    private String serverType;
    /**
     * 预留1，跨版本升级目标大版本号
     */
    private String bak1;
    /**
     * 预留2
     */
    private String bak2;
    /**
     * 预留3
     */
    private String bak3;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getSystemType()
    {
        return systemType;
    }

    public void setSystemType(String systemType)
    {
        this.systemType = systemType;
    }


    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }


    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }


    public String getSecondaryVersion()
    {
        return secondaryVersion;
    }

    public void setSecondaryVersion(String secondaryVersion)
    {
        this.secondaryVersion = secondaryVersion;
    }


    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }


    public String getPathRelative()
    {
        return pathRelative;
    }

    public void setPathRelative(String pathRelative)
    {
        this.pathRelative = pathRelative;
    }


    public String getSrcPath()
    {
        return srcPath;
    }

    public void setSrcPath(String srcPath)
    {
        this.srcPath = srcPath;
    }


    public String getUploadTime()
    {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime)
    {
        this.uploadTime = uploadTime;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(String fileSize)
    {
        this.fileSize = fileSize;
    }


    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }


    public String getDeployStatus()
    {
        return deployStatus;
    }

    public void setDeployStatus(String deployStatus)
    {
        this.deployStatus = deployStatus;
    }


    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }


    public String getDeployMsg()
    {
        return deployMsg;
    }

    public void setDeployMsg(String deployMsg)
    {
        this.deployMsg = deployMsg;
    }


    public String getDeployTime()
    {
        return deployTime;
    }

    public void setDeployTime(String deployTime)
    {
        this.deployTime = deployTime;
    }


    public String getVersionType()
    {
        return versionType;
    }

    public void setVersionType(String versionType)
    {
        this.versionType = versionType;
    }


    public String getServerType()
    {
        return serverType;
    }

    public void setServerType(String serverType)
    {
        this.serverType = serverType;
    }


    public String getBak1()
    {
        return bak1;
    }

    public void setBak1(String bak1)
    {
        this.bak1 = bak1;
    }


    public String getBak2()
    {
        return bak2;
    }

    public void setBak2(String bak2)
    {
        this.bak2 = bak2;
    }


    public String getBak3()
    {
        return bak3;
    }

    public void setBak3(String bak3)
    {
        this.bak3 = bak3;
    }

}
