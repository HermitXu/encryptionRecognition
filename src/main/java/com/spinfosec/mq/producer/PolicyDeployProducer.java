package com.spinfosec.mq.producer;

import javax.jms.Message;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PolicyDeployProducer
{

	private JmsTemplate template;
	private ActiveMQQueue destination;

	public void setTemplate(JmsTemplate template)
	{
		this.template = template;
	}

	public void setDestination(ActiveMQQueue destination)
	{
		this.destination = destination;
	}

	public void send(Message message)
	{
		template.convertAndSend(this.destination, message);
	}

	public void sendByMe(String mess)
	{
		template.convertAndSend(this.destination, mess);
	}

}
