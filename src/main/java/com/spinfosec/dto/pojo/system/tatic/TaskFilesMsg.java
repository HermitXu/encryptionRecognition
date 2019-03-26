package com.spinfosec.dto.pojo.system.tatic;

import com.spinfosec.dao.entity.SpTaskFiles;
import com.spinfosec.dao.entity.SpTaskSipped;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName TaskFilesMsg
 * @Description: 〈任务文件处理信息模型〉
 * @date 2018/12/4
 * All rights Reserved, Designed By SPINFO
 */
public class TaskFilesMsg
{
	/**
	 * 处理文件信息
	 */
	private SpTaskFiles successFile;

	/**
	 * 处理失败文件信息
	 */
	private SpTaskSipped failedFile;

	public SpTaskFiles getSuccessFile()
	{
		return successFile;
	}

	public void setSuccessFile(SpTaskFiles successFile)
	{
		this.successFile = successFile;
	}

	public SpTaskSipped getFailedFile()
	{
		return failedFile;
	}

	public void setFailedFile(SpTaskSipped failedFile)
	{
		this.failedFile = failedFile;
	}
}
