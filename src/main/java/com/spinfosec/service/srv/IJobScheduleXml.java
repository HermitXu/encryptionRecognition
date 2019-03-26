package com.spinfosec.service.srv;

import com.spinfosec.dto.pojo.deploy.JobDeployObj;

import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IJobScheduleXml
 * @Description: 〈任务部署xml处理接口〉
 * @date 2018/11/12
 * All rights Reserved, Designed By SPINFO
 */
public interface IJobScheduleXml
{
	/**
	 * 生成definition.xml
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	List<JobDeployObj> jobScheduleCreateXml(List<String> ids) throws Exception;

}
