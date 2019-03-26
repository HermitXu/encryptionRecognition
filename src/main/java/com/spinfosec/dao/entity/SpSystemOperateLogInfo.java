package com.spinfosec.dao.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * @title [操作日志信息]
 * @ClassName: SpSystemOperateLogInfo
 * @description [操作日志信息]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 19:52
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpSystemOperateLogInfo implements Serializable
{

    private static final long serialVersionUID = 9049794587685634753L;
    /**
     * 主键id
     */
    private String id;
    /**
     * 鉴别器
     */
    private String discriminator;
    /**
     * 管理员名称
     */
    private String adminName;
    /**
     * 管理员id
     */
    private String adminId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * transactionId
     */
    private String transactionId;
    /**
     * 1
     */
    private double isLeaderForTx;
    /**
     *
     */
    private String message;
    /**
     * 日志生成时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date generationTimeTs;
    /**
     *
     */
    private String entityType;
    /**
     * 操作信息
     */
    private String operation;
    /**
     *
     */
    private String entityId;
    /**
     *
     */
    private String businessId;

	/**
	 * 操作结果 0 成功 1 失败
	 */
	private int result;

    public String getDiscriminator()
    {
        return discriminator;
    }

    public void setDiscriminator(String discriminator)
    {
        this.discriminator = discriminator;
    }

	public int getResult()
	{
		return result;
	}

	public void setResult(int result)
	{
		this.result = result;
	}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getAdminName()
    {
        return adminName;
    }

    public void setAdminName(String adminName)
    {
        this.adminName = adminName;
    }


    public String getAdminId()
    {
        return adminId;
    }

    public void setAdminId(String adminId)
    {
        this.adminId = adminId;
    }


    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }


    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }


    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }


    public double getIsLeaderForTx()
    {
        return isLeaderForTx;
    }

    public void setIsLeaderForTx(double isLeaderForTx)
    {
        this.isLeaderForTx = isLeaderForTx;
    }


    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }


    public Date getGenerationTimeTs()
    {
        return generationTimeTs;
    }

    public void setGenerationTimeTs(Date generationTimeTs)
    {
        this.generationTimeTs = generationTimeTs;
    }


    public String getEntityType()
    {
        return entityType;
    }

    public void setEntityType(String entityType)
    {
        this.entityType = entityType;
    }


    public String getOperation()
    {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }


    public String getEntityId()
    {
        return entityId;
    }

    public void setEntityId(String entityId)
    {
        this.entityId = entityId;
    }


    public String getBusinessId()
    {
        return businessId;
    }

    public void setBusinessId(String businessId)
    {
        this.businessId = businessId;
    }

}
