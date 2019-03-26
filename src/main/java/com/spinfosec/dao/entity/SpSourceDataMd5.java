package com.spinfosec.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangpf
 * @version v 1.0
 * @title [标题]
 * @ClassName: com.spinfosec.dao.entity.bmj.SpSourceDataMd5
 * @description [一句话描述]
 * @copyright Copyright 2013 SHIPING INFO Corporation. All rights reserved.
 * @company SHIPING INFO.
 * @create 2016/3/31 16:18
 * All rights Reserved, Designed By SPINFO
 * Copyright:    Copyright(C) 2014-2015
 */
public class SpSourceDataMd5 implements Serializable
{
    private static final long serialVersionUID = -2458373551251926300L;

    private String id;

    private String md5Value;

    private String filePath;

    private String fileType;

    private String breachContent;

    private String matchContent;

    private String ruleId;

    private String ruleName;

    private Date createDate;

    private String adminId;

    private String orgId;

    private String orgName;

    private String resourceType;

    private String resourceName;

    private float secretRate;

    private String fileName;

    private String jobId;

    private BigDecimal falsePositive;

    private int subsystem;

    private Date detectDateTs;

    public String getResourceName()
    {
        return resourceName;
    }

    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
    }

    public float getSecretRate()
    {
        return secretRate;
    }

    public void setSecretRate(float secretRate)
    {
        this.secretRate = secretRate;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMd5Value()
    {
        return md5Value;
    }

    public void setMd5Value(String md5Value)
    {
        this.md5Value = md5Value;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getBreachContent() {
        return breachContent;
    }

    public void setBreachContent(String breachContent)
    {
        this.breachContent = breachContent;
    }

    public String getMatchContent()
    {
        return matchContent;
    }

    public void setMatchContent(String matchContent)
    {
        this.matchContent = matchContent;
    }

    public String getRuleId()
    {
        return ruleId;
    }

    public void setRuleId(String ruleId)
    {
        this.ruleId = ruleId;
    }


    public String getRuleName()
    {
        return ruleName;
    }

    public void setRuleName(String ruleName)
    {
        this.ruleName = ruleName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId)
    {
        this.adminId = adminId;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public String getOrgName()
    {
        return orgName;
    }

    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public BigDecimal getFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(BigDecimal falsePositive) {
        this.falsePositive = falsePositive;
    }

    public int getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(int subsystem) {
        this.subsystem = subsystem;
    }

    public Date getDetectDateTs() {
        return detectDateTs;
    }

    public void setDetectDateTs(Date detectDateTs) {
        this.detectDateTs = detectDateTs;
    }
}

