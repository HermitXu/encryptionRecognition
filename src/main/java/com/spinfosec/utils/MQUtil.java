package com.spinfosec.utils;

import com.spinfosec.mq.producer.MyQueueRequestor;
import com.spinfosec.system.RspCode;
import com.spinfosec.system.TMCException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 *
 * @author xuqy
 * @version version 1.0
 * @ClassName MQUtil
 * @Description: 〈MQ消息工具〉
 * @date 2018/11/9
 * All rights Reserved, Designed By SPINFO
 */
public class MQUtil
{
	private static Logger log = LoggerFactory.getLogger(MQUtil.class);

	// 默认超时60秒发送接收
	public static String sendMessage(ActiveMQConnectionFactory connectionFactory, String queueName, String message)
	{
		String response = "";
		QueueConnection conn = null;
		try
		{
			conn = connectionFactory.createQueueConnection();
			conn.start();
			QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue requestQueue = session.createQueue(queueName);
			Message msg = (TextMessage) session.createTextMessage(message);
			log.debug("消息队列名称：" + queueName);
			log.debug("发送消息内容：" + message);
			MyQueueRequestor requestor = new MyQueueRequestor(session, requestQueue);
			TextMessage reply = (TextMessage) requestor.request(msg);

			if (null != reply)
			{
				response = reply.getText();
				log.debug("返回消息内容 ： " + response);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new TMCException(RspCode.MQ_CONNECTION_ERROR);
		}
		finally
		{
			try
			{
				if (null != conn)
				{
					conn.stop();
					conn.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new TMCException(RspCode.MQ_CONNECTION_ERROR);
			}
		}

		return response;
	}

	// 自定义超时时间
	public static String sendMessage(ActiveMQConnectionFactory connectionFactory, String queueName, String message,
			long reciveTimeout)
	{
		String response = "";
		QueueConnection conn = null;
		try
		{
			conn = connectionFactory.createQueueConnection();
			conn.start();
			QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue requestQueue = session.createQueue(queueName);
			Message msg = (TextMessage) session.createTextMessage(message);
			log.debug("消息队列名称：" + queueName);
			log.debug("发送消息内容：" + message);
			MyQueueRequestor requestor = new MyQueueRequestor(session, requestQueue, reciveTimeout);
			TextMessage reply = (TextMessage) requestor.request(msg);
			if (null != reply)
			{
				response = reply.getText();
				log.debug("返回消息内容 ：" + response);
			}
		}
		catch (JMSException e)
		{
			e.printStackTrace();
			throw new TMCException(RspCode.MQ_CONNECTION_ERROR);
		}
		finally
		{
			try
			{
				if (null != conn)
				{
					conn.stop();
					conn.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new TMCException(RspCode.MQ_CONNECTION_ERROR);
			}
		}
		return response;
	}

	public static void sendMessageOnly(ActiveMQConnectionFactory connectionFactory, String queueName, String message)
	{
		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageProducer producer = null;
		try
		{
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			TextMessage msg = session.createTextMessage(message);
			connection.start();
			log.debug("消息队列名称：" + queueName);
			log.debug("发送消息内容：" + message);
			producer.send(msg);
		}
		catch (JMSException e)
		{
			e.printStackTrace();
			throw new TMCException(RspCode.MQ_CONNECTION_ERROR);
		}
		finally
		{
			try
			{
				if (null != producer)
				{
					producer.close();
				}
				if (null != session)
				{
					session.close();
				}
				if (null != connection)
				{
					connection.close();
				}
			}
			catch (JMSException e)
			{
				e.printStackTrace();
			}

		}
	}
}
