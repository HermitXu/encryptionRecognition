package com.spinfosec.mq.consumer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spinfosec.dao.entity.SpServerStatus;
import com.spinfosec.dao.tactic.BasicSettingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName SysStatusConsumer
 * @Description: 〈服务器状态信息监听〉
 * @date 2018/11/20
 * All rights Reserved, Designed By SPINFO
 */
@Component
public class SysStatusConsumer
{
	private Logger log = LoggerFactory.getLogger(SysStatusConsumer.class);

	@Autowired
	private BasicSettingDao basicSettingDao;

	@JmsListener(destination = "statSystemStatus", containerFactory = "")
	public void receiveSysStatus(String content)
	{
		JSONObject json = JSONObject.parseObject(content);
		JSONArray net = JSONArray.parseArray(json.get("net").toString());
		JSONArray disk = JSONArray.parseArray(json.get("disk").toString());
		Iterator<Object> ite = net.iterator();
		Iterator<Object> ite_disk = disk.iterator();
		double disktotal = 0.0;
		double diskFree = 0.0;

		while (ite_disk.hasNext())
		{
			JSONObject json_disk = JSONObject.parseObject(ite_disk.next().toString());
			disktotal = disktotal + Double.parseDouble(json_disk.getString("total"));
			diskFree = diskFree + Double.parseDouble(json_disk.getString("free"));
		}
		while (ite.hasNext())
		{
			JSONObject itObject = JSONObject.parseObject(ite.next().toString());
			Object status = itObject.get("status");
			if (null != status)
			{
				if (Boolean.valueOf(status.toString()))
				{
					SpServerStatus serverStatus = basicSettingDao.getServerStatus();
					if (serverStatus == null)
					{
						serverStatus = new SpServerStatus();
						serverStatus.setId(1L);
						serverStatus.setCpuUsage(Double.parseDouble(json.get("cpu").toString()));
						serverStatus.setFreeDisk(diskFree);
						serverStatus.setMemUsage(Double.parseDouble(json.get("mem").toString()));
						serverStatus.setNetName(itObject.get("nic").toString());
						serverStatus.setHostname("");
						serverStatus.setHostIp(itObject.get("ip").toString());
						serverStatus.setTotalDisk(String.valueOf(disktotal));
						serverStatus.setGetTime(new Date());
						basicSettingDao.saveServerStatus(serverStatus);
					}
					else
					{
						serverStatus.setCpuUsage(Double.parseDouble(json.get("cpu").toString()));
						serverStatus.setFreeDisk(diskFree);
						serverStatus.setMemUsage(Double.parseDouble(json.get("mem").toString()));
						serverStatus.setNetName(itObject.get("nic").toString());
						serverStatus.setHostIp(itObject.get("ip").toString());
						serverStatus.setTotalDisk(String.valueOf(disktotal));
						basicSettingDao.updateServerStatus(serverStatus);
					}

				}
			}
		}
		log.info("获取系统状态信息：" + content);
	}
}
