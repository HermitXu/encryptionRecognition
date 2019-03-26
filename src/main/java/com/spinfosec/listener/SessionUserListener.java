package com.spinfosec.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spinfosec.dto.enums.SessionItem;
import org.springframework.stereotype.Component;

/**
 * @author xuqiuyu
 * @version V1.0
 * @title SessionUserListener
 * @Description: 监听用户会话
 * @date 2017年10月20日
 */
@Component
@WebListener
public class SessionUserListener implements HttpSessionListener
{

	/**
	 * 加载日志
	 */
	private static Logger log = LoggerFactory.getLogger(SessionUserListener.class);

	public static Map<String, String> userIds = new HashMap<String, String>();
	public static Map<String, HttpSession> ids = new HashMap<String, HttpSession>();

	@Override
	public void sessionCreated(HttpSessionEvent se)
	{
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se)
	{
		HttpSession session = se.getSession();
		deleteSession(session);

	}

	/**
	 * 可能存在无状态session 这时需要判断当前session是否存储用户信息，
	 * 将存储用户信息有价值的session移除集合
	 * @param session 销毁session
	 */
	public static synchronized void deleteSession(HttpSession session)
	{
		if (session != null)
		{
			if (StringUtils.isNotEmpty(session.getId()))
			{
				String userId = (String) session.getAttribute(SessionItem.userId.name());
				SessionUserListener.userIds.remove(userId);
				log.info("SessionUserListener 会话监听销毁过期session：" + session.getId());
			}
		}

	}

}
