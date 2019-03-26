package com.spinfosec.dto.pojo.system;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName ReportWordData
 * @Description: 〈一句话功能简述〉
 * @date 2018/11/14
 * All rights Reserved, Designed By SPINFO
 */
public class ReportWordData
{
	/**
	 * 主键ID
	 */
	private String id;

	/**
	 * 任务名称
	 */
	private String name;

	/**
	 * 目标ID
	 */
	private String targetIp;

	/**
	 * 被检单位ID
	 */
	private String beCheckOrgId;

	/**
	 * 被检单位名称
	 */
	private String beCheckOrgName;

	/**
	 * 检查单位名称
	 */
	private String checkOrgName;

	/**
	 * 检查时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date checkDate;

	/**
	 * 检查数量
	 */
	private String checkNum;

	/**
	 * 密文总数
	 */
	private Long ciphertextNum;

	/**
	 * 明文总数
	 */
	private Long plaintextNum;

	/**
	 * 商用密文总数
	 */
	private Long businessPassNum;

	/**
	 * 非商用密文总数  AES、Camelia、DES、3DES四种算法，均为非商用密码算法
	 */
	private Long unBusinessPassNum;

	public String getBeCheckOrgName()
	{
		return beCheckOrgName;
	}

	public void setBeCheckOrgName(String beCheckOrgName)
	{
		this.beCheckOrgName = beCheckOrgName;
	}

	public Long getBusinessPassNum()
	{
		return businessPassNum;
	}

	public void setBusinessPassNum(Long businessPassNum)
	{
		this.businessPassNum = businessPassNum;
	}

	public Long getUnBusinessPassNum()
	{
		return unBusinessPassNum;
	}

	public void setUnBusinessPassNum(Long unBusinessPassNum)
	{
		this.unBusinessPassNum = unBusinessPassNum;
	}

	public Long getCiphertextNum()
	{
		return ciphertextNum;
	}

	public void setCiphertextNum(Long ciphertextNum)
	{
		this.ciphertextNum = ciphertextNum;
	}

	public Long getPlaintextNum()
	{
		return plaintextNum;
	}

	public void setPlaintextNum(Long plaintextNum)
	{
		this.plaintextNum = plaintextNum;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTargetIp()
	{
		return targetIp;
	}

	public void setTargetIp(String targetIp)
	{
		this.targetIp = targetIp;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getBeCheckOrgId()
	{
		return beCheckOrgId;
	}

	public void setBeCheckOrgId(String beCheckOrgId)
	{
		this.beCheckOrgId = beCheckOrgId;
	}

	public String getCheckOrgName()
	{
		return checkOrgName;
	}

	public void setCheckOrgName(String checkOrgName)
	{
		this.checkOrgName = checkOrgName;
	}

	public Date getCheckDate()
	{
		return checkDate;
	}

	public void setCheckDate(Date checkDate)
	{
		this.checkDate = checkDate;
	}

	public String getCheckNum()
	{
		return checkNum;
	}

	public void setCheckNum(String checkNum)
	{
		this.checkNum = checkNum;
	}
}
