package com.spinfosec.dao.entity;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SpEncryptionFileType
 * @Description: 〈加密算法文件类型关系〉
 * @date 2018/11/7
 * All rights Reserved, Designed By SPINFO
 */
public class SpAlgorithmFileType
{
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 加密算法库ID
	 */
	private String algorithmId;

	/**
	 * 策略ID
	 */
	private String jobId;

	/**
	 * 文件类型
	 */
	private String fileType;

	public String getJobId()
	{
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getAlgorithmId()
	{
		return algorithmId;
	}

	public void setAlgorithmId(String algorithmId)
	{
		this.algorithmId = algorithmId;
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

}
