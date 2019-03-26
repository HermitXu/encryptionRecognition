package com.spinfosec.dto.pojo.deploy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: OperateJobData
 * @Description:TODO(操作job数据结构体)
 * @author: zhangpf
 * @date: 2014-7-10 下午5:22:12 All rights Reserved, Designed By SPINFO Copyright: Copyright(C) 2010-2011
 */
public class OperateJobData
{
	/**
	 * 任务操作消息体
	 */
	private List<JobDeployObj> content = new ArrayList<JobDeployObj>();
	/**
	 * 任务操作类型 : addJob,deleteJob,startJob,stopJob
	 */
	private String type;

	public static final String ADD_JOB = "addJob";

	public static final String DELETE_JOB = "deleteJob";

	public static final String START_JOB = "startJob";

	public static final String STOP_JOB = "stopJob";

	public static final String CLEAR_DATA = "clearData";

	public static final String DELETE_MONGODB = "deleteMongoDB";

	public List<JobDeployObj> getContent()
	{
		return content;
	}

	public void setContent(List<JobDeployObj> content)
	{
		this.content = content;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public OperateJobData getInstance(List<String> jobIds, String type)
	{
		for (String jobId : jobIds)
		{
			JobDeployObj deployObj = new JobDeployObj();
			deployObj.setJobId(jobId);
			deployObj.setTaskIds(new ArrayList<String>());
			this.content.add(deployObj);
		}
		this.setType(type);
		return this;
	}

	public OperateJobData getInstance(Map<String, List<String>> map, String type)
	{
		Set<Map.Entry<String, List<String>>> entries = map.entrySet();
		for (Map.Entry<String, List<String>> entry : entries)
		{
			String jobId = entry.getKey();
			List<String> taskIds = entry.getValue();
			JobDeployObj deployObj = new JobDeployObj();
			deployObj.setJobId(jobId);
			deployObj.setTaskIds(taskIds);
			this.content.add(deployObj);
		}
		this.setType(type);
		return this;
	}
}
