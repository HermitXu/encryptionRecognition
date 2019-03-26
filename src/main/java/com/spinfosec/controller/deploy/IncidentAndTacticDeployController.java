package com.spinfosec.controller.deploy;

import com.alibaba.fastjson.JSON;
import com.spinfosec.dao.tactic.DiscoveryTasksDao;
import com.spinfosec.dto.pojo.common.ResponseBean;
import com.spinfosec.dto.pojo.deploy.JobDeployObj;
import com.spinfosec.dto.pojo.deploy.OperateJobData;
import com.spinfosec.dto.pojo.deploy.QueryUnDeployData;
import com.spinfosec.encryptAnalyze.analyor.DatabaseAnalyze;
import com.spinfosec.encryptAnalyze.analyor.FileSystemAnalyze;
import com.spinfosec.service.srv.IJobScheduleXml;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import com.spinfosec.utils.Contants;
import com.spinfosec.utils.MQUtil;
import com.spinfosec.utils.ResultUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName IncidentAndTacticDeployController
 * @Description: 〈策略（任务）部署〉
 * @date 2018/11/12
 * All rights Reserved, Designed By SPINFO
 */
@RestController
@RequestMapping("/deploy/incidentAndTactic")
public class IncidentAndTacticDeployController
{
	private static final Logger log = LoggerFactory.getLogger(IncidentAndTacticDeployController.class);

	private static final long DEPLOY_TIMEOUT = 6 * 60 * 1000;

	@Autowired
	private IJobScheduleXml jobScheduleXml;

	@Autowired
	private ActiveMQConnectionFactory connectionFactory;

	@Autowired
	private DiscoveryTasksDao discoveryTasksDao;

	@RequestMapping(value = "/deploy", method = RequestMethod.POST)
	public @ResponseBody ResponseBean deploy(HttpServletRequest req, @RequestBody QueryUnDeployData[] unDeployData)
			throws Exception
	{
		log.info("start deploy！");
		List<String> policyIds = new ArrayList<String>();
		List<String> successIds = new ArrayList<String>();
		for (QueryUnDeployData queryUnDeployData : unDeployData)
		{
			policyIds.add(queryUnDeployData.getId());
		}
		Thread policyThread = null;
		if (!policyIds.isEmpty())
		{
			// 部署和规则组相关的策略
			policyThread = deployPolicy(policyIds, successIds);
			policyThread.start();
		}
		if (null != policyThread)
		{
			policyThread.join(DEPLOY_TIMEOUT);
			policyThread.interrupt();
		}

		return ResultUtil.getSuccessResult(successIds);
	}

	private Thread deployPolicy(final List<String> policyIds, final List<String> successIds)
	{
		Thread policyThread = new Thread()
		{
			@Override
			public void run()
			{
				boolean deployIncidentAndTactic = doDeployIncidentAndTactic(policyIds);
				if (deployIncidentAndTactic)
				{
					successIds.addAll(policyIds);
                    for (String policyId : policyIds)
                    {
                        log.info("编辑时清理FileSystem解析器下策略" + policyId + "的map数据");
                        FileSystemAnalyze.clear(policyId);
                        log.info("编辑时清理Database解析器下策略" + policyId + "的map数据");
                        DatabaseAnalyze.dropDataTableMap.remove(policyId);
                    }
				}
			}
		};
		return policyThread;
	}

	private boolean doDeployIncidentAndTactic(List<String> taskIds)
	{
		boolean isOk = false;
		try
		{
			List<JobDeployObj> jobDocumentList = null;
			if (!taskIds.isEmpty())
			{
				jobDocumentList = jobScheduleXml.jobScheduleCreateXml(taskIds);
			}
			// 如果存在definetion则发送消息,如果不存在则不发送消息
			if (null != jobDocumentList && jobDocumentList.size() > 0)
			{
				OperateJobData msgData = new OperateJobData();
				msgData.setContent(jobDocumentList);
				msgData.setType(OperateJobData.ADD_JOB);
				String msgJson = JSON.toJSONString(msgData);
				// 异步发送
				log.debug("部署策略内容：" + msgJson);

				MQUtil.sendMessageOnly(connectionFactory, Contants.JOB_OPERATE, msgJson);

				isOk = true;
				for (String taskId : taskIds)
				{
					// 修改策略状态 已部署
					discoveryTasksDao.updateTaskStatus("SYNCHRONIZED", taskId);
				}
				log.info("deploy policy success!");
			}

		}
		catch (Exception e)
		{
			log.error("部署策略失败,失败原因:" + e.getMessage(), e);
			e.printStackTrace();
			throw new TMCException(RspCode.DEPLOY_POLICY_FAILURE, e);
		}
		return isOk;
	}
}
