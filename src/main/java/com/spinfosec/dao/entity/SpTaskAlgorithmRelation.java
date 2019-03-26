package com.spinfosec.dao.entity;


import java.io.Serializable;
/**
 * @title [策略算法关系表]
 * @ClassName: SpTaskAlgorithmRelation
 * @description [策略算法关系表]
 * @author ank
 * @version v 1.0
 * @create 2018/9/6 20:18
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class SpTaskAlgorithmRelation implements Serializable
{

    private static final long serialVersionUID = -5087846445648757849L;
    /**
     * 主键
     */
    private String id;
    private String taskId;
    private String algorithmId;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }


    public String getAlgorithmId()
    {
        return algorithmId;
    }

    public void setAlgorithmId(String algorithmId)
    {
        this.algorithmId = algorithmId;
    }

}
